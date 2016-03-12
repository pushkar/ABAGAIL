package opt.ga;

import dist.Distribution;

import shared.Instance;

/**
 * Implementation of the single point crossover function for genetic algorithms.
 *
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SingleCrossOver implements CrossoverFunction {

    /**
     * Mates two candidate solutions using single point crossover by choosing a point in the bit string, and creating
     * a crossover mask of 0s up to that point, then 1s after. The mated solution takes the first bits from the second
     * solution, and the remaining bits from the first.
     *
     * @param a the first solution
     * @param b the second solution
     * @return the mated solution
     */
    public Instance mate(Instance a, Instance b) {
        // Create space for the mated solution
        double[] newData = new double[a.size()];

        // Randomly assign the dividing point
        int point = Distribution.random.nextInt(newData.length + 1);

        // Assign the bits for the mated solution
        for (int i = 0; i < newData.length; i++) {
            if (i >= point) {
                newData[i] = a.getContinuous(i);
            } else {
                newData[i] = b.getContinuous(i);
            }
        }

        // Return the mated solution
        return new Instance(newData);
    }

}