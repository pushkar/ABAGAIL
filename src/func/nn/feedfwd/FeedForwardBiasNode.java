package func.nn.feedfwd;
import func.nn.feedfwd.FeedForwardNode;

/**
 * A subclass of <code> FeedForwardNode </code> that acts as a constant value alongside
 * other <code> Neuron </code> objects in a <code> Layer </code> as values pass through that layer.
 * @author Jesse Rosalia https://github.com/theJenix
 */
public class FeedForwardBiasNode extends FeedForwardNode {
	
	/**
	 * Creates a bias node that represents a constant value that contains no activation
	 * function and simply holds an activation value.
	 * @param activation a bias value for the layer it lies in
	 * @see FeedForwardLayer
	 * @see FeedForwardNode#setActivation(double)
	 */
    public FeedForwardBiasNode(double activation) {
        super(null);
        super.setActivation(activation);
    }

    /**
     * Overrides the feedforward() method so that it does nothing since this node represents
     * a constant for the layer and so cannot receive values from previous layers.
     * @see FeedForwardLayer
     * @see FeedForwardNode#feedforward()
     */
    public void feedforward() { }
}