package func.nn.activation;

import java.io.Serializable;

/**
 * An abstract class representing an activation function with which values can be calculated 
 * before being passed from a list of <code> Link </code> objects to a <code> Neuron </code> object
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class ActivationFunction implements Serializable {
    /**
     * Calculates the value using an activation function from some input, typically the sum of
     * input values from the links going into some neuron.
     * @param value the input value for the activation function
     * @return the activation function result
     * @see func.nn.Link
     * @see func.nn.Neuron
     */
    public abstract double value(double value);
}
