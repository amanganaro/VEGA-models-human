import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.eye_irritation_sarpy.ismEyeIrritationSarpy;
import model.ModelExecutionTest;

public class EyeIrritationSarpyTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismEyeIrritationSarpy();
    }
}
