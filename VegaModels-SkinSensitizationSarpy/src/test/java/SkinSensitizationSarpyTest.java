import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.skin_sensitization_sarpy.ismSkinSensitizationSarpy;
import model.ModelExecutionTest;

public class SkinSensitizationSarpyTest extends ModelExecutionTest {

    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismSkinSensitizationSarpy();
    }
}
