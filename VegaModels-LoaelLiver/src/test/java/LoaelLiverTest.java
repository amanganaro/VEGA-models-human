import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.loael_coral_liver.ismLoaelCoralLiver;
import model.ModelExecutionTest;

public class LoaelLiverTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismLoaelCoralLiver();
    }
}
