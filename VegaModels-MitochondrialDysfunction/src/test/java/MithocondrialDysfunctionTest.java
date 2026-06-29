import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.mitochondrial_dysfunction.MitochondrialDysfunction;
import model.ModelExecutionTest;

public class MithocondrialDysfunctionTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new MitochondrialDysfunction(true, null);
    }
}
