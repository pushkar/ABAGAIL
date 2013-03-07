package shared;

/**
 * An abstract distance measure with some extra little things
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class AbstractDistanceMeasure implements DistanceMeasure {
    /**
     * Calculate the distance between two data sets
     * @param a the first
     * @param b the second
     * @return the distance
     */
    public double value(DataSet a, DataSet b) {
        double distance = 0;
        for (int i = 0; i < a.size(); i++) {
            distance += value(a.get(i), b.get(i));
        }
        return distance;
    }

}
