import insilico.mitochondrial_dysfunction.MitochondrialDysfunction;
import model.PythonBatchComparison;

import java.util.Arrays;

/**
 * Checks that this model, which is deliberately <em>not</em> enabled for batch execution, still runs one molecule at
 * a time after the batch mode was added to the core.
 * <p>
 * Its Python script cannot predict several molecules at once: {@code inference} is called without
 * {@code singlemol=False} and builds a frame indexed by a single molecule. The change needed to enable it has to be
 * agreed with the authors of the script and is written in {@code docs/python-models-batch.md} of the core.
 */
public class MitochondrialDysfunctionBatchCheck {

    public static void main(String[] args) throws Exception {
        MitochondrialDysfunction model = new MitochondrialDysfunction(true, null);
        int problems = PythonBatchComparison.runPerMoleculeOnly(model, Arrays.asList(
                "O=[N+]([O-])c1cc(cc(c1N(CCC)CCC)[N+](=O)[O-])S(=O)(=O)C",
                "O=[N+]([O-])c1cc(c(O)c(c1)C(C)(C)C)[N+](=O)[O-]",
                "O=S(=O)(C(C)(C)C)C(C)(C)C"
        ));
        System.exit(problems == 0 ? 0 : 1);
    }
}
