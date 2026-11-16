import insilico.mitochondrial_dysfunction.MitochondrialDysfunction;
import model.PythonBatchComparison;

/**
 * Checks that this model gives the same results in batch as it does one molecule at a time, and reports the time
 * saved.
 * <p>
 * Batch execution was enabled in version 1.0.1 of the Python package: {@code inference} is now called with
 * {@code singlemol=False} and its multi-molecule branch builds one row per molecule. Until then the script could
 * only predict a single molecule per execution.
 */
public class MitochondrialDysfunctionBatchCheck {

    public static void main(String[] args) throws Exception {
        MitochondrialDysfunction model = new MitochondrialDysfunction(true, null);
        int problems = PythonBatchComparison.run(model);
        System.exit(problems == 0 ? 0 : 1);
    }
}
