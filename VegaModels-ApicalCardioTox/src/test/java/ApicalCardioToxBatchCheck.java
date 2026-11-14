import insilico.apicalcardiotox.ApicalCardioTox;
import model.PythonBatchComparison;

/**
 * Checks that this model gives the same results in batch as it does one molecule at a time, and reports the time
 * saved. It was the pilot of the batch mode.
 * <p>
 * The alignment guard, which belongs to the core mechanism rather than to this model, is checked separately by
 * {@link BatchGuardHarness}.
 */
public class ApicalCardioToxBatchCheck {

    public static void main(String[] args) throws Exception {
        ApicalCardioTox model = new ApicalCardioTox(false, null);
        int problems = PythonBatchComparison.run(model);
        System.exit(problems == 0 ? 0 : 1);
    }
}
