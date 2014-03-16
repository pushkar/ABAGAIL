

package func.nn;

import java.io.Serializable;
import java.util.Random;

/**
 * A link between two nodes in a neural network
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class Link implements Serializable {
    /**
     * The random number generator
     */
    private static Random random = new Random();
	
	/**
	 * The weight
	 */
	private double weight;
	
	/**
	 * The in node
	 */
	private Neuron inNode;
	
	/**
	 * The out node
	 */
	private Neuron outNode;
    
    /**
     * Create a new linke
     * initializes the weight to a random value
     */
    public Link() {
        weight = random.nextDouble() * 2 - 1;
    }
	
	/**
	 * Get the in node
	 * @return the node
	 */
	public Neuron getInNode() {
		return inNode;
	}
    
    /**
     * Set the in node
     * @param node the node
     */
    public void setInNode(Neuron node) {
        inNode = node;
    }

	/**
	 * Get the out node
	 * @return the node
	 */
	public Neuron getOutNode() {
		return outNode;
	}
    
    /**
     * Set the out node
     * @param node the node
     */
    public void setOutNode(Neuron node) {
        outNode = node;
    }
	
	/**
	 * Get the input value
	 * @return the value
	 */
	public double getInValue() {
		return inNode.getActivation();
	}
	
	/**
	 * Get the output value
	 * @return the value
	 */
	public double getOutValue() {
		return outNode.getActivation();
	}
	
	/**
	 * Get the weighted out value
	 * @return the weighted out value
	 */
	public double getWeightedOutValue() {
		return outNode.getActivation() * weight;
	}
	
	/**
	 * Get the weighted in value
	 * @return the value
	 */
	public double getWeightedInValue() {
		return inNode.getActivation() * weight;
	}

	/**
	 * Get the weight of the link
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Set the weight of the link
	 * @param d the new weight
	 */
	public void setWeight(double d) {
		weight = d;
	}

	/**
	 * Update the weight
	 * @param delta the change in weight
	 */
	public void changeWeight(double delta) {
		weight += delta;
	}
}
