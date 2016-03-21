package func.nn.feedfwd;

import func.nn.Layer;

/**
 * A <code> Layer </code> class that feeds activation values from the previous layer forward into the
 * <code> FeedForwardNode </code> objects in this layer of a neural network.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FeedForwardLayer extends Layer {

    /** 
     * Asks the nodes in this layer of the neural network to request the previous layer's nodes to feed
     * forward their values into this layer's nodes.
     * @see FeedForwardNetwork 
     * @see Layer#getNodeCount()
     * @see Layer#getNode(int)
     * @see FeedForwardNode#feedforward()
     */
    public void feedforward() {
        for (int i = 0; i < getNodeCount(); i++) {
            ((FeedForwardNode) getNode(i)).feedforward();
        }
    }

}
