import insilico.core.model.InsilicoModel;
import insilico.noael_coral_liver.ismNoaelCoralLiver;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the NoaelLiver model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class NoaelLiverRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismNoaelCoralLiver());
    }
}
