import insilico.core.model.InsilicoModel;
import insilico.carcinogenicity_sfiregression.ismCarcinogenicitySFIRegression;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the CarcinogenicitySFIRegression model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class CarcinogenicitySFIRegressionRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismCarcinogenicitySFIRegression());
    }
}
