package func.nn.backprop;

import func.nn.Layer;
import util.linalg.Vector;

/**
 * A binary wrapper of the back propagation network to return values based on a 0.5 output threshold.
 * @author Joshua Wang joshuawang@gatech.edu
 */
public class BackPropagationNetworkBinary extends BackPropagationNetwork {
    public BackPropagationNetworkBinary(BackPropagationNetwork network) {
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
