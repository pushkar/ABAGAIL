

package func.nn.activation;
/**
 * A sigmoid activation function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LogisticSigmoid extends DifferentiableActivationFunction {

    /**
     * @see nn.function.ActivationFunction#activation(double)
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
	 * @see nn.function.DifferentiableActivationFunction#derivative(double)
	 */
    public double derivative(double value) {
        double logistic = value(value);
        return logistic * (1 - logistic);
	}

}
