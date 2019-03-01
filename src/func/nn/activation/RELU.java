package func.nn.activation;

/**
 * A RELU activation function
 * @author Jeremy Aguilon, jeraguilon@gmail.com
 * @version 1.0
 */
public class RELU extends DifferentiableActivationFunction {

    /**
     * @see nn.function.ActivationFunction#activation(double)
     */
    public double value(double value) {
        return Math.max(value, 0.0);
    }

    /**
     * @see nn.function.DifferentiableActivationFunction#derivative(double)
     */
    public double derivative(double value) {
        if (value <= 0) {
            return 0;
        } else {
            return 1;
        }
    }
}
