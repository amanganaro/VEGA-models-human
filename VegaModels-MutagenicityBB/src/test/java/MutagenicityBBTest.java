import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.mutagenicity_bb.ismMutagenicityBB;
import model.ModelExecutionTest;

public class MutagenicityBBTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismMutagenicityBB();
    }
}
