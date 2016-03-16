package opt.ga;

import dist.Distribution;

import shared.Instance;

/**
 * A continuous add-one mutation function which perturbs a randomly-chosen
 * dimension of the input instance by adding to it some uniform random value
 * in the range <code>[-amount/2,amount/2]</code>.
 * 
 * <p>
 * Note that this is basically an in-place version of
 * {@link opt.ContinuousAddOneNeighbor}.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ContinuousAddOneMutation implements MutationFunction {
    /**
     * The range by which to modify the value
     */
    private double amount;
    
    /**
     * Continuous add-one mutation
     * @param amount The range in which to perturb the instance
     */
    public ContinuousAddOneMutation(double amount) {
        this.amount = amount;
    }
    
    /**
     * Continuous add-one mutation with <code>amount=1</code>, that is,
     * {@link #mutate} will add a uniform random value in [-0.5, 0.5] 
     * to some dimension.
     */
    public ContinuousAddOneMutation() {
        this(1);
    }

    /**
     * Modify input <code>d</code> by perturbing one of its dimensions as
     * described.
     * @param cod Input value
     * @see opt.ga.MutationFunction
     */
    public void mutate(Instance cod) {
        int i = Distribution.random.nextInt(cod.size());
        cod.getData().set(i, cod.getContinuous(i)+ Distribution.random.nextDouble() * amount - amount / 2);
    }
}
