package func.nn;
import java.util.ArrayList;
import java.util.List;

import util.linalg.Vector;


/**
 * A abstract neural network made up of <code> Layer </code> objects with at least one as an input layer, one as an output layer,
 * and some list of hidden layers. Each layer consists of <code> Neuron </code> objects connected between consecutive
 * layers by <code> Link </code> objects.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class LayeredNetwork extends NeuralNetwork {

	/**
	 * The input layer made up of neurons that hold the initial values through the network.
	 * @see Layer
	 * @see Neuron
	 */
	private Layer inputLayer;
	
	/**
	 * The output layer made up of neurons that hold the final values of the network.
	 * @see Layer
	 * @see Neuron
	 */
	private Layer outputLayer;
	
	/**
	 * The list of middle layers each made up of neurons that receive values kept in their activation variables that are
	 * multiplied by the weights of links between each pair of neurons in two consecutive layers as they move from one
	 * layer to the next.
	 * @see Layer
	 * @see Neuron
	 * @see Link
	 */
	private List hiddenLayers;
    
    /**
     * The cached list of links that connect each neuron in one layer to each neuron in the next layer
     * for the entire network. This variable remains null unless <code> LayeredNetwork#getLinks </code> is called after
     * which it is as described above.
     */
    private List links = null;
	
	/**
	 * Makes a new layered network with an input layer and an output layer each made up of
	 * neurons with links between the neurons of consecutive layers and initially no hidden layers
	 * in between.
	 * @see Layer
	 * @see Neuron
	 * @see Link
	 */
	public LayeredNetwork() {
		hiddenLayers = new ArrayList();
	}

	/**	
 	 * Retrieves the values in the activation variables of the neurons in the output layer which
 	 * represent the results from the network. This typically occurs after passing values through
 	 * the all previous layers as well along links between neurons in consecutive layers.
 	 * @see Layer
 	 * @see Neuron
 	 * @see Link
 	 * @return the vector of values from the output layer
 	 */
	public Vector getOutputValues() {
		return outputLayer.getActivations();
	}
	
	/**
	 * Sets the initial activation values for the neurons in the initial layer likely to
	 * be passed down through the layers of the network along links between neurons to
	 * eventually be retrieved from the output layer.
	 * @param values a vector of activation values for the input layer
	 * @see Layer
	 * @see Neuron
	 * @see Link
	 */
	public void setInputValues(Vector values) {
		inputLayer.setActivations(values);
	}
    
    /**
     * Retrieves the index of the neuron in the output layer with the largest activation value.
     * @return the index of a neuron in the output layer
     * @see Layer
     * @see Neuron
     */
    public int getDiscreteOutputValue() {
        return outputLayer.getGreatestActivationIndex();
    }
    
    /**
     * Calculates a boolean value based on whether the first neuron (although assumed to be the only one) in 
     * the output layer is greater than 0.5 or not.
     * @return the boolean value based on whether an activation is above 0.5 or not
     * @see Layer
     * @see Neuron
     */
    public boolean getBinaryOutputValue() {
        return outputLayer.getNode(0).getActivation() > .5;
    }

	/**
	 * Retrieves the input layer of neurons of this neural network.
	 * @return the input layer
	 * @see Layer
	 * @see Neuron
	 * @see LayeredNetwork#inputLayer
	 */
	public Layer getInputLayer() {
		return inputLayer;
	}

	/**
	 * Retrieves the list of the hidden layers between the input and output layers.
	 * @return the list of hidden layers
	 * @see Layer
	 * @see LayeredNetwork#hiddenLayers
	 */
	public List getHiddenLayers() {
		return hiddenLayers;
	}

	/**
	 * Retrieves the output layer of neurons of this neural network.
	 * @return the output layer
	 * @see Layer
	 * @see Neuron
	 * @see LayeredNetwork#outputLayer
	 */
	public Layer getOutputLayer() {
		return outputLayer;
	}

	/**
	 * Sets the input layer of this network to the given layer of neurons.
	 * @param layer the new input layer
	 * @see Layer
	 * @see Neuron
	 */
	public void setInputLayer(Layer layer) {
		inputLayer = layer;
	}

	/**
	 * Sets the output layer of this network to the given layer of neurons.
	 * @param layer the new output layer
	 * @see Layer
	 * @see Neuron
	 */
	public void setOutputLayer(Layer layer) {
		outputLayer = layer;
	}

	/**
	 * Gets the number of hidden layers between the input and output layers.
	 * @return the hidden layer count
	 * @see Layer
	 */
	public int getHiddenLayerCount() {
		return hiddenLayers.size();
	}
	
	/**
	 * Retrieves the ith hidden layer from the list of hidden layers of this neural network.
	 * @param i the index of the hidden layer in the hidden layer list
	 * @return the hidden layer located at the ith position of the list
	 * @see Layer
	 * @see LayeredNetwork#hiddenLayers
	 */
	public Layer getHiddenLayer(int i) {
		return (Layer) hiddenLayers.get(i);
	}
	
	/**
	 * Adds a hidden layer of neurons to the end of the list of hidden layers.
	 * @param layer the hidden layer to add
	 * @see Layer
	 * @see Neuron
	 * @see LayeredNetwork#hiddenLayers
	 */
	public void addHiddenLayer(Layer layer) {
		hiddenLayers.add(hiddenLayers.size(), layer);
	}

	/**
	 * Disconnects each layer of neurons from the following one in this network
	 * starting with the input layer if it exists followed by the hidden layers if
	 * there are any to the output layer if it exists. The method calls each
	 * layer's disconnect method.
	 * @see Neuron
	 * @see LayeredNetwork#inputLayer
	 * @see LayeredNetwork#hiddenLayers
	 * @see LayeredNetwork#outputLayer
	 * @see LayeredNetwork#getHiddenLayerCount()
	 * @see LayeredNetwork#getHiddenLayer(int)
	 * @see Layer#disconnect(Layer)
	 */
	public void disconnect() {
		if (inputLayer != null && getHiddenLayerCount() > 0) {
			Layer firstMiddle = getHiddenLayer(0);
			inputLayer.disconnect(firstMiddle);
		} else if (inputLayer != null && outputLayer != null) {
			inputLayer.disconnect(outputLayer);
		}
		for (int i = 0; i + 1 < getHiddenLayerCount(); i++) {
			Layer first = getHiddenLayer(i);
			Layer second = getHiddenLayer(i + 1);
			first.disconnect(second);
		}
		if (outputLayer != null && getHiddenLayerCount() > 0) {
			Layer lastMiddle = getHiddenLayer(getHiddenLayerCount() - 1);
			lastMiddle.disconnect(outputLayer);
		}
	}
	
	/**
	 * Connects each of the layers of neurons stored in the variables above of this network using links. The
	 * method calls each layer's connect method.
	 * @see Neuron
	 * @see LayeredNetwork#inputLayer
	 * @see LayeredNetwork#hiddenLayers
	 * @see LayeredNetwork#outputLayer
	 * @see LayeredNetwork#getHiddenLayerCount()
	 * @see LayeredNetwork#getHiddenLayer(int)
	 * @see Layer#connect(Layer)
	 */
	public void connect() {
		if (inputLayer != null && getHiddenLayerCount() > 0) {
			Layer firstMiddle = getHiddenLayer(0);
			inputLayer.connect(firstMiddle);
		} else if (inputLayer != null && outputLayer != null) {
			inputLayer.connect(outputLayer);
		}
		for (int i = 0; i + 1 < getHiddenLayerCount(); i++) {
			Layer first = getHiddenLayer(i);
			Layer second = getHiddenLayer(i + 1);
			first.connect(second);
		}
		if (outputLayer != null && getHiddenLayerCount() > 0) {
			Layer lastMiddle = getHiddenLayer(getHiddenLayerCount() - 1);
			lastMiddle.connect(outputLayer);
		}
	}
    
    /**
     * Retrieves all the links connecting each neuron of each layer to each neuron
     * in the following layer.
     * @return list a list of Links adjoining neurons in the layers
     * @see NeuralNetwork#getLinks()
     * @see Layer
     * @see Neuron
     * @see Link
     * @see LayeredNetwork#inputLayer
     * @see LayeredNetwork#hiddenLayers
     * @see LayeredNetwork#outputLayer
     * @see LayeredNetwork#getHiddenLayerCount()
     * @see LayeredNetwork#getHiddenLayer(int)
     * @see Layer#getLinks()
     * 
     */
    public List getLinks() {
       if (links != null) {
           return links;
       }
       links = new ArrayList();
       links.addAll(inputLayer.getLinks());
       for (int i = 0; i < getHiddenLayerCount(); i++) {
           links.addAll(getHiddenLayer(i).getLinks()); 
       }
       links.addAll(outputLayer.getLinks());
       return links;
    }

}
