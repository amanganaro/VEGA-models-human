import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.devtox_caesar.ismDevtoxCaesar;
import model.ModelExecutionTest;

public class DevtoxCaesarTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismDevtoxCaesar();
    }
}
