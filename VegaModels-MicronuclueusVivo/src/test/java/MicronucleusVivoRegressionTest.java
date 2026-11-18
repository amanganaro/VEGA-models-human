import insilico.core.model.InsilicoModel;
import insilico.micronuclueus_vivo.ismMicronucleusInVivo;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the MicronuclueusVivo model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class MicronucleusVivoRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismMicronucleusInVivo());
    }
}
