package util.graph;

/**
 * An edge
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class Edge {
    /**
     * The in node
     */
    private Node a;
    /**
     * The out node
     */
    private Node b;

    /**
     * Get the in node
     * @return the in node
     */
    public Node getA() {
        return a;
    }

    /**
     * Get the out node
     * @return the out node
     */
    public Node getB() {
        return b;
    }
    
    /**
     * Get the other node
     * @param n the node
     * @return the other node
     */
    public Node getOther(Node n) {
        if (n == a) {
            return b;
        } else {
            return a;
        }
    }

    /**
     * Set the in node
     * @param node the in node
     */
    public void setA(Node node) {
        a = node;
    }

    /**
     * Set the out node
     * @param node the out node
     */
    public void setB(Node node) {
        b = node;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
       return a.getLabel() + " -> " + b.getLabel(); 
    }

}
