import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.skin_irritation.ismSkinIrritation;
import model.ModelExecutionTest;

public class SkinIrritationTest extends ModelExecutionTest {

    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismSkinIrritation();
    }
}
