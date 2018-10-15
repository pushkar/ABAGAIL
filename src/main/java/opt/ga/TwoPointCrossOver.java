package opt.ga;

import dist.Distribution;
import shared.Instance;

/**
 * Implementation of the two-point crossover function for genetic algorithms.
 * Two-point crossover calls for two points to be selected on the parent organism strings. Everything between the two
 * points is swapped between the parent organisms.
 *
 * @author Avriel Harvey aharvey32@gatech.edu
 * @version 1.0
 */
public class TwoPointCrossOver implements CrossoverFunction {

    /**
     * Mate two candidate solutions using two-point crossover.
     * Generates two random points x and y, and takes the bits [x,y) from the first instance, and the remaining bits
     * from the second instance.
     *
     * @param a the first solution
     * @param b the second solution
     * @return the mated solution
     */
    public Instance mate(Instance a, Instance b) {
        // Create space for the mated solution
        double[] newData = new double[a.size()];

        // Randomly assign the first point
        int firstPoint = Distribution.random.nextInt(newData.length + 1);

        // Make sure the second point comes after the first point
        int secondPoint = Distribution.random.nextInt(newData.length + 1 - firstPoint) + firstPoint;

        // Assign bits to the mated solution
        for (int i = 0; i < newData.length; i++) {
            if (i >= firstPoint && i < secondPoint) {
                newData[i] = a.getContinuous(i);
            } else {
                newData[i] = b.getContinuous(i);
            }
        }

        // Return the mated solution
        return new Instance(newData);
    }
}