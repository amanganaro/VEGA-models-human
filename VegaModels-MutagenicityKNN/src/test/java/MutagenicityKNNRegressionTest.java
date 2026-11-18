import insilico.core.model.InsilicoModel;
import insilico.mutagenicity_knn.ismMutagenicityKnn;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the MutagenicityKNN model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class MutagenicityKNNRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismMutagenicityKnn());
    }
}
