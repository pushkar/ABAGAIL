package func.nn.backprop;

import func.nn.Neuron;

/**
 * A bias node, implemented as a node
 * that refuses to feed forward values
 * or backpropagate values.  This is a little
 * wasteful since as it is used it has useless
 * links that go into it.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationBiasNode extends BackPropagationNode {

    /**
     * A bias node
     * @param bias the bias value to set to
     */
    public BackPropagationBiasNode(double bias) {
        super(null);
        setActivation(bias);
    }

    /**
     * @see func.nn.feedfwd.FeedForwardNode#feedforward()
     */
    public void feedforward() { }

    /**
     * @see func.nn.backprop.BackPropagationNode#backpropagate()
     */
    public void backpropagate() { }

    /**
     * Bias node should not be connected to other bias nodes
     * @param neuron other neuron to connect to
     */
    @Override
    public void connect(Neuron neuron) {
        if (!neuron.getClass().equals(BackPropagationBiasNode.class)) {
            super.connect(neuron);
        }
    }

}
