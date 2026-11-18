import insilico.core.model.InsilicoModel;
import insilico.eye_irritation_sarpy.ismEyeIrritationSarpy;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the EyeIrritationSarpy model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class EyeIrritationSarpyRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismEyeIrritationSarpy());
    }
}
