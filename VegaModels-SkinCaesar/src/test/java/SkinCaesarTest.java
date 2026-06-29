import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.skin_caesar.ismSkinCaesar;
import model.ModelExecutionTest;

public class SkinCaesarTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismSkinCaesar();
    }
}
