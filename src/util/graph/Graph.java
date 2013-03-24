package util.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class repsenting a graph
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class Graph {
    /**
     * The list of nodes
     */
    private List nodes;
    
    /**
     * Make a new graph
     */
    public Graph() {
        nodes = new ArrayList();
    }
    
    /**
     * Add a node
     * @param n the node to add
     */
    public void addNode(Node n) {
        n.setLabel(getNodeCount());
        nodes.add(n);
    }
    
    /**
     * Get a node
     * @param i the node to get
     * @return the node
     */
    public Node getNode(int i) {
        return (Node) nodes.get(i);
    }
    
    /**
     * Get the number of nodes in the graph
     * @return the number of nodes
     */
    public int getNodeCount() {
        return nodes.size();
    }
    
    /**
     * Get the set of edges
     * @return the edges
     */
    public Set getEdges() {
        Set set = new HashSet();
        for (int i = 0; i < getNodeCount(); i++) {
            set.addAll(getNode(i).getEdges());
        }
        return set;
    }
    /**
     * Get the nodes
     * @return the nodes
     */
    public List getNodes() {
        return nodes;
    }

    /**
     * Set the nodes
     * @param list the nodes
     */
    public void setNodes(List list) {
        nodes = list;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
       String result = "";
       for (int i = 0; i < getNodeCount(); i++) {
           result += getNode(i) + "\n"; 
       }
       return result;
    }

}
