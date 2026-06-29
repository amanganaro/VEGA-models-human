import insilico.carcinogenicity_sfoclassification.ismCarcinogenicitySFOClassification;
import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import model.ModelExecutionTest;

public class CarcinogenicitySFOClassificationTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismCarcinogenicitySFOClassification();
    }
}
