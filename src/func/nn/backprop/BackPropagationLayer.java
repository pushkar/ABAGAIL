package func.nn.backprop;

import func.nn.feedfwd.FeedForwardLayer;

/**
 * A layer in a backpropagation network
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationLayer extends FeedForwardLayer {
    
    /**
     * Back propagate all the error values for this
     * layer.
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
     * Clear out the error derivatives in the weights
     */
    public void clearError() {
        for (int i = 0; i < getNodeCount(); i++) {
            ((BackPropagationNode) getNode(i)).clearError();
        }
    }
    
    /**
     * Update weights with the given rule
     * @param rule the rule to use
     */
    public void updateWeights(WeightUpdateRule rule) {
        for (int i = 0; i < getNodeCount(); i++) {
            ((BackPropagationNode) getNode(i)).updateWeights(rule);
        }
    }
    
    /**
     * Set the output errors for this layer
     * @param errors the output errors
     */
    public void setOutputErrors(double[] errors) {
        for (int i = 0; i < getNodeCount(); i++) {
            ((BackPropagationNode) getNode(i)).setOutputError(errors[i]);
        }
    }
    
}
