import insilico.core.model.InsilicoModel;
import insilico.skin_permeation_tenberge.ismSkinPermeationTenBerge;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the SkinPermeationTenberge model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class SkinPermeationTenbergeRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismSkinPermeationTenBerge());
    }
}
