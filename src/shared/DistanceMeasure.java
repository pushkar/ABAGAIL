package shared;


/**
 * A measure of the distance between vectors.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface DistanceMeasure {
    
    /**
     * Measure the distance between two vectors
     * @param va the first vector
     * @param vb the second vector
     * @return the distance between the vectors
     */
    public abstract double value(Instance va, Instance vb);


}
