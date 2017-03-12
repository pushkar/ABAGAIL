package func.nn.feedfwd;

import func.nn.Layer;
import util.linalg.Vector;

/**
 * Splits the output values of a feedforward network based on a threshold of 0.5
 * @author Joshua Wang joshuawang@gatech.edu
 */
public class FeedForwardNetworkBinary extends FeedForwardNetwork {
    public FeedForwardNetworkBinary(FeedForwardNetwork network) {
        super();
        this.setInputLayer(network.getInputLayer());
        for (Object o : network.getHiddenLayers()) {
            Layer hiddenLayer = (Layer)o;
            this.addHiddenLayer(hiddenLayer);
        }
        this.setOutputLayer(network.getOutputLayer());
        this.connect();
    }

    public Vector getOutputValues() {
        Vector output = super.getOutputValues();
        for (int i = 0; i < output.size(); i++) {
            output.set(i, output.get(i) < 0.5 ? 0.0 : 1.0);
        }
        return output;
    }
}
