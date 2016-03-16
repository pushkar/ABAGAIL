package opt;

import dist.Distribution;

import shared.Instance;

/**
 * A continuous add-one neighbor function which perturbs a randomly-chosen
 * dimension of the input instance by adding to it some uniform random value
 * in the range <code>[-amount/2,amount/2]</code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ContinuousAddOneNeighbor implements NeighborFunction {
    /**
     * The range by which to modify the value
     */
    private double amount;
    
    /**
     * Continuous add-one neighbor
     * @param amount The range in which to perturb the instance
     */
    public ContinuousAddOneNeighbor(double amount) {
        this.amount = amount;
    }
    
    /**
     * Continuous add-one neighbor with <code>amount=1</code>, that is,
     * {@link #neighbor} will add a uniform random value in [-0.5, 0.5] 
     * to some dimension.
     */
    public ContinuousAddOneNeighbor() {
        this(1);
    }

    /**
     * Return a new {@link Instance} produced by perturbing one dimension of
     * input <code>d</code> as described.
     * @see opt.NeighborFunction#neighbor
     * @param d Input value
     */
    public Instance neighbor(Instance d) {
        int i = Distribution.random.nextInt(d.size());
        Instance cod = (Instance) d.copy();
        cod.getData().set(i, cod.getContinuous(i)+ Distribution.random.nextDouble() * amount - amount / 2);
        return cod;
    }
}
