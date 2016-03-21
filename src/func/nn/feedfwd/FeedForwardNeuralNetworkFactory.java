package func.nn.feedfwd;

import shared.DataSetDescription;
import func.nn.Layer;
import func.nn.activation.DifferentiableActivationFunction;
import func.nn.activation.HyperbolicTangentSigmoid;
import func.nn.activation.LinearActivationFunction;
import func.nn.activation.LogisticSigmoid;
import func.nn.feedfwd.FeedForwardLayer;
import func.nn.feedfwd.FeedForwardNetwork;
import func.nn.feedfwd.FeedForwardNode;

/**
 * A class that builds various <code> FeedForwardNetwork </code> objects.
 * @author Jesse Rosalia https://github.com/theJenix
 * @version 1.0
 */
public class FeedForwardNeuralNetworkFactory {

    /**
	 * Creates a multilayer perceptron with a given number of neurons per layer where the first is the input layer
	 * and the last is the output layer. The method also assigns the layers a transfer function except for the 
	 * output layer which can have its own.
	 * @param nodeCounts the number of nodes in each layer
	 * @param transfer the transfer function
	 * @param outputLayer the output layer of the network
	 * @param outputFunction the output transfer function
	 * @return a multilayer perceptron with nodeCounts.length layers
	 * @see FeedForwardNode
	 * @see Layer
	 * @see FeedForwardLayer
	 * @see DifferentiableActivationFunction
	 * @see FeedForwardNetwork
	 */
    private FeedForwardNetwork createNetwork(int[] nodeCounts,
           DifferentiableActivationFunction transfer, Layer outputLayer,
           DifferentiableActivationFunction outputFunction) {
        if (nodeCounts.length < 2) {
            throw new IllegalArgumentException();
        }
        FeedForwardNetwork network = new FeedForwardNetwork();
        
        // create the input layer
        Layer inputLayer = new FeedForwardLayer();
        for (int i = 0; i < nodeCounts[0]; i++) {
            inputLayer.addNode(new FeedForwardNode(null));
        }
        inputLayer.addNode(new FeedForwardBiasNode(1));
        network.setInputLayer(inputLayer);
        
        // create hidden layers
        for (int i = 1; i < nodeCounts.length - 1; i++) {
            Layer hiddenLayer = new FeedForwardLayer();
            for (int j = 0; j < nodeCounts[i]; j++) {
                hiddenLayer.addNode(new FeedForwardNode(transfer));
            }
            hiddenLayer.addNode(new FeedForwardBiasNode(1));
            network.addHiddenLayer(hiddenLayer);
        }
        
        // create the output layer
       for (int i = 0; i < nodeCounts[nodeCounts.length - 1]; i++) {
            outputLayer.addNode(new FeedForwardNode(outputFunction));
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
	 * @see FeedForwardNode
	 * @see FeedForwardLayer#FeedForwardLayer()
	 * @see FeedForwardNeuralNetworkFactory#createNetwork(int[], DifferentiableActivationFunction, Layer, DifferentiableActivationFunction)
	 */
    public FeedForwardNetwork createRegressionNetwork(int[] nodeCounts,
            DifferentiableActivationFunction transfer) {
        return createNetwork(nodeCounts, transfer, new FeedForwardLayer(),
            new LinearActivationFunction());
    }

    /**
	 * Creates a multilayer perceptron based on a given number of neurons per layer where the first is the input layer,
	 * the last is the output layer, and the others are hidden layers. All layers receive a hyperbolic tangent sigmoid 
	 * activation function except the last which specifically receives a linear activation function.
	 * @param nodeCounts the number of nodes in each layer
	 * @return a multilayer perceptron with nodeCounts.length layers
	 * @see FeedForwardNode
	 * @see FeedForwardNetwork
	 * @see HyperbolicTangentSigmoid
	 * @see FeedForwardNeuralNetworkFactory#createRegressionNetwork(int[], DifferentiableActivationFunction)
	 */
    public FeedForwardNetwork createRegressionNetwork(int[] nodeCounts) {
        return createRegressionNetwork(nodeCounts, new HyperbolicTangentSigmoid());
    }
    
    /**
	 * Creates a multilayer perceptron based on a given number of neurons per layer where the first is the input layer,
	 * the last is the output layer, and the others are hidden layers. All layers receive a given differentiable 
	 * activation function except the last which specifically receives a logistic sigmoid activation function.
	 * @param nodeCounts the number of nodes in each layer
	 * @param transfer the transfer function
	 * @return a multilayer perceptron with nodeCounts.length layers
	 * @see func.nn.activation.DifferentiableActivationFunction
	 * @see LogisticSigmoid#LogisticSigmoid()
	 * @see FeedForwardNode
	 * @see FeedForwardNetwork
	 * @see FeedForwardNeuralNetworkFactory#createNetwork(int[], DifferentiableActivationFunction, Layer, DifferentiableActivationFunction)
	 */
    public FeedForwardNetwork createClassificationNetwork(int[] nodeCounts,
           DifferentiableActivationFunction transfer) {
       if (nodeCounts[nodeCounts.length - 1] == 1) {
           return createNetwork(nodeCounts, transfer, new FeedForwardLayer(),
             new LogisticSigmoid());
       } else {
           return createNetwork(nodeCounts, transfer, new FeedForwardLayer(),
               new LogisticSigmoid());
       }
    }

    /**
	 * Creates a multilayer perceptron based on a given number of neurons per layer where the first is the input layer,
	 * the last is the output layer, and the others are hidden layers. All layers receive a hyperbolic tangent sigmoid 
	 * activation function except the last which specifically receives a logistic sigmoid activation function.
	 * @param nodeCounts the number of nodes in each layer
	 * @return a multilayer perceptron with nodeCounts.length layers
	 * @see HyperbolicTangentSigmoid#HyperbolicTangentSigmoid()
	 * @see FeedForwardNode
	 * @see FeedForwardNetwork
	 */
    public FeedForwardNetwork createClassificationNetwork(int[] nodeCounts) {
        return createClassificationNetwork(nodeCounts, new HyperbolicTangentSigmoid());
    }

    /**
     * Retrieves the number of optimal hidden layer nodes based on the number of attributes in the data and the range
     * of the label description.
     * @param desc the description of some data set
     * @param labelDesc the description of some data set's label
     * @return the number of optimal hidden layers of nodes there should be
     * @see DataSetDescription#getAttributeCount()
     * @see DataSetDescription#getDiscreteRange()
     */
    public int getOptimalHiddenLayerNodes(DataSetDescription desc, DataSetDescription labelDesc) {
        return 2 * desc.getAttributeCount() / 3 + labelDesc.getDiscreteRange();
    }
}
