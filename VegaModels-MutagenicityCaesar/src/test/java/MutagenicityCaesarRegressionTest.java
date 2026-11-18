import insilico.core.model.InsilicoModel;
import insilico.mutagenicity_caesar.ismMutagenicityCaesar;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the MutagenicityCaesar model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class MutagenicityCaesarRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismMutagenicityCaesar());
    }
}
