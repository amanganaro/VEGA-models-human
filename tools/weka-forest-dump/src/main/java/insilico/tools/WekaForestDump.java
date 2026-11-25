package insilico.tools;

import insilico.devtox_caesar.model.DecisionForest;

import weka.classifiers.Classifier;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Extracts the trained random forest of the Developmental Toxicity (CAESAR) model out of WEKA 3.5.8 and checks
 * that {@link DecisionForest} reproduces WEKA's predictions exactly.
 * <p>
 * Until version 2.1.9 the model carried no trained forest: it trained one with WEKA from
 * {@code /data/sel18_EPA_training.arff} on every instantiation. This tool trains it one last time, with the
 * very same options, writes it to a text resource and then compares the two implementations on the whole
 * training set and on a large number of generated instances, so that the resource can replace WEKA for good.
 * <p>
 * Usage: {@code WekaForestDump <output file> [number of random instances]}. The tool fails, loudly, as soon as
 * an assumption about the trained model or a prediction does not hold.
 *
 * @see <a href="README.md">README.md</a>
 */
public final class WekaForestDump {

    /** The training set, read from the resources of the model. */
    private static final String TRAINING_SET = "/data/sel18_EPA_training.arff";

    /** The options the model used to train the forest. */
    private static final String[] OPTIONS = {"-I", "10", "-K", "0", "-S", "1", "-depth", "12"};

    /** Marker for a node carrying no class distribution, as read by {@link DecisionForest}. */
    private static final String NO_DISTRIBUTION = "-";

    private static final int DEFAULT_RANDOM_INSTANCES = 200_000;

    /** Seed of the generated instances, fixed so that a run can be repeated. */
    private static final long PROBE_SEED = 20261125L;


    public static void main(String[] args) throws Exception {

        if (args.length < 1 || args.length > 2) {
            System.err.println("Usage: WekaForestDump <output file> [number of random instances]");
            System.exit(2);
        }

        Path output = Paths.get(args[0]);
        int randomInstances = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_RANDOM_INSTANCES;

        byte[] arff = readResource(TRAINING_SET);
        Instances trainingSet = readTrainingSet(arff);

        System.out.println("Training set: " + TRAINING_SET + " (" + trainingSet.numInstances() + " instances, "
                + trainingSet.numAttributes() + " attributes, sha-256 " + sha256(arff) + ")");

        RandomForest weka = new RandomForest();
        weka.setOptions(OPTIONS.clone());
        weka.buildClassifier(trainingSet);
        System.out.println("Trained with WEKA, options " + String.join(" ", OPTIONS));

        List<String> dump = dump(weka, trainingSet, arff);
        Files.write(output, String.join("\n", dump).concat("\n").getBytes(StandardCharsets.UTF_8));
        System.out.println("Written " + output.toAbsolutePath() + " (" + dump.size() + " lines)");

        DecisionForest forest;
        try (InputStream source = Files.newInputStream(output)) {
            forest = DecisionForest.load(source);
        }
        System.out.println("Read back " + forest.getTreesSize() + " trees over "
                + forest.getAttributeNames().length + " attributes, classes "
                + Arrays.toString(forest.getClassLabels()));

        compare(weka, forest, trainingSet, output, randomInstances);

        System.out.println("OK: the extracted forest reproduces WEKA exactly");
    }


    // ------------------------------------------------------------------------------------------------------
    // Extraction
    // ------------------------------------------------------------------------------------------------------

    /**
     * Writes the trained forest as the text representation read by {@link DecisionForest}.
     */
    private static List<String> dump(RandomForest weka, Instances trainingSet, byte[] arff) throws Exception {

        Classifier[] trees = trees(weka);

        List<String> lines = new ArrayList<>();
        lines.add("# Random forest of the Developmental Toxicity (CAESAR) model.");
        lines.add("#");
        lines.add("# Extracted from WEKA 3.5.8 by tools/weka-forest-dump, which trained it with");
        lines.add("#     RandomForest " + String.join(" ", OPTIONS));
        lines.add("# on " + TRAINING_SET + " (sha-256 " + sha256(arff) + "),");
        lines.add("# exactly as the model itself did, on every instantiation, up to version 2.1.9.");
        lines.add("#");
        lines.add("# One node per line, each tree in preorder: a split line is followed by its left subtree");
        lines.add("# and then by its right subtree. A split sends the values below its threshold to the left.");
        lines.add("# Every node carries the class distribution WEKA stored in it; '" + NO_DISTRIBUTION
                + "' means it has none.");
        lines.add("#");
        lines.add("# DO NOT EDIT: this file is the model.");

        StringBuilder attributes = new StringBuilder("attributes");
        for (int i = 0; i < trainingSet.classIndex(); i++)
            attributes.append(' ').append(trainingSet.attribute(i).name());
        lines.add(attributes.toString());

        StringBuilder classes = new StringBuilder("classes");
        for (int i = 0; i < trainingSet.classAttribute().numValues(); i++)
            classes.append(' ').append(trainingSet.classAttribute().value(i));
        lines.add(classes.toString());

        lines.add("trees " + trees.length);

        for (Classifier tree : trees) {
            lines.add("tree");
            dumpNode(tree, trainingSet, lines);
        }

        return lines;
    }

    /**
     * Writes one node and, for a split node, its two subtrees, checking the assumptions the extraction rests
     * on: no ZeroR fallback, numeric attributes only and binary splits.
     */
    private static void dumpNode(Object node, Instances trainingSet, List<String> lines) throws Exception {

        if (field(node, "m_ZeroR") != null)
            throw new IllegalStateException("A node of the forest falls back on ZeroR, which is not supported");

        int attribute = (Integer) field(node, "m_Attribute");
        double[] classProbs = (double[]) field(node, "m_ClassProbs");

        if (attribute < 0) {
            lines.add("leaf " + distribution(classProbs));
            return;
        }

        Attribute split = trainingSet.attribute(attribute);
        if (!split.isNumeric())
            throw new IllegalStateException("The forest splits on the non numeric attribute " + split.name());

        Object[] successors = (Object[]) field(node, "m_Successors");
        if (successors == null || successors.length != 2)
            throw new IllegalStateException("The forest has a split with "
                    + (successors == null ? "no" : String.valueOf(successors.length)) + " successors");

        double splitPoint = (Double) field(node, "m_SplitPoint");
        lines.add("split " + attribute + " " + Double.toString(splitPoint) + " " + distribution(classProbs));

        dumpNode(successors[0], trainingSet, lines);
        dumpNode(successors[1], trainingSet, lines);
    }

    /**
     * Formats a class distribution; {@link Double#toString(double)} reads back exactly.
     */
    private static String distribution(double[] classProbs) {

        if (classProbs == null)
            return NO_DISTRIBUTION;

        StringBuilder values = new StringBuilder();
        for (double value : classProbs) {
            if (values.length() > 0)
                values.append(' ');
            values.append(Double.toString(value));
        }

        return values.toString();
    }

    /**
     * Returns the trees of the forest: in WEKA 3.5.8 a {@code RandomForest} is a {@code Bagging} of
     * {@code RandomTree}, and a {@code RandomTree} is itself the root node of its tree.
     */
    private static Classifier[] trees(RandomForest weka) throws Exception {

        Bagging bagger = (Bagging) field(weka, "m_bagger");
        Classifier[] trees = (Classifier[]) field(bagger, "m_Classifiers");

        if (trees == null || trees.length == 0)
            throw new IllegalStateException("The forest holds no tree");

        return trees;
    }

    /**
     * Reads a field of WEKA's own classes, which are not meant to be read from outside.
     */
    private static Object field(Object target, String name) throws Exception {

        for (Class<?> type = target.getClass(); type != null; type = type.getSuperclass()) {
            try {
                Field field = type.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(target);
            } catch (NoSuchFieldException ignored) {
                // Declared further up the hierarchy
            }
        }

        throw new NoSuchFieldException(name + " on " + target.getClass().getName());
    }


    // ------------------------------------------------------------------------------------------------------
    // Verification
    // ------------------------------------------------------------------------------------------------------

    /**
     * Compares the two implementations on the training set and on generated instances, and fails on the first
     * difference. The generated instances deliberately include the split points themselves, the values right
     * around them and values outside the range of the training set, since that is where the two could part.
     */
    private static void compare(RandomForest weka, DecisionForest forest, Instances trainingSet, Path dump,
                                int randomInstances) throws Exception {

        int attributes = trainingSet.classIndex();
        Instances header = new Instances(trainingSet, 0);

        for (int i = 0; i < trainingSet.numInstances(); i++) {
            double[] values = new double[attributes];
            for (int j = 0; j < attributes; j++)
                values[j] = trainingSet.instance(i).value(j);
            compareOne(weka, forest, header, values, "training instance " + i);
        }
        System.out.println("Checked the " + trainingSet.numInstances() + " training instances");

        double[][] candidates = candidateValues(trainingSet, dump);
        Random random = new Random(PROBE_SEED);

        for (int i = 0; i < randomInstances; i++) {
            double[] values = new double[attributes];
            for (int j = 0; j < attributes; j++)
                values[j] = candidates[j][random.nextInt(candidates[j].length)];
            compareOne(weka, forest, header, values, "generated instance " + i + " " + Arrays.toString(values));
        }
        System.out.println("Checked " + randomInstances + " generated instances");
    }

    /**
     * Checks that the two implementations agree, bit for bit, on one instance.
     */
    private static void compareOne(RandomForest weka, DecisionForest forest, Instances header, double[] values,
                                   String what) throws Exception {

        double[] attValues = Arrays.copyOf(values, values.length + 1);
        attValues[values.length] = Instance.missingValue();
        Instance instance = new Instance(1.0, attValues);
        instance.setDataset(header);

        double[] expected = weka.distributionForInstance(instance);
        double[] actual = forest.distribution(values);

        for (int i = 0; i < expected.length; i++)
            if (Double.compare(expected[i], actual[i]) != 0)
                throw new IllegalStateException("Distributions differ on " + what + ": WEKA "
                        + Arrays.toString(expected) + ", forest " + Arrays.toString(actual));

        double expectedClass = weka.classifyInstance(instance);
        int actualClass = forest.classify(values);
        int wekaClass = Double.isNaN(expectedClass) ? DecisionForest.NO_CLASS : (int) expectedClass;

        if (wekaClass != actualClass)
            throw new IllegalStateException("Classes differ on " + what + ": WEKA " + expectedClass
                    + ", forest " + actualClass);
    }

    /**
     * The values each attribute is probed with: the ones seen in the training set, every threshold the forest
     * splits that attribute on, the values immediately around each threshold, and values outside the range.
     */
    private static double[][] candidateValues(Instances trainingSet, Path dump) throws IOException {

        int attributes = trainingSet.classIndex();
        List<TreeSet<Double>> values = new ArrayList<>();
        for (int i = 0; i < attributes; i++)
            values.add(new TreeSet<>());

        for (int i = 0; i < trainingSet.numInstances(); i++)
            for (int j = 0; j < attributes; j++)
                values.get(j).add(trainingSet.instance(i).value(j));

        // The thresholds are read back from the resource, so that they are probed exactly as stored
        for (String[] split : splits(dump)) {
            int attribute = Integer.parseInt(split[0]);
            double threshold = Double.parseDouble(split[1]);
            values.get(attribute).add(threshold);
            values.get(attribute).add(Math.nextDown(threshold));
            values.get(attribute).add(Math.nextUp(threshold));
        }

        double[][] candidates = new double[attributes][];
        for (int i = 0; i < attributes; i++) {
            TreeSet<Double> attributeValues = values.get(i);
            attributeValues.add(attributeValues.first() - 1000.0);
            attributeValues.add(attributeValues.last() + 1000.0);
            attributeValues.add(0.0);
            candidates[i] = attributeValues.stream().mapToDouble(Double::doubleValue).toArray();
        }

        return candidates;
    }

    /**
     * The split lines of the written resource, as {@code {attribute, threshold}}.
     */
    private static List<String[]> splits(Path dump) throws IOException {

        List<String[]> splits = new ArrayList<>();

        for (String line : Files.readAllLines(dump, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (trimmed.startsWith("split ")) {
                String[] tokens = trimmed.split("\\s+");
                splits.add(new String[]{tokens[1], tokens[2]});
            }
        }

        if (splits.isEmpty())
            throw new IOException("The written forest holds no split");

        return splits;
    }


    // ------------------------------------------------------------------------------------------------------
    // Resources
    // ------------------------------------------------------------------------------------------------------

    private static byte[] readResource(String resource) throws IOException {

        try (InputStream source = WekaForestDump.class.getResourceAsStream(resource)) {
            if (source == null)
                throw new IOException("Resource not found: " + resource);
            return source.readAllBytes();
        }
    }

    private static Instances readTrainingSet(byte[] arff) throws Exception {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(arff), StandardCharsets.UTF_8))) {
            Instances trainingSet = new Instances(reader);
            trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
            return trainingSet;
        }
    }

    private static String sha256(byte[] content) throws Exception {

        byte[] digest = MessageDigest.getInstance("SHA-256").digest(content);

        StringBuilder hex = new StringBuilder();
        for (byte b : digest)
            hex.append(String.format("%02x", b));

        return hex.toString();
    }

}
