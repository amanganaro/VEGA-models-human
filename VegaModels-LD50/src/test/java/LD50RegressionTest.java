import insilico.core.model.InsilicoModel;
import insilico.ld50.ismLD50;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the LD50 model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class LD50RegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismLD50());
    }
}
