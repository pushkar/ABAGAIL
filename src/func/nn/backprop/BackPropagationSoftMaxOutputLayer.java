package func.nn.backprop;

import func.nn.Layer;
import func.nn.feedfwd.FeedForwardNetwork;
import func.nn.feedfwd.FeedForwardNode;

/**
 * A soft max layer that is an output layer in some <code> BackPropagationNetwork </code> that can be used with 
 * a standard error measure for multi class probability. It's feedforward method essentially reduces the extremity
 * of any overwhelmingly large values concerning its output values and normalizes them.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationSoftMaxOutputLayer 
        extends BackPropagationLayer {
          

    /**
     * Asks the nodes in this output layer of the neural network to interact with the previous layer nodes to feed
     * forward their values into the nodes of this layer. The method then calculates the largest weighted input
     * sum which is subtracted from all the nodes making the result non-positive and exponentiated so that the values
     * are between 0 and 1 and set as the activation for each node. The activation values are then normalized. This
     * method essentially reduces the extremity of any overwhelmingly large values concerning the output nodes.
     * @see BackPropagationNetwork 
     * @see BackPropagationLayer#getNodeCount()
     * @see BackPropagationLayer#getNode(int)
     * @see BackPropagationNode#feedforward()
     * @see func.nn.feedfwd.FeedForwardLayer#feedforward()
     */
    public void feedforward() {
        // feed forward to calculate
        // the weighted input sums
        super.feedforward();
        // trick stolen from Torch library for preventing overflows
        double shift = ((BackPropagationNode) getNode(0)).getWeightedInputSum();
        for (int i = 0; i < getNodeCount(); i++) {
            BackPropagationNode node = 
               (BackPropagationNode) getNode(i);
            shift = Math.max(shift, node.getWeightedInputSum());      
        }
        // now override the activation values
        // by calculating it ourselves
        // with the softmax formula
        double sum = 0;
        for (int i = 0; i < getNodeCount(); i++) {
            BackPropagationNode node = 
                (BackPropagationNode) getNode(i);
            node.setActivation(
                Math.exp(node.getWeightedInputSum() - shift));     
            sum += node.getActivation();
        }
        for (int i = 0; i < getNodeCount(); i++) {
            BackPropagationNode node = 
                (BackPropagationNode) getNode(i);
            node.setActivation(node.getActivation() / sum);
        }
    }
}
