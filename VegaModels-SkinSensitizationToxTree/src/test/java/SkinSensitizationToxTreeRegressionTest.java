import insilico.core.model.InsilicoModel;
import insilico.skin_sensitization_toxtree.ismSkinSensitizationToxTree;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the SkinSensitizationToxTree model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class SkinSensitizationToxTreeRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismSkinSensitizationToxTree());
    }
}
