import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.dilibayer.ismDiliBayer;
import model.ModelExecutionTest;

public class DiliBayerTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismDiliBayer(true, null);
    }
}
