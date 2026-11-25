package insilico.devtox_caesar.model;

/**
 * A node of a binary decision tree working on numeric attributes.
 * <p>
 * A node is either a split node, which compares one attribute against a threshold and delegates to one of its
 * two children, or a leaf. Both kinds of node carry a class distribution: a split node falls back on its own
 * distribution when the selected child has none, which reproduces the behaviour of the original model (see
 * {@link DecisionForest}).
 */
final class ForestNode {

    /** Attribute index this node splits on, or {@link #LEAF} when the node is a leaf. */
    static final int LEAF = -1;

    private final int attribute;
    private final double splitPoint;
    private final ForestNode left;
    private final ForestNode right;
    private final double[] classProbs;

    /**
     * Creates a split node.
     *
     * @param attribute  index of the attribute to compare
     * @param splitPoint threshold: values strictly below it go to {@code left}, the others to {@code right}
     * @param left       child for values below the threshold
     * @param right      child for values at or above the threshold
     * @param classProbs class distribution of this node, used as a fallback; may be {@code null}
     */
    ForestNode(int attribute, double splitPoint, ForestNode left, ForestNode right, double[] classProbs) {
        this.attribute = attribute;
        this.splitPoint = splitPoint;
        this.left = left;
        this.right = right;
        this.classProbs = classProbs;
    }

    /**
     * Creates a leaf.
     *
     * @param classProbs class distribution of the leaf; may be {@code null}
     */
    ForestNode(double[] classProbs) {
        this(LEAF, 0.0, null, null, classProbs);
    }

    boolean isLeaf() {
        return attribute == LEAF;
    }

    int getAttribute() {
        return attribute;
    }

    double getSplitPoint() {
        return splitPoint;
    }

    ForestNode getLeft() {
        return left;
    }

    ForestNode getRight() {
        return right;
    }

    double[] getClassProbs() {
        return classProbs;
    }

    /**
     * Walks the tree down to a leaf and returns the class distribution found there.
     *
     * @param values the attribute values, in the order declared by the forest
     * @return the class distribution, or {@code null} when neither the reached node nor this one has one
     */
    double[] distribution(double[] values) {

        double[] result = null;

        if (attribute != LEAF)
            result = (values[attribute] < splitPoint ? left : right).distribution(values);

        // A split node whose subtree has no distribution falls back on its own
        return result != null ? result : classProbs;
    }

}
