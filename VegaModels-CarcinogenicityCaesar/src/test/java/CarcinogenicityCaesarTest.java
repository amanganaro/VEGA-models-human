import insilico.carcinogenicity_caesar.ismCarcinogenicityCaesar;
import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import model.ModelExecutionTest;

public class CarcinogenicityCaesarTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismCarcinogenicityCaesar();
    }
}
