

package func.nn.activation;
/**
 * The logistic sigmoid function which represents a specific <code> DifferentiableActivationFunction </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LogisticSigmoid extends DifferentiableActivationFunction {

    /**
     * Calculates the value using the logistic sigmoid function of some input. This method
     * is typically used as the final step before determining the activation value of a neuron.
     * @param value the input value for the logistic sigmoid activation function
     * @return the logistic sigmoid activation function result
     * @see func.nn.Link
     * @see ActivationFunction#value(double)
     */
	public double value(double value) {
        double enx = Math.exp(-value);
        if (enx == Double.POSITIVE_INFINITY) {
            return 0;
        } else {
            return 1.0 / (1.0 + enx);        
        }
	}

	/**
	 * Calculates the derivative of the logistic sigmoid activation function at the given value. This
	 * method is typically used with some back propagation rules for adjusting weights.
	 * @param value the input value for the derivative of the logistic sigmoid activation function
	 * @return the output of the logistic sigmoid activation function's derivative
	 * @see DifferentiableActivationFunction#derivative(double)
	 */
    public double derivative(double value) {
        double logistic = value(value);
        return logistic * (1 - logistic);
	}

}
