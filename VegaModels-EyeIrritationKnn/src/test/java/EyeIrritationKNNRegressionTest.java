import insilico.core.model.InsilicoModel;
import insilico.eye_irritation_knn.ismEyeIrritationKnn;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the EyeIrritationKnn model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class EyeIrritationKNNRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismEyeIrritationKnn());
    }
}
