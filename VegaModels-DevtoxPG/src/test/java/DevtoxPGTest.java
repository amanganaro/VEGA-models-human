import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.devtox_pg.ismDevToxPG;
import model.ModelExecutionTest;

public class DevtoxPGTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismDevToxPG();
    }
}
