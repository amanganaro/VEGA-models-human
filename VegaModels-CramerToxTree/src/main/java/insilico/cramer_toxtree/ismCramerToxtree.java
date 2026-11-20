package insilico.cramer_toxtree;

import insilico.core.descriptor.DescriptorsEngine;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.core.model.InsilicoModelOutput;
import insilico.core.tools.utils.ModelUtilities;
import insilico.toxtree.core.Classification;
import insilico.toxtree.cramer.CramerTree;

/**
 * Cramer classification of presumed toxic hazard.
 * <p>
 * The rules live in the ToxTreeRules library and work on the molecule this model has already normalized, so the
 * model only runs the tree and reports what it says.
 *
 * @author Alberto Manganaro (a.manganaro@kode-solutions.net)
 */
public class ismCramerToxtree extends InsilicoModel {

    private static final long serialVersionUID = 1L;

    private static final String ModelData = "/data/model_cramer_toxtree.xml";

    private final CramerTree tree;

    public ismCramerToxtree() throws InitFailureException {
        super(ModelData);

        tree = new CramerTree();

        // Define no. of descriptors
        this.DescriptorsSize = 0;
        this.DescriptorsNames = new String[DescriptorsSize];

        // Defines results
        this.ResultsSize = 1;
        this.ResultsName = new String[ResultsSize];
        this.ResultsName[0] = "Predicted Cramer class";

        // Define AD items
        this.ADItemsName = new String[0];

    }


    @Override
    protected short CalculateDescriptors(DescriptorsEngine DescEngine) {

        try {

            Descriptors = new double[DescriptorsSize];

        } catch (Throwable e) {
            return DESCRIPTORS_ERROR;
        }

        return DESCRIPTORS_CALCULATED;
    }


    @Override
    protected short CalculateModel() {

        int MainResult = -1;
        String MainResultText = "N.A.";

        try {

            Classification result = tree.classify(this.CurMolecule.GetStructure());
            if (result.isClassified()) {
                MainResult = result.categoryIndex();
                MainResultText = result.categoryName();
            }

        } catch (Exception e) {
            //
        }

        CurOutput.setMainResultValue(MainResult);

        String[] Res = new String[ResultsSize];
        Res[0] = MainResultText;

        CurOutput.setResults(Res);

        return MODEL_CALCULATED;
    }


    @Override
    protected short CalculateAD() {

        return InsilicoModel.AD_ERROR;

    }


    @Override
    protected void CalculateAssessment() {

        // Sets assessment message
        ModelUtilities.SetDefaultAssessment(CurOutput, CurOutput.getResults()[0]);

        // Sets assessment status
        CurOutput.setAssessmentStatus(InsilicoModelOutput.ASSESS_GRAY);

    }

}
