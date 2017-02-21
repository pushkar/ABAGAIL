package abagail.util.graph;

/**
 * Produces a depth-first-search tree traversal.
 *
 * @author Andrew Guillory - gtg008g@mail.gatech.edu
 */
public class DfsTree implements GraphTransformation {
    /**
     * Keeps track of which nodes have been visited.
     */
    private boolean[] visited;

    @Override
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
     * Perform a depth first search on the graph.
     *
     * @param n the node to start from
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
