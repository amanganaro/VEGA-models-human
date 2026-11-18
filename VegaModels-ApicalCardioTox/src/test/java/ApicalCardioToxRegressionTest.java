import insilico.core.model.InsilicoModel;
import insilico.apicalcardiotox.ApicalCardioTox;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the ApicalCardioTox model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class ApicalCardioToxRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new ApicalCardioTox(true, null));
    }
}
