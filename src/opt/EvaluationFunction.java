package opt;

import shared.Instance;

/**
 * A class representing an evaluation function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface EvaluationFunction {
    
    /**
     * Evaluate a data
     * @param d the data to evaluate
     * @return the value
     */
    public abstract double value(Instance d);

}
