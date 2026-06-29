import insilico.carcinogenicity_sforegression.ismCarcinogenicitySFORegression;
import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import model.ModelExecutionTest;

public class CarcinogenicitySFORegressionTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismCarcinogenicitySFORegression();
    }
}
