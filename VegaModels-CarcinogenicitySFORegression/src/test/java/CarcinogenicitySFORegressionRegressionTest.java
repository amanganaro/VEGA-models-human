import insilico.core.model.InsilicoModel;
import insilico.carcinogenicity_sforegression.ismCarcinogenicitySFORegression;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the CarcinogenicitySFORegression model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class CarcinogenicitySFORegressionRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismCarcinogenicitySFORegression());
    }
}
