package insilico.devtox_caesar.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * An ensemble of binary decision trees over numeric attributes, read from a text resource.
 * <p>
 * The forest was trained once with WEKA 3.5.8 ({@code RandomForest -I 10 -K 0 -S 1 -depth 12}) and stored as a
 * resource, so that running the model no longer needs WEKA, nor the training that used to be repeated on every
 * instantiation. Both the storage format and the prediction reproduce WEKA's behaviour exactly:
 * <ul>
 *     <li>each tree returns the class distribution of the node it walks down to, as stored, without
 *         normalizing it again (see {@link ForestNode#distribution(double[])});</li>
 *     <li>the distributions of the trees are summed and then normalized, unless their sum is zero up to
 *         WEKA's own tolerance of 1e-6;</li>
 *     <li>the predicted class is the first index holding the maximum, so a tie goes to the first class, and
 *         no class at all is predicted when the distribution carries no positive mass.</li>
 * </ul>
 * The forest is stored one node per line, each tree in preorder: a {@code split} line is followed by its left
 * subtree and then by its right subtree. See {@code /data/forest_devtox_caesar.txt}.
 */
public final class DecisionForest {

    /** WEKA's {@code Utils.SMALL}, the tolerance of the zero-sum check performed before normalizing. */
    private static final double SMALL = 1e-6;

    /** Marker used in the resource for a node that carries no class distribution. */
    private static final String NO_DISTRIBUTION = "-";

    /** Returned by {@link #classify(double[])} when the distribution carries no positive mass. */
    public static final int NO_CLASS = -1;

    private final String[] attributeNames;
    private final String[] classLabels;
    private final ForestNode[] trees;

    private DecisionForest(String[] attributeNames, String[] classLabels, ForestNode[] trees) {
        this.attributeNames = attributeNames;
        this.classLabels = classLabels;
        this.trees = trees;
    }

    /**
     * Reads a forest from its text representation.
     *
     * @param source the resource to read; it is closed by this method
     * @return the forest
     * @throws IOException if the resource cannot be read or is malformed
     */
    public static DecisionForest load(InputStream source) throws IOException {

        List<String[]> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(source, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("#"))
                    lines.add(trimmed.split("\\s+"));
            }
        }

        Cursor cursor = new Cursor(lines);

        String[] attributeNames = cursor.readHeader("attributes");
        String[] classLabels = cursor.readHeader("classes");

        String[] treeCount = cursor.readHeader("trees");
        if (treeCount.length != 1)
            throw new IOException("Malformed forest: expected a single tree count, found " + treeCount.length);

        ForestNode[] trees = new ForestNode[Integer.parseInt(treeCount[0])];
        for (int i = 0; i < trees.length; i++) {
            cursor.expect("tree");
            trees[i] = readNode(cursor, classLabels.length);
        }

        if (!cursor.atEnd())
            throw new IOException("Malformed forest: unexpected content after the last tree");

        return new DecisionForest(attributeNames, classLabels, trees);
    }

    /**
     * Reads one node and, for a split node, its two subtrees.
     */
    private static ForestNode readNode(Cursor cursor, int numClasses) throws IOException {

        String[] tokens = cursor.next();

        if ("leaf".equals(tokens[0]))
            return new ForestNode(readDistribution(tokens, 1, numClasses));

        if (!"split".equals(tokens[0]))
            throw new IOException("Malformed forest: expected a node, found '" + tokens[0] + "'");

        int attribute = Integer.parseInt(tokens[1]);
        double splitPoint = Double.parseDouble(tokens[2]);
        double[] classProbs = readDistribution(tokens, 3, numClasses);

        ForestNode left = readNode(cursor, numClasses);
        ForestNode right = readNode(cursor, numClasses);

        return new ForestNode(attribute, splitPoint, left, right, classProbs);
    }

    /**
     * Reads a class distribution from {@code from} on, or {@code null} if the node has none.
     */
    private static double[] readDistribution(String[] tokens, int from, int numClasses) throws IOException {

        if (tokens.length == from + 1 && NO_DISTRIBUTION.equals(tokens[from]))
            return null;

        if (tokens.length != from + numClasses)
            throw new IOException("Malformed forest: expected " + numClasses + " class values in '"
                    + String.join(" ", tokens) + "'");

        double[] distribution = new double[numClasses];
        for (int i = 0; i < numClasses; i++)
            distribution[i] = Double.parseDouble(tokens[from + i]);

        return distribution;
    }

    /**
     * Combines the trees into a single class distribution.
     *
     * @param values the attribute values, in the order of {@link #getAttributeNames()}
     * @return the class distribution, normalized unless its mass is zero
     */
    public double[] distribution(double[] values) {

        if (values.length != attributeNames.length)
            throw new IllegalArgumentException("Expected " + attributeNames.length + " attribute values, got "
                    + values.length);

        for (double value : values)
            if (!Double.isFinite(value))
                throw new IllegalArgumentException("Attribute values must be finite, got " + value);

        double[] sums = new double[classLabels.length];
        for (ForestNode tree : trees) {
            double[] probs = tree.distribution(values);
            if (probs == null)
                throw new IllegalStateException("A tree of the forest returned no class distribution");
            for (int i = 0; i < probs.length; i++)
                sums[i] += probs[i];
        }

        double sum = 0.0;
        for (double value : sums)
            sum += value;

        // WEKA leaves an all-zero distribution untouched rather than dividing by zero
        if (sum - 0.0 < SMALL && 0.0 - sum < SMALL)
            return sums;

        for (int i = 0; i < sums.length; i++)
            sums[i] /= sum;

        return sums;
    }

    /**
     * Predicts the class of one instance.
     *
     * @param values the attribute values, in the order of {@link #getAttributeNames()}
     * @return the index of the predicted class, or {@link #NO_CLASS} when no class has positive mass
     */
    public int classify(double[] values) {

        double[] distribution = distribution(values);

        double max = 0.0;
        int maxIndex = 0;
        for (int i = 0; i < distribution.length; i++) {
            // The comparison is strict, so a tie is won by the first class
            if (distribution[i] > max) {
                maxIndex = i;
                max = distribution[i];
            }
        }

        return max > 0.0 ? maxIndex : NO_CLASS;
    }

    /** @return the attribute names, in the order expected by {@link #distribution(double[])} */
    public String[] getAttributeNames() {
        return attributeNames.clone();
    }

    /** @return the class labels, in the order of the predicted distribution */
    public String[] getClassLabels() {
        return classLabels.clone();
    }

    /** @return the number of trees in the forest */
    public int getTreesSize() {
        return trees.length;
    }


    /**
     * Walks the lines of the resource.
     */
    private static final class Cursor {

        private final List<String[]> lines;
        private int position;

        Cursor(List<String[]> lines) {
            this.lines = lines;
        }

        boolean atEnd() {
            return position >= lines.size();
        }

        String[] next() throws IOException {
            if (atEnd())
                throw new IOException("Malformed forest: unexpected end of resource");
            return lines.get(position++);
        }

        void expect(String keyword) throws IOException {
            String[] tokens = next();
            if (tokens.length != 1 || !keyword.equals(tokens[0]))
                throw new IOException("Malformed forest: expected '" + keyword + "', found '"
                        + String.join(" ", tokens) + "'");
        }

        /**
         * Reads a {@code <keyword> <value>...} line and returns its values.
         */
        String[] readHeader(String keyword) throws IOException {
            String[] tokens = next();
            if (tokens.length < 2 || !keyword.equals(tokens[0]))
                throw new IOException("Malformed forest: expected the '" + keyword + "' header, found '"
                        + String.join(" ", tokens) + "'");
            String[] values = new String[tokens.length - 1];
            System.arraycopy(tokens, 1, values, 0, values.length);
            return values;
        }

    }

}
