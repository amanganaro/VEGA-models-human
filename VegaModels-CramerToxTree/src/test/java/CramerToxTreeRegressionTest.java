import insilico.core.model.InsilicoModel;
import insilico.cramer_toxtree.ismCramerToxtree;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the CramerToxTree model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class CramerToxTreeRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ismCramerToxtree());
    }

    /**
     * The model has no training set, so only the core dataset applies. Without a training set it computes no
     * applicability domain either, so the ADI column of the core reference is empty for every molecule.
     */
    @Override
    protected boolean hasTrainingSet() {
        return false;
    }
}
