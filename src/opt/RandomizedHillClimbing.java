package opt;

import shared.Instance;

/**
 * A randomized hill climbing algorithm
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RandomizedHillClimbing extends OptimizationAlgorithm {

    /**
     * The current optimization data
     */
    private Instance cur;

    /**
     * The current value of the data
     */
    private double curVal;

    // Track the best instance / value seen
    private Instance best;
    private double bestVal;

    // Max number of iterations to go without finding a better max
    private int SINCE_NEW_MAX = 10;

    // Keep track of iterations since we found a new max
    private int sinceNewCount = 0;

    /**
     * Make a new randomized hill climbing
     */
    public RandomizedHillClimbing(HillClimbingProblem hcp) {
        super(hcp);
        cur = hcp.random();
        curVal = hcp.value(cur);
        best = cur;
        bestVal = curVal;
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        HillClimbingProblem hcp = (HillClimbingProblem) getOptimizationProblem();

        if (sinceNewCount > SINCE_NEW_MAX) {
            cur = hcp.random();
            curVal = hcp.value(cur);
            sinceNewCount = 0;
        }

        Instance neigh = hcp.neighbor(cur);
        double neighVal = hcp.value(neigh);
        if (neighVal > curVal) {
            curVal = neighVal;
            cur = neigh;
            sinceNewCount = 0;
        } else {
            sinceNewCount += 1;
        }

        if (curVal > bestVal) {
            bestVal = curVal;
            best = cur;
        }

        return bestVal;
    }

    /**
     * @see opt.OptimizationAlgorithm#getOptimalData()
     */
    public Instance getOptimal() {
        return cur;
    }

}
