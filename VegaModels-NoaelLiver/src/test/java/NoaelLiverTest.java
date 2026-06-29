import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.noael_coral_liver.ismNoaelCoralLiver;
import model.ModelExecutionTest;

public class NoaelLiverTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismNoaelCoralLiver();
    }
}
