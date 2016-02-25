package abagail.util.graph;

/**
 * Represents a directed graph with a root.
 *
 * @author Andrew Guillory - gtg008g@mail.gatech.edu
 */
public class Tree extends Graph {
    /**
     * The root node.
     */
    private Node root;

    /**
     * Creates a tree (no root set).
     */
    public Tree() {
    }

    /**
     * Creates a tree with the given root.
     *
     * @param root the root
     */
    public Tree(Node root) {
        this.root = root;
    }

    /**
     * Returns the root of this tree.
     *
     * @return the root
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Sets the root of this tree.
     *
     * @param node the root
     */
    public void setRoot(Node node) {
        root = node;
    }

}
