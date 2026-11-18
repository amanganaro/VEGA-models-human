import insilico.core.model.InsilicoModel;
import insilico.loael_general_coral.ismLoaelGeneralCoral;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the LoaelGeneralCoral model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class LoaelGeneralCoralRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismLoaelGeneralCoral());
    }
}
