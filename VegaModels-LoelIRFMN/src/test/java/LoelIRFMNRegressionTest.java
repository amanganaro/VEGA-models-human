import insilico.core.model.InsilicoModel;
import insilico.loel_irfmn.ismLoelIrfmn;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the LoelIRFMN model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class LoelIRFMNRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismLoelIrfmn());
    }
}
