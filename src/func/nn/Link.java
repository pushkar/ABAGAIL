

package func.nn;

import java.io.Serializable;
import java.util.Random;

/**
 * A link between two neurons in adjacent layers of a neural network on which values pass from
 * the input neuron after potentially being multiplied by the given weight of the link to be stored 
 * with others as a sum in the activation variable of the output neuron.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class Link implements Serializable {
    /**
     * The random number generator which is used to initialize the weight of the link
     * upon the link's creation. Every link after the first link, uses the same random
     * number generator to save on memory due the static modifier.
     */
    private static Random random = new Random();
	
	/**
	 * The weight of the link which can be used to find the weighted value being passed
	 * from the link's input neuron to the output neuron after multiplication. 
	 */
	private double weight;
	
	/**
	 * The input <code> Neuron </code> from which the link receives a value to be
	 * passed to the output Neuron after potentially being multiplied by the weight.
	 */
	private Neuron inNode;
	
	/**
	 * The output <code> Neuron </code> that receives a value to be stored in its
	 * activation variable after being passed along this link from the intput Neuron 
	 * and potentially being multiplied by the link's weight.
	 */
	private Neuron outNode;
    
    /**
     * Creates a new link that connections two consecutive neurons and randomly chooses 
     * its weight in the range [-1,1) which will likely be used to multiply an activation 
     * value from the input neuron and passed to the output neuron as values are sent
     * through the neural network.
     * @see NeuralNetwork
     * @see LayeredNetwork
     * @see Neuron
     */
    public Link() {
        weight = random.nextDouble() * 2 - 1;
    }
	
	/**
	 * Retrieves the input neuron to this link.
	 * @return the neuron
	 * @see Neuron
	 */
	public Neuron getInNode() {
		return inNode;
	}
    
    /**
     * Sets the input neuron from which activation values will be received
     * and processed.
     * @param node the neuron that will send it values to process
     * @see Neuron
     */
    public void setInNode(Neuron node) {
        inNode = node;
    }

	/**
	 * Retrieves the output neuron of this link.
	 * @return the neuron
	 * @see Neuron
	 */
	public Neuron getOutNode() {
		return outNode;
	}
    
    /**
     * Sets the output neuron that it sends values to from the input neuron after
     * processing them potentially by multiplying by this link's weight.
     * @param node the neuron
     * @see Neuron
     */
    public void setOutNode(Neuron node) {
        outNode = node;
    }
	
	/**
	 * Gets the input value received from the input neuron.
	 * @return a double the activation value of the input neuron
	 * @see Neuron#getActivation()
	 */
	public double getInValue() {
		return inNode.getActivation();
	}
	
	/**
	 * Gets the output value from the output neuron.
	 * @return a double the activation value of the output neuron
	 * @see Neuron#getActivation()
	 */
	public double getOutValue() {
		return outNode.getActivation();
	}
	
	/**
	 * Calculates the weighted out value by multiplying this link's weight variable
	 * by the output neuron's activation variable.
	 * @return a double the output neuron's activation value times this link's weight
	 * @see Neuron#getActivation()
	 * @see Link#weight
	 */
	public double getWeightedOutValue() {
		return outNode.getActivation() * weight;
	}
	
	/**
	 * Calculates the weighted in value, likely to be stored in the output neuron's activation value,
	 * by multiplying this link's weight variable by the input neuron's activation variable.
	 * @return a double the input neuron's activation value times this link's weight
	 * @see Neuron#getActivation()
	 * @see Link#weight
	 */
	public double getWeightedInValue() {
		return inNode.getActivation() * weight;
	}

	/**
	 * Retrieves the weight of this link.
	 * @return a double this link's weight
	 * @see Link#weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Sets the weight value of the link to be used when passing values from the
	 * input neuron to the output neuron.
	 * @param d a double, the new weight
	 * @see Link#weight
	 */
	public void setWeight(double d) {
		weight = d;
	}

	/**
	 * Updates this link's weight based on some delta value likely calculated through
	 * the means of some backpropogation method or some randomly calculated change.
	 * @param delta a double, the change in weight
	 * @see Link#weight
	 */
	public void changeWeight(double delta) {
		weight += delta;
	}
}
