import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.loel_irfmn.ismLoelIrfmn;
import model.ModelExecutionTest;

public class LoelIRFMNTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismLoelIrfmn();
    }
}
