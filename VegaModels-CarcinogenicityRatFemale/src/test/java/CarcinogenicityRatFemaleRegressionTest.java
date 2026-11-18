import insilico.core.model.InsilicoModel;
import insilico.carcinogenicity_rat_female.ismCarcinogenicityRatFemale;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the CarcinogenicityRatFemale model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class CarcinogenicityRatFemaleRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismCarcinogenicityRatFemale());
    }
}
