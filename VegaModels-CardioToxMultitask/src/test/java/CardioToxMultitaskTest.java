import insilico.cardiotoxMultitask.CardioToxMultitask;
import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import model.ModelExecutionTest;

public class CardioToxMultitaskTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new CardioToxMultitask(true, null);
    }
}
