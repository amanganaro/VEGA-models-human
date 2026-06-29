import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.micronculeus_vitro.ismMicronucleusInVitro;
import model.ModelExecutionTest;

public class MicronucleusVitroTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismMicronucleusInVitro();
    }
}
