package func.nn.activation;

import java.io.Serializable;

/**
 * A activation function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class ActivationFunction implements Serializable {
    /**
     * Activation of a value
     * @param value the value
     * @return the activation
     */
    public abstract double value(double value);
}
