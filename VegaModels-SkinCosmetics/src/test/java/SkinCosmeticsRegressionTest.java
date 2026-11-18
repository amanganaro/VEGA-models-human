import insilico.core.model.InsilicoModel;
import insilico.skin_cosmetics.ismSkinCosmetics;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the SkinCosmetics model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class SkinCosmeticsRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismSkinCosmetics());
    }
}
