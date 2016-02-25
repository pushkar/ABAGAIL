package abagail.util.graph;


import java.util.ArrayList;
import java.util.List;

/**
 * A node in a graph.
 *
 * @author Andrew Guillory - gtg008g@mail.gatech.edu
 */
public class Node {
    /**
     * The list of edges.
     */
    private List<Edge> edges = new ArrayList<>();

    /**
     * Generic label stored on the node.
     */
    private int label;

    /**
     * Creates a new node, with a label of {@code 0}.
     */
    public Node() {
    }

    /**
     * Creates a new node with the given label.
     *
     * @param label the label
     */
    public Node(int label) {
        this.label = label;
    }

    /**
     * Add an edge.
     *
     * @param e the edge to add
     */
    public void addEdge(Edge e) {
        edges.add(e);
    }

    /**
     * Get the edge count.
     *
     * @return the edge count
     */
    public int getEdgeCount() {
        return edges.size();
    }

    /**
     * Remove an edge by supplying its index.
     *
     * @param i the index of the edge to remove
     */
    public void removeEdge(int i) {
        edges.remove(i);
    }

    /**
     * Remove an edge.
     *
     * @param edge the edge to remove
     */
    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    /**
     * Returns the edge at the given index.
     *
     * @param i the index of the edge to return
     * @return the edge
     */
    public Edge getEdge(int i) {
        return edges.get(i);
    }

    /**
     * Connect this node to another node via the given edge.
     * Note that the edge is added to both this and the other node.
     *
     * @param other the other node
     * @param link  the link
     */
    public void connect(Node other, Edge link) {
        link.setA(this);
        link.setB(other);
        this.addEdge(link);
        other.addEdge(link);
    }

    /**
     * Connect this node to another node via the given link.
     * Note that the edge is added to just this node (not the other node).
     *
     * @param other the other node
     * @param link  the link
     */
    public void connectDirected(Node other, Edge link) {
        link.setA(this);
        link.setB(other);
        this.addEdge(link);
    }

    /**
     * Returns the label for this node.
     *
     * @return the label
     */
    public int getLabel() {
        return label;
    }

    /**
     * Set the label for this node.
     *
     * @param label the label
     */
    public void setLabel(int label) {
        this.label = label;
    }

    /**
     * Returns the edges for this node.
     *
     * @return the edges
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Sets the edges for this node.
     *
     * @param list the list of edges
     */
    public void setEdges(List<Edge> list) {
        edges = list;
    }

    @Override
    public String toString() {
        return label + " : " + edges;
    }

}
