

package func.nn.backprop;

import func.nn.Layer;
import func.nn.activation.DifferentiableActivationFunction;
import func.nn.activation.HyperbolicTangentSigmoid;
import func.nn.activation.LinearActivationFunction;
import func.nn.activation.LogisticSigmoid;
import func.nn.activation.DifferentiableActivationFunction;
import func.nn.feedfwd.FeedForwardLayer;
import func.nn.feedfwd.FeedForwardNetwork;
import func.nn.feedfwd.FeedForwardNeuralNetworkFactory;
import func.nn.feedfwd.FeedForwardNode;

/**
 * A class that builds various <code> BackPropagationNetwork </code> objects representing a multilayer perceptron.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationNetworkFactory {

    /**
     * Creates a multilayer perceptron with a given number of neurons per layer where the first is the input layer
	 * and the last is the output layer. The method also assigns the layers a transfer function except for the 
	 * output layer which can have its own.
	 * @param nodeCounts the number of nodes in each layer
	 * @param transfer the transfer function
	 * @param outputLayer the output layer of the network
	 * @param outputFunction the output layer transfer function
	 * @return a multilayer perceptron with nodeCounts.length layers
	 * @see BackPropagationNode
	 * @see BackPropagationLayer
	 * @see DifferentiableActivationFunction
	 * @see BackPropagationNetwork
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
     * Creates a multilayer perceptron based on a given number of neurons per layer where the first is the input layer,
	 * the last is the output layer, and the others are hidden layers. All layers receive a given differentiable 
	 * activation function except the last which specifically receives a linear activation function.
	 * @param nodeCounts the number of nodes in each layer
	 * @param transfer the transfer function
	 * @return a multilayer perceptron with nodeCounts.length layers
	 * @see DifferentiableActivationFunction
	 * @see LinearActivationFunction
	 * @see BackPropagationNode
	 * @see BackPropagationLayer#BackPropagationLayer()
	 * @see BackPropagationNetworkFactory#createNetwork(int[], DifferentiableActivationFunction, Layer, DifferentiableActivationFunction)
     */
    public BackPropagationNetwork createRegressionNetwork(int[] nodeCounts, 
            DifferentiableActivationFunction transfer) {
        return createNetwork(nodeCounts, transfer, new BackPropagationLayer(),
            new LinearActivationFunction());
    }

	/**
	 * Creates a multilayer perceptron based on a given number of neurons per layer where the first is the input layer,
	 * the last is the output layer, and the others are hidden layers. All layers receive a hyperbolic tangent sigmoid 
	 * activation function except the last which specifically receives a linear activation function.
	 * @param nodeCounts the number of nodes in each layer
	 * @return a multilayer perceptron with nodeCounts.length layers
	 * @see BackPropagationNode
	 * @see BackPropagationNetwork
	 * @see HyperbolicTangentSigmoid
	 * @see BackPropagationNetworkFactory#createRegressionNetwork(int[], DifferentiableActivationFunction)
	 */
	public BackPropagationNetwork createRegressionNetwork(int[] nodeCounts) {
		return createRegressionNetwork(nodeCounts, new HyperbolicTangentSigmoid());
	}
    
    /**
     * Creates a multilayer perceptron based on a given number of neurons per layer where the first is the input layer,
	 * the last is the output layer, and the others are hidden layers all of which receive a given differentiable 
	 * activation function except the last layer. If the output layer has only one node, it specifically receives a 
	 * logistic sigmoid activation function. Otherwise, a soft max output layer is created in place of a typical layer
	 * and receives a linear activation function.
	 * @param nodeCounts the number of nodes in each layer
	 * @param transfer the transfer function
	 * @return a multilayer perceptron with nodeCounts.length layers
	 * @see func.nn.activation.DifferentiableActivationFunction
	 * @see LogisticSigmoid#LogisticSigmoid()
	 * @see LinearActivationFunction#LinearActivationFunction()
	 * @see BackPropagationNode
	 * @see BackPropagationLayer#BackPropagationLayer()
	 * @see BackPropagationSoftMaxOutputLayer#BackPropagationSoftMaxOutputLayer()
	 * @see BackPropagationNetwork
	 * @see BackPropagationNetworkFactory#createNetwork(int[], DifferentiableActivationFunction, Layer, DifferentiableActivationFunction)
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
     * Creates a multilayer perceptron based on a given number of neurons per layer where the first is the input layer,
	 * the last is the output layer, and the others are hidden layers all of which receive a hyperbolic tangent sigmoid 
	 * activation function except the last layer. If the output layer has only one node, it specifically receives a 
	 * logistic sigmoid activation function. Otherwise, a soft max output layer is created in place of a typical layer
	 * and receives a linear activation function.
	 * @param nodeCounts the number of nodes in each layer
	 * @return a multilayer perceptron with nodeCounts.length layers
	 * @see HyperbolicTangentSigmoid
	 * @see HyperbolicTangentSigmoid#HyperbolicTangentSigmoid()
	 * @see BackPropagationNode
	 * @see BackPropagationNetwork
     */
    public BackPropagationNetwork createClassificationNetwork(int[] nodeCounts) {
        return createClassificationNetwork(nodeCounts, new HyperbolicTangentSigmoid());
    }


}
