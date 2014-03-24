package shared;


/**
 * The standard hamming distance
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class HammingDistance extends DistanceMeasure {

    /**
     * @see memory.DistanceMeasure#distanceSquared(double[], double[])
     */
    public double value(Instance a, Instance b) {
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            if (a.getDiscrete(i) != b.getDiscrete(i)) {
                sum += 1;
            }
        }
        return sum;
    }
}
