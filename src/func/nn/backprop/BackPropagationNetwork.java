

package func.nn.backprop;

import func.nn.feedfwd.FeedForwardNetwork;

/**
 *  A <code> FeedForwardNetwork </code> object that can back propagate error through layers from the output layer 
 *  to the input layer along <code> BackPropagationLink </code> objects connecting <code> BackPropagationdNode </code> 
 *  objects within each <code> BackPropagationLayer </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationNetwork extends FeedForwardNetwork {

    /**
     * Back propagates error through the network from the output layer back. Each layer then asks each
     * node to back propagate values as well as each node's links.
     * @see BackPropagationLayer#backpropagate()
     * @see BackPropagationNetwork#getOutputLayer()
     * @see BackPropagationNetwork#getHiddenLayerCount()
     * @see BackPropagationNetwork#getHiddenLayer(int)
     */
	public void backpropagate() {
		((BackPropagationLayer) getOutputLayer()).backpropagate();
		for (int i = getHiddenLayerCount() - 1; i >= 0; i--) {
			((BackPropagationLayer) getHiddenLayer(i)).backpropagate();;
		}
	}

	/**
     * Clears out the error values such as at the end of a batch or at the end of a single training for
     * stochastic / online training. The layers each ask their nodes each to clear the errors of their links
     * which set their error values to zero but retains the error in the lastError variables.
     * @see BackPropagationLayer#clearError()
     * @see BackPropagationNetwork#getOutputLayer()
     * @see BackPropagationNetwork#getHiddenLayerCount()
     * @see BackPropagationNetwork#getHiddenLayer(int)
	 */
    public void clearError() {
        ((BackPropagationLayer) getOutputLayer()).clearError();
        for (int i = getHiddenLayerCount() - 1; i >= 0; i--) {
            ((BackPropagationLayer) getHiddenLayer(i)).clearError();;
        }
    }
    
    /**
     * Updates the weights of the links between the nodes of the layers of this
     * network with the given rule.
     * @param rule the rule to use to update weights
     * @see WeightUpdateRule
     * @see BackPropagationLayer#updateWeights(WeightUpdateRule)
     * @see BackPropagationNetwork#getOutputLayer()
     * @see BackPropagationNetwork#getHiddenLayerCount()
     * @see BackPropagationNetwork#getHiddenLayer(int)
     */
    public void updateWeights(WeightUpdateRule rule) {
        ((BackPropagationLayer) getOutputLayer()).updateWeights(rule);
        for (int i = getHiddenLayerCount() - 1; i >= 0; i--) {
            ((BackPropagationLayer) getHiddenLayer(i)).updateWeights(rule);;
        }
    }
    
    /**
     * Sets the output errors likely determined by some function of the difference 
     * between the output values of the nodes in the output layer and the true 
     * values of some data being trained on.
     * @param errors the output errors
     * @see BackPropagationLayer#setOutputErrors(double[])
     * @see BackPropagationNetwork#getOutputLayer()
     */
    public void setOutputErrors(double[] errors) {
        ((BackPropagationLayer) getOutputLayer()).setOutputErrors(errors);
    }




}
