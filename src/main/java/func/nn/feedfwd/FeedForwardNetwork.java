package func.nn.feedfwd;

import func.nn.LayeredNetwork;

/**
 * A feed forward network
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FeedForwardNetwork extends LayeredNetwork {

    /**
     * @see nn.Network#run()
     */
    public void run() {
        for (int i = 0; i < getHiddenLayerCount(); i++) {
            ((FeedForwardLayer) getHiddenLayer(i)).feedforward();
        }
        ((FeedForwardLayer) getOutputLayer()).feedforward();
    }
    
    
}
