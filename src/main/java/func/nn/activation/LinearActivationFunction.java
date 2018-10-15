package func.nn.activation;

/**
 * A linear activation function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LinearActivationFunction extends DifferentiableActivationFunction {

    /**
     * @see nn.function.DifferentiableActivationFunction#derivative(double)
     */
    public double derivative(double value) {
        return 1;
    }

    /**
     * @see nn.function.ActivationFunction#activation(double)
     */
    public double value(double value) {
        return value;
    }

}
