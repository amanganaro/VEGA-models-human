import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.micronuclueus_vivo.ismMicronucleusInVivo;
import model.ModelExecutionTest;

public class MicronucleusVivoTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismMicronucleusInVivo();
    }
}
