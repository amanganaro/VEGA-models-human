import insilico.core.model.InsilicoModel;
import insilico.dilibayer.ismDiliBayer;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the Drug-induced Liver Injury (Bayer/ONTOX) model, a Python model using the CDDD descriptors,
 * see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class DiliBayerRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismDiliBayer(true, null));
    }
}
