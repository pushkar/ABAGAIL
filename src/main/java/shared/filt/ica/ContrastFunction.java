package shared.filt.ica;

/**
 * A contrast function for use by ICA
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface ContrastFunction {
    /**
     * Evaluate the derivative of the contrast function
     * on the given value
     * @param value the value to evaluate the derivative on
     * @return the evaluated derivative
     */
    public double g(double value);

    /**
     * The second derivative of the contrast function
     * @param value the evaluated second derivative
     * @return the value
     */
    public double gprime(double value);
}
