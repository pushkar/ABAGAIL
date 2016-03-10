package opt;

import dist.Distribution;

import shared.Instance;

/**
 * A continuous add one neighbor function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ContinuousAddOneNeighbor implements NeighborFunction {
    /**
     * The amount to add to the value
     */
    private double amount;
    
    /**
     * Continuous add one neighbor
     * @param amount the amount to add
     */
    public ContinuousAddOneNeighbor(double amount) {
        this.amount = amount;
    }
    
    /**
     * Continuous add one neighbor
     */
    public ContinuousAddOneNeighbor() {
        this(1);
    }

    /**
     * @see opt.NeighborFunction#neighbor(opt.OptimizationData)
     */
    public Instance neighbor(Instance d) {
        //randomly choose a dimension
        int i = Distribution.random.nextInt(d.size());
        //make a copy of the original instance
        Instance cod = (Instance) d.copy();
        //randomly add [-0.5amount, 0.5amount) to the value at dimension i
        cod.getData().set(i, cod.getContinuous(i)+ Distribution.random.nextDouble() * amount - amount / 2);
        return cod;
    }
}
