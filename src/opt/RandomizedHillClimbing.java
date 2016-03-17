package opt;

import shared.Instance;

/**
 * An implementation of randomized hill climbing.  The starting value is
 * randomized, and hill climbing proceeds from there, updating the maximum
 * value to a neighbor at each iteration if that neighbor produces a higher
 * value.
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
    
    /**
     * Make a new instance of randomized hill climbing for the given
     * {@link opt.HillClimbingProblem}, and initialize the starting value to be
     * random.
     */
    public RandomizedHillClimbing(HillClimbingProblem hcp) {
        super(hcp);
        cur = hcp.random();
        curVal = hcp.value(cur);
    }

    /**
     * Run one iteration of hill climbing.  The optimal value may move to
     * a neighbor (depending on its value and the current value).
     * @see shared.Trainer#train()
     */
    public double train() {
        HillClimbingProblem hcp = (HillClimbingProblem) getOptimizationProblem();
        Instance neigh = hcp.neighbor(cur);
        double neighVal = hcp.value(neigh);
        if (neighVal > curVal) {
            curVal = neighVal;
            cur = neigh;
        }
        return curVal;
    }

    /**
     * @return Current value of the given {@link opt.HillClimbingProblem}
     * @see opt.OptimizationAlgorithm#getOptimal()
     */
    public Instance getOptimal() {
        return cur;
    }

}
