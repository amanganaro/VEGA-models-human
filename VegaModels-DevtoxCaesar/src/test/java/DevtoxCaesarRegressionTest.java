import insilico.core.model.InsilicoModel;
import insilico.devtox_caesar.ismDevtoxCaesar;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the DevtoxCaesar model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class DevtoxCaesarRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismDevtoxCaesar());
    }
}
