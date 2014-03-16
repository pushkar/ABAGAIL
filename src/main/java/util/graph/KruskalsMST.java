package util.graph;

import java.util.Arrays;
import java.util.Set;

/**
 * Kruskal's minimum spanning tree algorithm
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class KruskalsMST implements GraphTransformation {
    
    /**
     * The ranks of the nodes
     */
    private int[] ranks;
    
    /**
     * The paths of the nodes
     */
    private int[] paths;

    /**
     * @see graph.GraphTransform#transform(graph.Graph)
     */
    public Graph transform(Graph g) {
        final Set var = g.getEdges();
        WeightedEdge[] edges = (WeightedEdge[]) var.toArray(new WeightedEdge[var.size()]);
        Arrays.sort(edges);
        for (int i = 0; i < g.getNodeCount(); i++) {
            g.getNode(i).getEdges().clear();      
        }
        ranks = new int[g.getNodeCount()];
        paths = new int[g.getNodeCount()];
        for (int i = 0; i < g.getNodeCount(); i++) {
            ranks[i] = 0;
            paths[i] = i;
        }
        for (final WeightedEdge edge : edges) {
            int in = edge.getA().getLabel();
            int out = edge.getB().getLabel();
            if (set(in) != set(out)) {
                combine(in, out);
                g.getNode(in).connect(g.getNode(out), edge);
            }
        }
        ranks = null;
        paths = null;
        return g;
    }

    /**
     * Find the set label for a given index
     * @param i the set label to find
     * @return the root label of the set
     */
    private int set(int i) {
        if (paths[i] != i) {
            paths[i] = set(paths[i]);
        }
        return paths[i];
    }
    
    /**
     * Combine two the sets
     * @param i the first set to combine
     * @param j the second to combine
     */
    private void combine(int i, int j) {
        link(set(i), set(j));
    }
    
    /**
     * Link together two sets
     * @param i the first set
     * @param j the second set
     */
    private void link(int i, int j) {
        if (ranks[i] > ranks[j]) {
            paths[j] = i;
        } else {
            paths[i] = j;
            if (ranks[i] == ranks[j]) {
                ranks[j]++;
            }
        }
    }

}
