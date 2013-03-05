package func.nn;
import java.util.ArrayList;
import java.util.List;

import util.linalg.Vector;


/**
 * A layered neural network
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class LayeredNetwork extends NeuralNetwork {

	/**
	 * The input layer
	 */
	private Layer inputLayer;
	
	/**
	 * The output layer
	 */
	private Layer outputLayer;
	
	/**
	 * The list of middle layers
	 */
	private List hiddenLayers;
    
    /**
     * The cached list of links
     */
    private List links = null;
	
	/**
	 * Make a new layered network
	 */
	public LayeredNetwork() {
		hiddenLayers = new ArrayList();
	}

	/**	
 	 * @see Network#getOutputValues()
 	 */
	public Vector getOutputValues() {
		return outputLayer.getActivations();
	}
	
	/**
	 * @see Network#setInputValues(double[])
	 */
	public void setInputValues(Vector values) {
		inputLayer.setActivations(values);
	}
    
    /**
     * Get the index of the node with the largest value
     * @return the index
     */
    public int getDiscreteOutputValue() {
        return outputLayer.getGreatestActivationIndex();
    }
    
    /**
     * Get the binary output value
     * @return the binary output value
     */
    public boolean getBinaryOutputValue() {
        return outputLayer.getNode(0).getActivation() > .5;
    }

	/**
	 * Get the input layer
	 * @return the layer
	 */
	public Layer getInputLayer() {
		return inputLayer;
	}

	/**
	 * Get the list of middle layers
	 * @return the list
	 */
	public List getHiddenLayers() {
		return hiddenLayers;
	}

	/**
	 * Get the output layer
	 * @return the layer
	 */
	public Layer getOutputLayer() {
		return outputLayer;
	}

	/**
	 * Set the input layer
	 * @param layer the new layer
	 */
	public void setInputLayer(Layer layer) {
		inputLayer = layer;
	}

	/**
	 * Set the output layer
	 * @param layer the output layer
	 */
	public void setOutputLayer(Layer layer) {
		outputLayer = layer;
	}

	/**
	 * Get the middle layer count
	 * @return the middle layer count
	 */
	public int getHiddenLayerCount() {
		return hiddenLayers.size();
	}
	
	/**
	 * Get the middle layer
	 * @param i the index of the middle layer
	 * @return the layer
	 */
	public Layer getHiddenLayer(int i) {
		return (Layer) hiddenLayers.get(i);
	}
	
	/**
	 * Add a middle layer
	 * @param layer the layer to add
	 */
	public void addHiddenLayer(Layer layer) {
		hiddenLayers.add(hiddenLayers.size(), layer);
	}

	/**
	 * Disconnect this network
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
	 * Connect this network
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
     * @see nn.NeuralNetwork#getLinks()
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
