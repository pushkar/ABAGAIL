package shared;

/**
 * An abstract error measure
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class ErrorMeasure extends MonoidOfDoublesForDataSets {
    /**
     * Measure the error for the given output and target
     * @param output the output
     * @param example the example
     * @return the error
     */
    public abstract double value(Instance output, Instance example);
}
