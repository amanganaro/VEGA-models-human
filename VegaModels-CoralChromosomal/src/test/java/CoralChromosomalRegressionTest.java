import insilico.core.model.InsilicoModel;
import insilico.chromosomal_coral.ismChromosomalAberrationCoral;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the CoralChromosomal model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class CoralChromosomalRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismChromosomalAberrationCoral());
    }
}
