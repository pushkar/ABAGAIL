package func.nn.backprop;

import func.nn.feedfwd.FeedForwardLayer;

/**
 * A subclass of <code> FeedForwardLayer </code> and a part of a <code> BackPropagationNetwork </code> that can
 * send error values backwards through the network to adjust weights of <code> BackPropagationLink </code>
 * objects connecting <code> BackPropagationNode </code> objects. 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationLayer extends FeedForwardLayer {
    
    /**
     * Back propagates all the error values for this layer by first having each node calculate 
     * those values and then back propagating that error onto their links.
     * @see func.nn.Layer#getNodeCount()
     * @see func.nn.Layer#getNode(int)
     * @see BackPropagationNode#backpropagate()
     * @see BackPropagationNode#backpropagateLinks()
     */
    public void backpropagate() {
        for (int i = 0; i < getNodeCount(); i++) {
            BackPropagationNode node =
                 (BackPropagationNode) getNode(i);
            node.backpropagate();
            node.backpropagateLinks();
        }
    }
    
    /**
     * Clears out the error of all the links into this layer by asking each node within it
     * to clear its own links which effectively sets their errors to zero. Their error,
     * however, is stored in their lastError variable.
     * @see func.nn.Layer#getNodeCount()
     * @see func.nn.Layer#getNode(int)
     * @see BackPropagationNode#clearError()
     */
    public void clearError() {
        for (int i = 0; i < getNodeCount(); i++) {
            ((BackPropagationNode) getNode(i)).clearError();
        }
    }
    
    /**
     * Updates weights of all the links going into each node of this layer with the given rule. This
     * update usually involves some kind of function concerning a determined learning rate.
     * @param rule the rule to use
     * @see func.nn.Layer#getNodeCount()
     * @see func.nn.Layer#getNode(int)
     * @see BackPropagationNode#updateWeights(WeightUpdateRule)
     */
    public void updateWeights(WeightUpdateRule rule) {
        for (int i = 0; i < getNodeCount(); i++) {
            ((BackPropagationNode) getNode(i)).updateWeights(rule);
        }
    }
    
    /**
     * Sets the output errors for each node of this layer based on some array of errors received,
     * potentially calculated by comparing the current output value for some data with what
     * the true output value should be.
     * @param errors the output errors
     * @see func.nn.Layer#getNodeCount()
     * @see func.nn.Layer#getNode(int)
     * @see BackPropagationNode#setOutputError(double)
     */
    public void setOutputErrors(double[] errors) {
        for (int i = 0; i < getNodeCount(); i++) {
            ((BackPropagationNode) getNode(i)).setOutputError(errors[i]);
        }
    }
    
}
