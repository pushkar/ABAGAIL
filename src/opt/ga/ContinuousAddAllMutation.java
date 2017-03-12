package opt.ga;

import dist.Distribution;

import shared.Instance;

/**
 * A continuous add one neighbor function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ContinuousAddAllMutation implements MutationFunction {
    /**
     * The amount to add to the value
     */
    private double amount;

    /**
     * Continuous add one neighbor
     * @param amount the amount to add
     */
    public ContinuousAddAllMutation(double amount) {
        this.amount = amount;
    }

    /**
     * Continuous add one neighbor
     */
    public ContinuousAddAllMutation() {
        this(1);
    }

    /**
     * @see opt.ga.MutationFunction
     */
    public void mutate(Instance cod) {
        for (int i = 0; i < cod.size(); i++) {
            cod.getData().set(i, cod.getContinuous(i) + Distribution.random.nextDouble() * amount - amount / 2);
        }
    }
}
