import insilico.core.model.InsilicoModel;
import insilico.carcinogenicity_sfoclassification.ismCarcinogenicitySFOClassification;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the CarcinogenicitySFOClassification model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class CarcinogenicitySFOClassificationRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismCarcinogenicitySFOClassification());
    }
}
