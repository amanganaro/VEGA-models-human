package insilico.devtox_caesar.model;

import insilico.core.exception.InitFailureException;

import java.io.IOException;
import java.io.InputStream;

/**
 * The random forest of the Developmental Toxicity (CAESAR) model.
 * <p>
 * It wraps the {@link DecisionForest} stored in the resources and adapts it to the model: it maps the
 * descriptors from the order used by the model to the order of the forest attributes, and turns the predicted
 * class into the toxic/non toxic answer the model works with.
 * <p>
 * Until version 2.1.9 the forest was not stored at all: WEKA 3.5.8 trained it again from
 * {@code /data/sel18_EPA_training.arff} on every instantiation of the model. The forest is now extracted once
 * and frozen in {@value #FOREST_RESOURCE}, which gives the very same predictions without WEKA and without
 * depending on the floating point library of the JVM that happens to run the training.
 */
public final class DevtoxCaesarForest {

    /** The frozen forest. */
    public static final String FOREST_RESOURCE = "/data/forest_devtox_caesar.txt";

    /** Label of the toxic class in the forest. */
    private static final String TOXIC_CLASS = "D";

    private final DecisionForest forest;

    /** For each forest attribute, the index of the descriptor holding its value. */
    private final int[] descriptorOfAttribute;

    private final int toxicClass;

    /**
     * Loads the forest and binds it to the descriptors of the model.
     *
     * @param descriptorsNames the descriptor names, in the order used by the model
     * @throws InitFailureException if the forest cannot be read or does not match the given descriptors
     */
    public DevtoxCaesarForest(String[] descriptorsNames) throws InitFailureException {

        try (InputStream source = getClass().getResourceAsStream(FOREST_RESOURCE)) {

            if (source == null)
                throw new IOException("Resource not found: " + FOREST_RESOURCE);

            forest = DecisionForest.load(source);

        } catch (IOException e) {
            throw new InitFailureException("Unable to read the forest of the model: " + e.getMessage());
        }

        descriptorOfAttribute = mapAttributesToDescriptors(forest.getAttributeNames(), descriptorsNames);
        toxicClass = indexOf(forest.getClassLabels(), TOXIC_CLASS);

        if (toxicClass < 0)
            throw new InitFailureException("The forest of the model has no '" + TOXIC_CLASS + "' class");
    }

    /**
     * Predicts the developmental toxicity of one molecule.
     *
     * @param descriptors the descriptor values, in the order used by the model
     * @return {@code true} if the molecule is predicted to be toxic
     */
    public boolean predictToxic(double[] descriptors) {

        double[] values = new double[descriptorOfAttribute.length];
        for (int i = 0; i < values.length; i++)
            values[i] = descriptors[descriptorOfAttribute[i]];

        return forest.classify(values) == toxicClass;
    }

    /**
     * Matches the attributes of the forest with the descriptors of the model by name: the two use a different
     * order, which used to be hard coded in the ARFF file the model wrote for WEKA.
     *
     * @throws InitFailureException if an attribute has no descriptor of the same name
     */
    private static int[] mapAttributesToDescriptors(String[] attributeNames, String[] descriptorsNames)
            throws InitFailureException {

        int[] map = new int[attributeNames.length];

        for (int i = 0; i < attributeNames.length; i++) {
            map[i] = indexOf(descriptorsNames, attributeNames[i]);
            if (map[i] < 0)
                throw new InitFailureException("The model has no descriptor named " + attributeNames[i]
                        + ", required by the forest");
        }

        return map;
    }

    private static int indexOf(String[] values, String value) {
        for (int i = 0; i < values.length; i++)
            if (values[i].equals(value))
                return i;
        return -1;
    }

}
