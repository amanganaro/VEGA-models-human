import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.mutagenicity_sarpy.ismMutagenicitySarpy;
import model.ModelExecutionTest;

public class MutagenicitySarpyTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismMutagenicitySarpy();
    }
}
