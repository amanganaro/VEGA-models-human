import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.hepatoxicty_irfmn.ismHepatotoxicityIrfmn;
import model.ModelExecutionTest;

public class HepatoxicityIRFMNTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismHepatotoxicityIrfmn();
    }
}
