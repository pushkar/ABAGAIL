

package func.nn.activation;
/**
 * An activation function that is differentiable and can be used to calculate adjustments
 * of weights of <code> Link </code> objects based on error measures of a <code> LayeredNetwork </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class DifferentiableActivationFunction extends ActivationFunction {	
	
	/**
	 * Calculates the derivative of the activation function on the given value.
	 * @param value the input value for the derivative of the activation function
	 * @return the output of the activation function's derivative
	 * @see ActivationFunction
	 */
	public abstract double derivative(double value);
    
}
