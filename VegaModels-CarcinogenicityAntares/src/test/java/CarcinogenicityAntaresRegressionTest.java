import insilico.core.model.InsilicoModel;
import insilico.carcinogenicity_antares.ismCarcinogenicityAntares;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the CarcinogenicityAntares model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class CarcinogenicityAntaresRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismCarcinogenicityAntares());
    }
}
