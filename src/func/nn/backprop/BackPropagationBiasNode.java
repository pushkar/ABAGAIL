package func.nn.backprop;

/**
 * A bias node, implemented as a node that refuses to feed forward values or back propagate values. It essentially
 * acts as a constant value in calculations involving <code> BackPropagationNode </code> objects in the current
 * <code> BackPropagationLayer </code> as values pass through its layer. This is a little wasteful since it has 
 * useless <code> BackPropagationLink </code> objects that go into it.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationBiasNode extends BackPropagationNode {
    
    /**
     * Creates a bias node that represents a constant value that contains no activation
	 * function and simply holds an activation value.
     * @param bias the bias value to set to
     * @see BackPropagationNode
     * @see func.nn.feedfwd.FeedForwardBiasNode#setActivation(double)
     */
    public BackPropagationBiasNode(double bias) {
        super(null);
        setActivation(bias);
    }

    /**
     * Overrides the feedforward() method so that it does nothing since this node represents
     * a constant for the layer and so cannot receive values from previous layers.
     * @see func.nn.feedfwd.FeedForwardNode#feedforward()
     */    
    public void feedforward() { }
    
    /**
     * Overrides the backpropagate() method so that it does nothing since this node represents
     * a constant for the layer and so does not send values to previous layers for adjustment purposes.
     * @see BackPropagationNode#backpropagate()
     */
    public void backpropagate() { }
    
}
