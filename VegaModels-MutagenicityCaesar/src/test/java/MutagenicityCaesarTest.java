import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.mutagenicity_caesar.ismMutagenicityCaesar;
import model.ModelExecutionTest;

public class MutagenicityCaesarTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismMutagenicityCaesar();
    }
}
