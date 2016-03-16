package opt;

import dist.Distribution;

import shared.Instance;

/**
 * A neighbor function which changes a single value in an {@link Instance} -
 * specifically, it changes a randomly-chosen dimension to a random integer in
 * a given range.  Ranges are set per-dimension.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteChangeOneNeighbor implements NeighborFunction {
    
    /**
     * The ranges of the different values
     */
    private int[] ranges;
    
    /**
     * Make a new change-one neighbor function.  In the given ranges, element
     * <code>i</code> of the array gives the range of dimension <code>i</code>
     * of the input {@link Instance}.
     * <p>
     * A value of <code>N</code> in the range corresponds to integers from 0 to
     * <code>N-1</code>, inclusive.
     * @param ranges the ranges of the data; should have as many elements as
     * dimensions of input {@link Instance} to {@link #neighbor}.
     */
    public DiscreteChangeOneNeighbor(int[] ranges) {
        this.ranges = ranges;
    }
    
    /**
     * @return A neighboring {@link Instance} in which one randomly chosen
     * dimension of <code>d</code> is set to a random value inside of the
     * corresponding range.
     * @see opt.NeighborFunction#neighbor
     */
    public Instance neighbor(Instance d) {
        Instance cod = (Instance) d.copy();
        int i = Distribution.random.nextInt(ranges.length);
        cod.getData().set(i, Distribution.random.nextInt(ranges[i]));
        return cod;
    }

}