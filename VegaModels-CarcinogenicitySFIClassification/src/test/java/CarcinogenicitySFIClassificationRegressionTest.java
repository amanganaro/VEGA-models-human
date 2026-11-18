import insilico.core.model.InsilicoModel;
import insilico.carcinogenicity_sfi_classification.ismCarcinogenicitySFIClassification;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the CarcinogenicitySFIClassification model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class CarcinogenicitySFIClassificationRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismCarcinogenicitySFIClassification());
    }
}
