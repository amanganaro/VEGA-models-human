package insilico.devtox_caesar;

import insilico.core.ad.ADCheckACF;
import insilico.core.ad.ADCheckDescriptorRange;
import insilico.core.ad.ADCheckIndicesQualitative;
import insilico.core.ad.item.*;
import insilico.core.descriptor.DescriptorsEngine;
import insilico.core.exception.InitFailureException;
import insilico.core.model.InsilicoModel;
import insilico.core.model.InsilicoModelOutput;

import insilico.core.tools.utils.ModelUtilities;
import insilico.devtox_caesar.descriptors.EmbeddedDescriptors;
import insilico.devtox_caesar.descriptors.IcycemDescriptor;
import insilico.devtox_caesar.model.DevtoxCaesarForest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Alberto Manganaro (a.manganaro@kode-solutions.net)
 */

public class ismDevtoxCaesar extends InsilicoModel {
    private static final Logger log = LogManager.getLogger(ismDevtoxCaesar.class);

    private static final long serialVersionUID = 1L;

    private static final String ModelData = "/data/model_devtox_caesar.xml";

    private final DevtoxCaesarForest RandomForest;


    public ismDevtoxCaesar()
            throws InitFailureException {
        super(ModelData);

        // Define no. of descriptors
        this.DescriptorsSize = 13;
        this.DescriptorsNames = new String[DescriptorsSize];
        this.DescriptorsNames[0] = "icycem";
        this.DescriptorsNames[1] = "BEHm1";
        this.DescriptorsNames[2] = "BELp3";
        this.DescriptorsNames[3] = "BELv1";
        this.DescriptorsNames[4] = "BELv8";
        this.DescriptorsNames[5] = "GATS1p";
        this.DescriptorsNames[6] = "GATS2m";
        this.DescriptorsNames[7] = "GATS3v";
        this.DescriptorsNames[8] = "MATS1p";
        this.DescriptorsNames[9] = "MATS4p";
        this.DescriptorsNames[10] = "MATS4v";
        this.DescriptorsNames[11] = "SdssC";
        this.DescriptorsNames[12] = "SHssNH";

        // Builds model objects: the forest matches the descriptors by name, so it must be built once they
        // are declared
        RandomForest = new DevtoxCaesarForest(this.DescriptorsNames);

        // Defines results
        this.ResultsSize = 1;
        this.ResultsName = new String[ResultsSize];
        this.ResultsName[0] = "Predicted developmental toxicity activity";

        // Define AD items
        this.ADItemsName = new String[5];
        this.ADItemsName[0] = new ADIndexSimilarity().GetIndexName();
        this.ADItemsName[1] = new ADIndexAccuracy().GetIndexName();
        this.ADItemsName[2] = new ADIndexConcordance().GetIndexName();
        this.ADItemsName[3] = new ADIndexRange().GetIndexName();
        this.ADItemsName[4] = new ADIndexACF().GetIndexName();

    }


    @Override
    protected short CalculateDescriptors(DescriptorsEngine DescEngine) {

        try {

            Descriptors = new double[DescriptorsSize];

            EmbeddedDescriptors embeddedDescriptors = new EmbeddedDescriptors(CurMolecule);

            // icycem is calculated with a custom class
            Descriptors[0] = IcycemDescriptor.Calculate(CurMolecule);

            Descriptors[1] = embeddedDescriptors.getBEH1m();

            // Burden low eigenvalue descriptors (BEL) are different from
            // the descriptors calculated in the original custom code,
            // they just have opposite sign
            Descriptors[2] = (-1) * embeddedDescriptors.getBEL3p();
            Descriptors[3] = (-1) * embeddedDescriptors.getBEL1v();
            Descriptors[4] = (-1) * embeddedDescriptors.getBEL8v();

            Descriptors[5] = embeddedDescriptors.getGATS1p();
            Descriptors[6] = embeddedDescriptors.getGATS2m();
            Descriptors[7] = embeddedDescriptors.getGATS3v();
            Descriptors[8] = embeddedDescriptors.getMATS1p();
            Descriptors[9] = embeddedDescriptors.getMATS4p();
            Descriptors[10] = embeddedDescriptors.getMATS4v();
            Descriptors[11] = embeddedDescriptors.getSdssC();
            Descriptors[12] = embeddedDescriptors.getSHssNH();

        } catch (Throwable e) {
            return DESCRIPTORS_ERROR;
        }

        return DESCRIPTORS_CALCULATED;
    }



    @Override
    protected short CalculateModel() {

        int MainResult;

        try {

            // The forest always answers: no undefined class can be returned
            MainResult = RandomForest.predictToxic(Descriptors) ? 1 : 0;

        } catch (Throwable e) {
            // Only reached by descriptors the forest cannot walk on, which Execute already rules out
            log.warn("Unable to run the forest of the model: " + e.getMessage());
            return MODEL_ERROR;
        }

        CurOutput.setMainResultValue(MainResult);

        String[] Res = new String[ResultsSize];
        try {
            Res[0] = this.GetTrainingSet().getClassLabel(MainResult); // devtox classification
        } catch (Throwable ex) {
            log.warn("Unable to find label for toxicity value " + MainResult);
            Res[0] = Integer.toString(MainResult);
        }

        CurOutput.setResults(Res);

        return MODEL_CALCULATED;
    }


    @Override
    protected short CalculateAD() {

        // Calculates various AD indices
        ADCheckIndicesQualitative adq = new ADCheckIndicesQualitative(TS);
        adq.AddMappingToPositiveValue(1);
        adq.AddMappingToNegativeValue(0);
        adq.setMoleculesForIndexSize(2);
        if (!adq.Calculate(CurMolecule, CurOutput))
            return InsilicoModel.AD_ERROR;

        // Sets threshold for AD indices
        try {
            ((ADIndexSimilarity)CurOutput.getADIndex(ADIndexSimilarity.class)).SetThresholds(0.8, 0.7);
            ((ADIndexAccuracy)CurOutput.getADIndex(ADIndexAccuracy.class)).SetThresholds(0.8, 0.6);
            ((ADIndexConcordance)CurOutput.getADIndex(ADIndexConcordance.class)).SetThresholds(0.8, 0.6);
        } catch (Throwable e) {
            return InsilicoModel.AD_ERROR;
        }

        // Sets Range check
        ADCheckDescriptorRange adrc = new ADCheckDescriptorRange();
        if (!adrc.Calculate(TS, Descriptors, CurOutput))
            return InsilicoModel.AD_ERROR;

        // Sets ACF check
        ADCheckACF adacf = new ADCheckACF(TS);
        if (!adacf.Calculate(CurMolecule, CurOutput))
            return InsilicoModel.AD_ERROR;

        // Sets final AD index
        double acfContribution = CurOutput.getADIndex(ADIndexACF.class).GetIndexValue();
        double rcContribution = CurOutput.getADIndex(ADIndexRange.class).GetIndexValue();
        double ADIValue = adq.getIndexADI() * acfContribution * rcContribution;

        ADIndexADI ADI = new ADIndexADI();
        ADI.SetIndexValue(ADIValue);
        ADI.SetThresholds(0.8, 0.7);
        CurOutput.setADI(ADI);

        return InsilicoModel.AD_CALCULATED;
    }


    @Override
    protected void CalculateAssessment() {

        // Sets assessment message
        ModelUtilities.SetDefaultAssessment(CurOutput, CurOutput.getResults()[0]);

        // Sets assessment status
        double Val = CurOutput.HasExperimental() ? CurOutput.getExperimental() : CurOutput.getMainResultValue();
        if (Val == 0)
            CurOutput.setAssessmentStatus(InsilicoModelOutput.ASSESS_GREEN);
        else if (Val == 1)
            CurOutput.setAssessmentStatus(InsilicoModelOutput.ASSESS_RED);
        else
            CurOutput.setAssessmentStatus(InsilicoModelOutput.ASSESS_YELLOW);

    }

}
