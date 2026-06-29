import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.skin_irritation_coral.ismSkinIrritationCoral;
import model.ModelExecutionTest;

public class SkinIrritationCoralTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismSkinIrritationCoral();
    }
}
