import insilico.core.model.InsilicoModel;
import insilico.noael_general_coral.ismNoaelGeneralCoral;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the NoaelGeneralCoral model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class NoaelGeneralCoralRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismNoaelGeneralCoral());
    }
}
