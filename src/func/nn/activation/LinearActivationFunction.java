package func.nn.activation;

/**
 * The linear identity function which represents a specific <code> DifferentiableActivationFunction </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LinearActivationFunction extends DifferentiableActivationFunction {

    /**
     * Calculates the derivative of the linear identity activation function at the given value. This
	 * method is typically used with some back propagation rules for adjusting weights.
	 * @param value the input value for the derivative of the linear identity activation function
	 * @return the output of the linear identity activation function's derivative
     * @see DifferentiableActivationFunction#derivative(double)
     */
    public double derivative(double value) {
        return 1;
    }

    /**
     * Calculates the value using the linear identity function of some input. This method
     * is typically used as the final step before determining the activation value of a neuron.
     * @param value the input value for the linear identity activation function
     * @return the output of the linear identity activation
     * @see func.nn.Link
     * @see ActivationFunction#value(double)
     */
    public double value(double value) {
        return value;
    }

}
