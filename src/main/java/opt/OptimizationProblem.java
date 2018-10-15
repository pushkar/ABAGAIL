package opt;

import shared.Instance;

/**
 * A class representing an optimization problem.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface OptimizationProblem {
    
    /**
     * Evaluate the given data
     * @param d the data to evaluate
     * @return the value of the data.
     */
    public abstract double value(Instance d);
    
    /**
     * Draw a random sample of optimization data.
     * @return the sampled data.
     */
    public abstract Instance random();

}
