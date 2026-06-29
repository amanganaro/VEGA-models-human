import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.noael_general_coral.ismNoaelGeneralCoral;
import model.ModelExecutionTest;

public class NoaelGeneralCoralTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismNoaelGeneralCoral();
    }
}
