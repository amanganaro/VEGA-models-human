import insilico.core.model.InsilicoModel;
import insilico.cardiotoxMultitask.CardioToxMultitask;
import regression.ModelRegressionTest;

import java.util.List;

/**
 * Regression test of the CardioToxMultitask model, see {@link ModelRegressionTest}.
 * Run with {@code mvn test -Pregression}; regenerate the references with {@code -Dgolden.update=true}.
 */
public class CardioToxMultitaskRegressionTest extends ModelRegressionTest {

    @Override
    protected List<InsilicoModel> getModels() throws Exception {
        return List.of(new CardioToxMultitask(true, null));
    }
}
