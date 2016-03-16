package opt.ga;

import dist.Distribution;

import shared.Instance;

/**
 * A mutation function for changing a single value in an {@link Instance}.
 * The input {@link Instance} has a randomly-chosen dimension changed to a
 * random integer in a given range.  Ranges are set per-dimension.
 * 
 * <p>
 * Note that this is basically an in-place version of
 * {@link opt.DiscreteChangeOneNeighbor}.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteChangeOneMutation implements MutationFunction {    
    /**
     * The ranges of the different values
     */
    private int[] ranges;
    
    /**
     * Make a new discrete change one mutation function.  In the given ranges,
     * element <code>i</code> of the array gives the range of dimension
     * <code>i</code>of the input {@link Instance}.
     * <p>
     * A value of <code>N</code> in the range corresponds to integers from 0 to
     * <code>N-1</code>, inclusive.
     * @param ranges the ranges of the data; should have as many elements as
     * dimensions of input {@link Instance} to {@link #mutate}.
     */
    public DiscreteChangeOneMutation(int[] ranges) { 
        this.ranges = ranges;
    }

    /**
     * Modify the input {@link Instance} by randomly choosing a dimension and
     * setting it to a random value inside of the corresponding range.
     * @see opt.ga.MutationFunction#mutate
     */
    public void mutate(Instance d) {
        int i = Distribution.random.nextInt(d.size());
        d.getData().set(i, Distribution.random.nextInt(ranges[i]));
    }

}
