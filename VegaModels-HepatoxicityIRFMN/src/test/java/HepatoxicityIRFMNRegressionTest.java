import insilico.core.model.InsilicoModel;
import insilico.hepatoxicty_irfmn.ismHepatotoxicityIrfmn;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the HepatoxicityIRFMN model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class HepatoxicityIRFMNRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismHepatotoxicityIrfmn());
    }
}
