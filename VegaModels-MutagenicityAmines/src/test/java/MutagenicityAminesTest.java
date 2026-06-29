import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.mutagenicity_amines.ismMutagenicityAmines;
import model.ModelExecutionTest;

public class MutagenicityAminesTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismMutagenicityAmines();
    }
}
