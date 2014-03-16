package opt;

import shared.Instance;

/**
 * A problem that can be solved through ill climbing.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface HillClimbingProblem extends OptimizationProblem {
    
    /**
     * Find a neighbor to the given piece of data
     * @param d the data to find the neighbor of
     * @return the data
     */
    public abstract Instance neighbor(Instance d);
}
