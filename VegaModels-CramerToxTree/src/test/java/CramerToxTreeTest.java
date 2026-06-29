import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.cramer_toxtree.ismCramerToxtree;
import model.ModelExecutionTest;

public class CramerToxTreeTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismCramerToxtree();
    }
}
