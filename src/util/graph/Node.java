package util.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * A node in a graph
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class Node {
    /**
     * The list of edges
     */
    private List edges;
    
    /**
     * Generic label stored on the node
     */
    private int label;
    
    /**
     * Make a new node
     */
    public Node() {
        edges = new ArrayList();
    }
    
    /**
     * Make a new node
     * @param data the data
     */
    public Node(int label) {
        this();
        this.label = label;
    }
    
    /**
     * Add a edge
     * @param e the edge to add
     */
    public void addEdge(Edge e) {
        edges.add(e);
    }
    
    /**
     * Get the edge count
     * @return the edge count
     */
    public int getEdgeCount() {
        return edges.size();
    }
    
    /**
     * Remove an edge
     * @param i the edge to remove
     */
    public void removeEdge(int i) {
        edges.remove(i);
    }
    
    /**
     * Remove an edge
     * @param edge the edge to remove
     */
    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }
    
    /**
     * Get an edge
     * @param i the edge to get
     * @return the edge
     */
    public Edge getEdge(int i) {
        return (Edge) edges.get(i);
    }
    
    
    /**
     * Connect to another node
     * @param other the other node
     * @param link the link
     */
    public void connect(Node other, Edge link) {
        link.setA(this);
        link.setB(other);
        edges.add(link);
        other.addEdge(link);
    }
    
    /**
     * Connect to another node
     * @param other the other node
     * @param link the link
     */
    public void connectDirected(Node other, Edge link) {
        link.setA(this);
        link.setB(other);
        edges.add(link);
    }

    /**
     * Get the generic label
     * @return the label
     */
    public int getLabel() {
        return label;
    }

    /**
     * Set the label
     * @param label the label
     */
    public void setLabel(int label) {
        this.label = label;
    }

    /**
     * Get the list of edges
     * @return the edges
     */
    public List getEdges() {
        return edges;
    }

    /**
     * Set the list of edges
     * @param list the list of edges
     */
    public void setEdges(List list) {
        edges = list;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return label + " : " + edges;
    }

}
