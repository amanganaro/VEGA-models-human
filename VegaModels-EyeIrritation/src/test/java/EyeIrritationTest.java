import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.eye_irritation.ismEyeIrritation;
import model.ModelExecutionTest;

public class EyeIrritationTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismEyeIrritation();
    }
}
