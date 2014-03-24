package shared;

/**
 * An abstract distance measure with some extra little things
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class DistanceMeasure extends MonoidOfDoublesForDataSets {

    /**
     * Measure the distance between two vectors
     * @param a the first vector
     * @param b the second vector
     * @return the distance between the vectors
     */
    @Override
    public abstract double value(Instance a, Instance b);
}
