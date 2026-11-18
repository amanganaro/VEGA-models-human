import insilico.core.model.InsilicoModel;
import insilico.micronculeus_vitro.ismMicronucleusInVitro;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the MicronucleusVitro model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class MicronucleusVitroRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismMicronucleusInVitro());
    }
}
