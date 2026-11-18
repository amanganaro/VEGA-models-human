import insilico.core.model.InsilicoModel;
import insilico.skin_caesar.ismSkinCaesar;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the SkinCaesar model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class SkinCaesarRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismSkinCaesar());
    }
}
