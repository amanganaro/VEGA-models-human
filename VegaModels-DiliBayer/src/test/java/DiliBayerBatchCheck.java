import insilico.dilibayer.ismDiliBayer;
import model.PythonBatchComparison;

/**
 * Checks that this model gives the same results in batch as it does one molecule at a time, and reports the time
 * saved.
 * <p>
 * Batch execution was enabled in version 1.0.1 of the Python package, which realigns the index of the input frame
 * before merging it with the predictions. Until then a molecule dropped while standardizing shifted all the
 * following rows, pairing predictions with the wrong molecules.
 * <p>
 * This model also reads its command line arguments positionally, which is why it overrides
 * {@code buildBatchCommandParams}.
 */
public class DiliBayerBatchCheck {

    public static void main(String[] args) throws Exception {
        ismDiliBayer model = new ismDiliBayer(true, null);
        int problems = PythonBatchComparison.run(model);
        System.exit(problems == 0 ? 0 : 1);
    }
}
