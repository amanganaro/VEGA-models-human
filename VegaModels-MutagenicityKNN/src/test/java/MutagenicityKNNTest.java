import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.mutagenicity_knn.ismMutagenicityKnn;
import model.ModelExecutionTest;

public class MutagenicityKNNTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismMutagenicityKnn();
    }
}
