import insilico.core.model.InsilicoModel;
import insilico.eye_irritation.ismEyeIrritation;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the EyeIrritation model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class EyeIrritationRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismEyeIrritation());
    }
}
