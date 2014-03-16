package shared;



/**
 * A error measure that is differentiable with
 * respect to the network outputs
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface GradientErrorMeasure extends ErrorMeasure {
    
    /**
     * Find the derivatives
     * @param output the outputs of the network
     * @param targets the targets of the network
     * @param index the index of the current pattern
     * @return the error derivatives
     */
    public abstract double[] gradient(Instance output, Instance example);

}
