package func.nn.backprop;

/**
 * A soft max layer in a back propagation network
 * that can be used with a standard error measure
 * for multi class probability in the output layer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationSoftMaxOutputLayer 
        extends BackPropagationLayer {
          

    /**
     * @see nn.FeedForwardLayer#feedforward()
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
        // by caculating it ourselves
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
