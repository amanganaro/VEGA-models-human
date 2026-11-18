import insilico.core.model.InsilicoModel;
import insilico.skin_permeation_potts.ismSkinPermeationPotts;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the SkinPermeationPotts model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class SkinPermeationPottsRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismSkinPermeationPotts());
    }
}
