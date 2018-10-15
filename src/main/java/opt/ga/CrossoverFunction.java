package opt.ga;

import shared.Instance;

/**
 * An interface for cross over functions
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface CrossoverFunction {
    
    /**
     * Mate two candidate solutions
     * @param a the first solution
     * @param b the second
     * @return the mated solution
     */
    public Instance mate(Instance a, Instance b);

}
