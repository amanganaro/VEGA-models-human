import insilico.core.model.InsilicoModel;
import insilico.loael_coral_liver.ismLoaelCoralLiver;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the LoaelLiver model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class LoaelLiverRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismLoaelCoralLiver());
    }
}
