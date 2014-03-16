package util.graph;

/**
 * A class for producing a tree traversal
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DFSTree implements GraphTransformation {
    /**
     * Whether or not a node has been visited
     */
    boolean[] visited;

    /**
     * @see graph.GraphTransform#transform(graph.Graph)
     */
    public Graph transform(Graph g) {
        visited = new boolean[g.getNodeCount()];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }
        dfs(g.getNode(0));
        Tree result = new Tree(g.getNode(0));
        result.setNodes(g.getNodes());
        visited = null;
        return result;
    }
    
    /**
     * Perform a depth first search on the graph
     * @param g the graph to search
     */
    private void dfs(Node n) {
        visited[n.getLabel()] = true;
        for (int i = 0; i < n.getEdgeCount(); i++) {
            Edge edge = n.getEdge(i);
            Node other = edge.getOther(n);
            if (visited[other.getLabel()]) {
                n.removeEdge(i);
                i--;
            } else {
                dfs(other);
            }
        }
    }

}
