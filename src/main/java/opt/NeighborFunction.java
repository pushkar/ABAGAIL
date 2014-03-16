package opt;

import shared.Instance;

/**
 * A neighbor function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface NeighborFunction {
    /**
     * Get the neighbor of a piece of data
     * @param d the data
     * @return the neighbor
     */
    public Instance neighbor(Instance d);

}
