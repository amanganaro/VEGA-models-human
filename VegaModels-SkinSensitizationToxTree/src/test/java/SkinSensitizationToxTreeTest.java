import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.skin_sensitization_toxtree.ismSkinSensitizationToxTree;
import model.ModelExecutionTest;

public class SkinSensitizationToxTreeTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismSkinSensitizationToxTree();
    }
}
