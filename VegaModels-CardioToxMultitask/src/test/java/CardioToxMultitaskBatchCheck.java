import insilico.cardiotoxMultitask.CardioToxMultitask;
import model.PythonBatchComparison;

/**
 * Checks that this model gives the same results in batch as it does one molecule at a time, and reports the time
 * saved.
 */
public class CardioToxMultitaskBatchCheck {

    public static void main(String[] args) throws Exception {
        CardioToxMultitask model = new CardioToxMultitask(true, null);
        int problems = PythonBatchComparison.run(model);
        System.exit(problems == 0 ? 0 : 1);
    }
}
