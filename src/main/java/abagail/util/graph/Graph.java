package abagail.util.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a graph of nodes and edges.
 *
 * @author Andrew Guillory - gtg008g@mail.gatech.edu
 */
public class Graph {
    /**
     * The list of nodes.
     */
    private List<Node> nodes;

    /**
     * Creates a new graph.
     */
    public Graph() {
        nodes = new ArrayList<>();
    }

    /**
     * Adds a node to the graph.
     *
     * @param n the node to add
     */
    public void addNode(Node n) {
        n.setLabel(getNodeCount());
        nodes.add(n);
    }

    /**
     * Returns the node with the given index.
     *
     * @param i the index of the node to return
     * @return the node
     */
    public Node getNode(int i) {
        return nodes.get(i);
    }

    /**
     * Returns the number of nodes in the graph.
     *
     * @return the number of nodes
     */
    public int getNodeCount() {
        return nodes.size();
    }

    /**
     * Returns the set of edges.
     *
     * @return the edges
     */
    public Set<Edge> getEdges() {
        Set<Edge> set = new HashSet<>();
        for (int i = 0; i < getNodeCount(); i++) {
            set.addAll(getNode(i).getEdges());
        }
        return set;
    }

    /**
     * Returns the nodes in the graph.
     *
     * @return the nodes
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Sets the nodes in this graph.
     *
     * @param nodes the nodes
     */
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < getNodeCount(); i++) {
            result += getNode(i) + "\n";
        }
        return result;
    }

}
