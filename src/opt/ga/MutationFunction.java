package opt.ga;

import shared.Instance;

/**
 * An interface for mutatation operators
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface MutationFunction {
    /**
     * Mutate the given optimization data
     * @param d the data to mutate
     */
    public abstract void mutate(Instance d);

}
