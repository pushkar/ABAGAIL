package opt;

import dist.Distribution;

import shared.Instance;

/**
 * A neighbor function for changing a single value
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteChangeAllNeighbor implements NeighborFunction {

    /**
     * The ranges of the different values
     */
    private int[] ranges;

    /**
     * Make a new change one neighbor function
     * @param ranges the ranges of the data
     */
    public DiscreteChangeAllNeighbor(int[] ranges) {
        this.ranges = ranges;
    }

    /**
     * @see opt.NeighborFunction#neighbor(opt.OptimizationData)
     */
    public Instance neighbor(Instance d) {
        Instance cod = (Instance) d.copy();
        for (int i = 0; i < d.size(); i++) {
            cod.getData().set(i, Distribution.random.nextInt(ranges[i]));
        }
        return cod;
    }

}