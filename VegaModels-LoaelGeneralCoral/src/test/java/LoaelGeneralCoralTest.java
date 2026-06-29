import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.loael_general_coral.ismLoaelGeneralCoral;
import model.ModelExecutionTest;

public class LoaelGeneralCoralTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismLoaelGeneralCoral();
    }
}
