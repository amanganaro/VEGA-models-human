import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import model.ModelExecutionTest;
import insilico.apicalcardiotox.ApicalCardioTox;

public class ApicalCardioToxTest extends ModelExecutionTest {

    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ApicalCardioTox(true, null);
    }
}
