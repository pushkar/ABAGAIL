package opt.ga;

import dist.Distribution;

import shared.Instance;

/**
 * A mutation function for changing a single value
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteChangeAllMutation implements MutationFunction {
    /**
     * The ranges of the different values
     */
    private int[] ranges;

    /**
     * Make a new discrete change one mutation function
     * @param ranges the ranges of the data
     */
    public DiscreteChangeAllMutation(int[] ranges) {
        this.ranges = ranges;
    }

    /**
     * @see opt.ga.MutationFunction#mutate(opt.OptimizationData)
     */
    public void mutate(Instance d) {
        for (int i = 0; i < d.size(); i++) {
            d.getData().set(i, Distribution.random.nextInt(ranges[i]));
        }
    }

}
