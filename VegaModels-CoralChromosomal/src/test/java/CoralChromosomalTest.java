import insilico.chromosomal_coral.ismChromosomalAberrationCoral;
import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import model.ModelExecutionTest;

public class CoralChromosomalTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismChromosomalAberrationCoral();
    }
}
