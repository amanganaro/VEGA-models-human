import insilico.core.model.InsilicoModel;
import insilico.mutagenicity_amines.ismMutagenicityAmines;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the MutagenicityAmines model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class MutagenicityAminesRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismMutagenicityAmines());
    }
}
