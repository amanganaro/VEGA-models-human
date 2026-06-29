import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.eye_irritation_knn.ismEyeIrritationKnn;
import model.ModelExecutionTest;

public class EyeIrritationKNNTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismEyeIrritationKnn();
    }
}
