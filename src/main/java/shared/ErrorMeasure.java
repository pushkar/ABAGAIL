package shared;

/**
 * A class representing an error measure
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface ErrorMeasure {

    /**
     * Measure the error for the given output and target
     * @param output the output
     * @param example the example
     * @return the error
     */
    public abstract double value(Instance output, Instance example);

}
