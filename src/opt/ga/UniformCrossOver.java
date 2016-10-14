package opt.ga;

import shared.Instance;

import dist.Distribution;

/**
 * Implementation of the uniform crossover function for genetic algorithms.
 *
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class UniformCrossOver implements CrossoverFunction {

    /**
     * Mates two candidate solutions by uniformly sampling bits to create the crossover mask, then taking the true
     * bits from the first solution and the remaining bits from the second solution.
     *
     * @param a the first solution
     * @param b the second solution
     * @return the mated solution.
     */
    public Instance mate(Instance a, Instance b) {
        // Create space for the mated solution
        double[] newData = new double[a.size()];

        // Assign bits to the mated solution
        for (int i = 0; i < newData.length; i++) {
            // Randomly pick a boolean value to determine which parent to take the ith bit from
            if (Distribution.random.nextBoolean()) {
                newData[i] = a.getContinuous(i);
            } else {
                newData[i] = b.getContinuous(i);
            }
        }

        // Return the mated solution
        return new Instance(newData);
    }

}