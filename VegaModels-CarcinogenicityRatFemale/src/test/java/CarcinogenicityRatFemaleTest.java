import insilico.carcinogenicity_rat_female.ismCarcinogenicityRatFemale;
import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import model.ModelExecutionTest;

public class CarcinogenicityRatFemaleTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismCarcinogenicityRatFemale();
    }
}
