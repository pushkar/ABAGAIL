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
        int i = Distribution.random.nextInt(d.size());
        Instance cod = (Instance) d.copy();
        cod.getData().set(i, cod.getContinuous(i)+ Distribution.random.nextDouble() * amount - amount / 2);
        return cod;
    }
}
