package func.nn.feedfwd;

import func.nn.Neuron;

/**
 *
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-05
 */
public class FeedForwardBiasNode extends FeedForwardNode {

    public FeedForwardBiasNode(double activation) {
        super(null);
        super.setActivation(activation);
    }

    /**
     * @see func.nn.feedfwd.FeedForwardNode#feedforward()
     */
    public void feedforward() { }

    /**
     * Bias node should not be connected to other bias nodes
     * @param neuron other neuron to connect to
     */
    @Override
    public void connect(Neuron neuron) {
        if (!neuron.getClass().equals(FeedForwardBiasNode.class)) {
            super.connect(neuron);
        }
    }

}