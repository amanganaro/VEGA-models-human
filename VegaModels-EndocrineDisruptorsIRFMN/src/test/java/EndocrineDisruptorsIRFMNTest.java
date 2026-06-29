import insilico.core.exception.GenericFailureException;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.endocrine_disruptors_irfmn.ismEndocrineDisruptorsIRFMN;
import model.ModelExecutionTest;

public class EndocrineDisruptorsIRFMNTest extends ModelExecutionTest {
    @Override
    protected InsilicoModel getModel() throws InitFailureException, GenericFailureException {
        return new ismEndocrineDisruptorsIRFMN();
    }
}
