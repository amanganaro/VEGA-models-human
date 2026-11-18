import insilico.core.model.InsilicoModel;
import insilico.devtox_pg.ismDevToxPG;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the DevtoxPG model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class DevtoxPGRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismDevToxPG());
    }
}
