package util.graph;

/**
 * A tree is a directed graph with a root
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class Tree extends Graph {
    
    /**
     * The root node
     */
    private Node root;
    
    /**
     * Make a rooted graph
     */
    public Tree() {
    }
    
    /**
     * Make a new tree
     * @param root the root
     */
    public Tree(Node root) {
        this.root = root;
    }

    /**
     * Get the root
     * @return the root
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Set the root 
     * @param node the root
     */
    public void setRoot(Node node) {
        root = node;
    }

}
