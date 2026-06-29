import insilico.carcinogenicity_antares.ismCarcinogenicityAntares;
import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import model.ModelExecutionTest;

public class CarcinogenicityAntaresTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismCarcinogenicityAntares();
    }
}
