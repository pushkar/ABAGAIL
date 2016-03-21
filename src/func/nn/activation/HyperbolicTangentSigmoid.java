package func.nn.activation;

/**
 * The tanh sigmoid function which represents a specific <code> DifferentiableActivationFunction </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class HyperbolicTangentSigmoid 
		extends DifferentiableActivationFunction{

   /**
    * Calculates the derivative of the tanh activation function at the given value. This
	* method is typically used with some back propagation rules for adjusting weights.
	* @param value the input value for the derivative of the tanh activation function
	* @return the output of the tanh activation function's derivative
    * @see DifferentiableActivationFunction#derivative(double)
    */
	public double derivative(double value) {
		double tanhvalue = value(value);
		return 1 - tanhvalue * tanhvalue;
	}

	/**
	 * Calculates the value using the tanh function of some input. This method
     * is typically used as the final step before determining the activation value of a neuron.
     * @param value the input value for the tanh activation function
     * @return the tanh activation function result
     * @see func.nn.Link
	 * @see ActivationFunction#value(double)
	 */
    public double value(double value) {
        double e2x = Math.exp(2 * value);
        if (e2x == Double.POSITIVE_INFINITY) {
            return 1;
        } else {
            return (e2x - 1) / (e2x + 1);
        }
	}
	

}
