import insilico.core.model.InsilicoModel;
import insilico.noel_coral.ismNoaelCoral;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the NoelCoralIRFMN model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class NoaelLiverRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismNoaelCoral());
    }
}
