import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.skin_permeation_tenberge.ismSkinPermeationTenBerge;
import model.ModelExecutionTest;

public class SkinPermeationTenbergeTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismSkinPermeationTenBerge();
    }
}
