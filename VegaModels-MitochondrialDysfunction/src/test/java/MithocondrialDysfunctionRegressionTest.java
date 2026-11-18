import insilico.core.model.InsilicoModel;
import insilico.mitochondrial_dysfunction.MitochondrialDysfunction;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the MitochondrialDysfunction model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class MithocondrialDysfunctionRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new MitochondrialDysfunction(true, null));
    }
}
