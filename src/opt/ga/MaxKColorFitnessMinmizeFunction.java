package opt.ga;

import opt.EvaluationFunction;
import shared.Instance;
import util.graph.Edge;
import util.linalg.Vector;

/**
 * A Max K Color evaluation function that minimizes coloring miss-colored nodes
 *
 * Fitness Function for Max K colors problem minimizes the amount of miss-colored nodes in a graph.
 * Thus, a graph that is correctly colored will have zero miss-colored nodes meaning no single node
 * will have the same color as any adjacent node.
 *
 * Example:  Given a 5 node graph with the following  edges = [(0, 1), (0, 2), (0, 4), (1, 3), (2, 0), (2, 3), (3, 4)]
 *
 * Proper coloring with k-colors could be these which have zero miss-colored nodes:
 * k=2 [0, 1, 1, 0, 1] or [1, 0, 0, 1, 0]
 * K=3 [0, 1, 1, 2, 1]
 *
 * @author robododge
 * @version 1.0
 */
public class MaxKColorFitnessMinmizeFunction implements EvaluationFunction {

    public static int DEFAULT_ALLOWED_MISSES = 10;

    private  int allowedMisses = DEFAULT_ALLOWED_MISSES;

    /**
     *
     */
    private Edge[] edges;

    /**
     * Function Evaluation Count
     */
    private int fEvals;

    /**
     *
     * The edges of the graph is all that is needed to initialize.
     * Also, a value of the allowable misses meaning how many miss-colored nodes
     * are allowed before the function abandons processing
     * @param edges Edges 1-D array
     * @param allowedMisses  Allowed misses, also controls the fitness score between 0 and allowedMisses
     */
    public MaxKColorFitnessMinmizeFunction(Edge[] edges, int allowedMisses) {
        this.edges = edges;
        this.fEvals = 0;
        this.allowedMisses = allowedMisses;
        this.missesRemaining = this.allowedMisses;
    }

    /**
     * Convience constructor with default misses
     * @param edges
     */
    public MaxKColorFitnessMinmizeFunction(Edge[] edges) {
        this(edges, DEFAULT_ALLOWED_MISSES);
    }

    private boolean conflict = false;
    private int missesRemaining = 0;

    /**
     * how many iterations does it take to find if k-colors.  A miss will be tracked
     * each time any adjacent nodes are marked with a color that matches between them.
     */
    public double value(Instance d) {
        this.fEvals++;
        Vector data = d.getData();
        int n = data.size();
        this.missesRemaining = this.allowedMisses;

        double iterations = 0;
        int edgeCount = edges.length;
        conflict = false;

        for (int j = 0; j < edgeCount; j++) {
            Edge edge = edges[j];
            iterations++;

            int labelA = edge.getA().getLabel();
            int labelB = edge.getB().getLabel();
            if (data.get(labelA) == data.get(labelB)) {
                this.missesRemaining--;
            }
            if (missesRemaining <= 0 ){
                this.conflict = true;
                break;
            }

        }

        double fitnessResult = 0;
        if (this.missesRemaining > 0) {
            fitnessResult = this.missesRemaining;
        }

        return fitnessResult;
    }

    /**
     * Return function evaluation count
     *
     * @return int fEvals
     */
    public int getFunctionEvaluations() {
        return this.fEvals;
    }

    /**
     * Reset function evaluation count
     */
    public void resetFunctionEvaluationCount() {
        this.fEvals = 0;
    }

    public String foundConflict() {
        int missCount = this.allowedMisses - this.missesRemaining;
        String successText = String.format("Found Max-K Color Combination ! with %d misses", missCount);
        return conflict ? "Failed to find Max-K Color combination !" : successText;
    }
}
