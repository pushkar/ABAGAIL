

package func.nn.feedfwd;

import func.nn.Neuron;
import func.nn.activation.ActivationFunction;

/**
 * A subclass of <code> Neuron </code> and part of a <code> FeedFowardLayer </code> in a <code> FeedForwardNetwork </code> that
 * holds onto the sum of weighted values received from neurons in the previous layer, if it exists, along <code> Link </code>
 * objects and applies an <code> ActivationFunction </code> such as a sigmoid function to calculate its activation value.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FeedForwardNode extends Neuron {

	/**
	 * The transfer function used to calculate the activation value of this neuron after
	 * being applied to the weightedInputSum.
	 * @see Neuron
	 * @see ActivationFunction
	 */
	private ActivationFunction activationFunction;
    
    /**
     * The weighted input sum of the incoming values along links from neurons in the previous layer,
     * if it exists.
     * @see Neuron
     * @see func.nn.Link
     */
    private double weightedInputSum;

	/**
	 * Creates a new feed forward node which is a neuron with an activation function.
	 * @param transfer the transfer function used to calculate the activation value of this neuron.
	 * @see Neuron
	 * @see ActivationFunction
	 * @see FeedForwardNode#activationFunction
	 * @see FeedForwardNode#weightedInputSum
	 */
	public FeedForwardNode(ActivationFunction transfer) {
		activationFunction = transfer;
	}
	
	/**
	 * Retrieves the transfer function used to calculate the activation value after being applied
	 * to the weightedInputSum value.
	 * @return the transfer function
	 * @see FeedForwardNode#activationFunction
	 * @see FeedForwardNode#weightedInputSum
	 */
	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

    /**
     * Retrieves the weighted input sum for this neuron.
     * @return the weighted input sum
     * @see Neuron
     * @see FeedForwardNode#weightedInputSum
     */
    public double getWeightedInputSum() {
        return weightedInputSum;
    }

    /**
     * Feeds forward the activation values from the neurons in the previous layer, if it exists,
     * into this neuron after multiplying by each link's weight and storing the sum in the weightedInputSum variable.
     * The method then applies the activation function on this sum before storing the result in the activation variable.
     * @see Neuron#getInLinks()
     * @see Neuron#getInLinkCount()
     * @see Neuron#getInLink(int)
     * @see func.nn.Link#getWeightedInValue()
     * @see ActivationFunction
     * @see ActivationFunction#value(double)
     * @see FeedForwardNode#setActivation(double)
     * @see FeedForwardNode#weightedInputSum
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
