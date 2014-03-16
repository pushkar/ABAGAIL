

package func.nn.backprop;

import func.nn.Layer;
import func.nn.activation.DifferentiableActivationFunction;
import func.nn.activation.HyperbolicTangentSigmoid;
import func.nn.activation.LinearActivationFunction;
import func.nn.activation.LogisticSigmoid;

/**
 * A multi layer perceptron factory
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationNetworkFactory {

    /**
     * Create a multilayer perceptron
     * @param nodeCounts the number of nodes in each layer
     * @param transfer the transfer function
     * @param outputLayer the output layer of the network
     * @param outputFunction the output transfer function
     * @return a multilayer perceptron with nodeCounts.length layers
     */
	private BackPropagationNetwork createNetwork(int[] nodeCounts,
	       DifferentiableActivationFunction transfer, Layer outputLayer,
           DifferentiableActivationFunction outputFunction) {
		if (nodeCounts.length < 2) {
			throw new IllegalArgumentException();
		}
		BackPropagationNetwork network = new BackPropagationNetwork();
		
        // create the input layer
        Layer inputLayer = new BackPropagationLayer();
		for (int i = 0; i < nodeCounts[0]; i++) {
			inputLayer.addNode(new BackPropagationNode(null));
		}
        inputLayer.addNode(new BackPropagationBiasNode(1));
		network.setInputLayer(inputLayer);
        
        // create hidden layers
		for (int i = 1; i < nodeCounts.length - 1; i++) {
			Layer hiddenLayer = new BackPropagationLayer();
			for (int j = 0; j < nodeCounts[i]; j++) {
				hiddenLayer.addNode(new BackPropagationNode(transfer));
			}
            hiddenLayer.addNode(new BackPropagationBiasNode(1));
			network.addHiddenLayer(hiddenLayer);
		}
        
        // create the output layer
       for (int i = 0; i < nodeCounts[nodeCounts.length - 1]; i++) {
			outputLayer.addNode(new BackPropagationNode(outputFunction));
		}
		network.setOutputLayer(outputLayer);
		network.connect();
		return network;
	}
    
    /**
     * Create a multilayer perceptron
     * @param nodeCounts the number of nodes in each layer
     * @param transfer the transfer function
     * @return a multilayer perceptron with nodeCounts.length layers
     */
    public BackPropagationNetwork createRegressionNetwork(int[] nodeCounts, 
            DifferentiableActivationFunction transfer) {
        return createNetwork(nodeCounts, transfer, new BackPropagationLayer(),
            new LinearActivationFunction());
    }

	/**
	 * Create a multilayer perceptron
	 * @param nodeCounts the number of nodes in each layer
	 * @return a multilayer perceptron with nodeCounts.length layers
	 */
	public BackPropagationNetwork createRegressionNetwork(int[] nodeCounts) {
		return createRegressionNetwork(nodeCounts, new HyperbolicTangentSigmoid());
	}
    
    /**
     * Create a multilayer perceptron
     * with a softmax output layer
     * @param nodeCounts the number of nodes in each layer
     * @param transfer the transfer function
     * @return a multilayer perceptron with nodeCounts.length layers
     */
    public BackPropagationNetwork createClassificationNetwork(int[] nodeCounts,
           DifferentiableActivationFunction transfer) {
       if (nodeCounts[nodeCounts.length - 1] == 1) {
           return createNetwork(nodeCounts, transfer, new BackPropagationLayer(),
             new LogisticSigmoid());     
       } else {
           return createNetwork(nodeCounts, transfer, new BackPropagationSoftMaxOutputLayer(),
               new LinearActivationFunction());
       }
    }

    /**
     * Create a multilayer perceptron
     * @param nodeCounts the number of nodes in each layer
     * @return a multilayer perceptron with nodeCounts.length layers
     */
    public BackPropagationNetwork createClassificationNetwork(int[] nodeCounts) {
        return createClassificationNetwork(nodeCounts, new HyperbolicTangentSigmoid());
    }


}
