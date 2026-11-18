import insilico.apicalcardiotox.ApicalCardioTox;
import insilico.core.exception.GenericFailureException;
import insilico.core.model.InsilicoModelOutput;
import insilico.core.molecule.InsilicoMolecule;
import insilico.core.molecule.conversion.SmilesMolecule;
import insilico.core.python.CdddDescriptors;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Checks that a batch never attributes a prediction to the wrong molecule.
 * <p>
 * A batch is only safe if every row that comes back is given to the molecule it was computed for. These two cases
 * simulate the two ways that can break — a row disappearing and the rows coming back in a different order — by
 * tampering with the input file. In both cases the results must be exactly the ones of the per-molecule execution.
 * <p>
 * Rows are matched by the SMILES each one carries, not by their position, because several Python scripts drop the
 * molecules they cannot handle. A molecule left without a row falls back to the per-molecule execution, which is
 * slower but always correct, so a missing row costs time and never correctness.
 */
public class BatchGuardHarness {

    private static final List<String> MOLECULES = Arrays.asList(
            "COc1cc(C=O)ccc1O",
            "NCCc1ccc(O)c(O)c1",
            "CN1CCC[C@H]1c1cccnc1",
            "C1=Cc2ccccc2C1"
    );

    private static int failures = 0;

    /** Drops the last molecule from the input file, so the script returns fewer rows than expected. */
    private static class MissingRowModel extends ApicalCardioTox {
        MissingRowModel() throws Exception {
            super(false, null);
        }

        @Override
        protected Map<String, String> buildBatchInputFile(List<String> smilesList, Path target)
                throws IOException, GenericFailureException {
            return super.buildBatchInputFile(smilesList.subList(0, Math.max(0, smilesList.size() - 1)), target);
        }
    }

    /** Reverses the input rows, so the results come back attributed to the wrong molecules. */
    private static class ReorderedRowsModel extends ApicalCardioTox {
        ReorderedRowsModel() throws Exception {
            super(false, null);
        }

        @Override
        protected Map<String, String> buildBatchInputFile(List<String> smilesList, Path target)
                throws IOException, GenericFailureException {
            List<String> reversed = new ArrayList<>(smilesList);
            Collections.reverse(reversed);
            return super.buildBatchInputFile(reversed, target);
        }
    }

    public static void main(String[] args) throws Exception {

        check("a row missing from the batch output", new MissingRowModel());
        check("the batch output rows in the wrong order", new ReorderedRowsModel());

        System.out.println();
        System.out.println(failures == 0
                ? "ALL GUARD CASES OK: the results stayed correct"
                : failures + " GUARD CASE(S) FAILED");
    }

    /**
     * Runs the model per molecule and then with the tampered batch: whether the batch is used or refused, the
     * results must match the per-molecule run cell by cell.
     */
    private static void check(String name, ApicalCardioTox model) throws Exception {

        System.out.println();
        System.out.println("--- " + name + " ---");

        CdddDescriptors cddd = new CdddDescriptors(new ArrayList<>(MOLECULES), true, null);
        if (!cddd.calculateDescriptors()) {
            System.out.println("FAILED (" + name + "): CDDD descriptors could not be computed");
            failures++;
            return;
        }
        model.setDescriptorGenerator(cddd);
        model.setCdddDescriptorsForBatch(cddd);

        List<InsilicoMolecule> molecules = new ArrayList<>();
        for (String smiles : MOLECULES) {
            molecules.add(SmilesMolecule.Convert(smiles));
        }

        try {
            model.clearBatch();
            List<String[]> expected = run(model, molecules);

            model.prepareBatch(molecules);
            System.out.println("isBatchActive: " + model.isBatchActive()
                    + " (either way, the results below must be unchanged)");

            List<String[]> actual = run(model, molecules);
            int differences = 0;
            for (int i = 0; i < expected.size(); i++) {
                String[] e = expected.get(i);
                String[] a = actual.get(i);
                if (e == null || a == null) {
                    if (e != a) {
                        differences++;
                    }
                    continue;
                }
                for (int c = 0; c < Math.min(e.length, a.length); c++) {
                    if (!Objects.equals(e[c], a[c])) {
                        System.out.printf("  [%d] column %d: expected='%s' got='%s'%n", i, c, e[c], a[c]);
                        differences++;
                    }
                }
            }

            if (differences > 0) {
                System.out.println("FAILED (" + name + "): " + differences + " differing cells");
                failures++;
            } else {
                System.out.println("OK: results unchanged");
            }
        } finally {
            model.clearBatch();
            cddd.dispose();
        }
    }

    private static List<String[]> run(ApicalCardioTox model, List<InsilicoMolecule> molecules) throws Exception {
        List<String[]> results = new ArrayList<>();
        for (InsilicoMolecule molecule : molecules) {
            InsilicoModelOutput out = model.Execute(molecule);
            String[] row = out.getResults();
            results.add(row == null ? null : row.clone());
        }
        return results;
    }
}
