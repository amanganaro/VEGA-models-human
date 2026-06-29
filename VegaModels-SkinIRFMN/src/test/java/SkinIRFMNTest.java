import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.skin_irfmn.ismSkinIRFMN;
import model.ModelExecutionTest;

public class SkinIRFMNTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismSkinIRFMN();
    }
}
