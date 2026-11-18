import insilico.core.model.InsilicoModel;
import insilico.skin_irritation_sarpy.ismSkinIrritationSarpy;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the SkinIrritationSarpy model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class SkinIrritationSarpyRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismSkinIrritationSarpy());
    }
}
