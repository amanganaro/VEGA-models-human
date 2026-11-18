import insilico.core.model.InsilicoModel;
import insilico.carcinogenicity_caesar.ismCarcinogenicityCaesar;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the CarcinogenicityCaesar model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class CarcinogenicityCaesarRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismCarcinogenicityCaesar());
    }
}
