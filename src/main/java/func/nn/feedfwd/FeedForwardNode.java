

package func.nn.feedfwd;

import func.nn.Neuron;
import func.nn.activation.ActivationFunction;

/**
 * A node in a feed forward network
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FeedForwardNode extends Neuron {

	/**
	 * The transfer function
	 */
	private ActivationFunction activationFunction;
    
    /**
     * The weighted input sum
     */
    private double weightedInputSum;

	/**
	 * Make a new feed forward node
	 * @param transfer the transfer function
	 */
	public FeedForwardNode(ActivationFunction transfer) {
		activationFunction = transfer;
	}
	
	/**
	 * Get the transfer function
	 * @return the transfer function
	 */
	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

    /**
     * Get the weighted input sum for this node
     * @return the weighted input sum
     */
    public double getWeightedInputSum() {
        return weightedInputSum;
    }

    /**
     * Feed forward the activation values into this node.
     * Calculates the sum of the input values and stores
     * this value into weightedInputSum.
     * Runs this sum through the activation function
     * and stores this into the activation for the node.
     */
	public void feedforward() {
		if (getInLinkCount() > 0) {
			double sum = 0;
			for (int i = 0; i < getInLinkCount(); i++) {
				sum += getInLink(i).getWeightedInValue();
			}
            weightedInputSum = sum;
			setActivation(activationFunction.value(sum));
		}
	}

}
