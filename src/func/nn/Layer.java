package func.nn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import util.linalg.DenseVector;
import util.linalg.Vector;


/**
 * A collection of <code> Neuron </code> objects to be used in a <code> NeuralNetwork </code> as either an input, hidden, or output layer.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class Layer implements Serializable {

	/**
	 * The list of neurons in this layer.
	 * @see Neuron
	 */
	private List nodes;

	/**
	 * Makes a new empty layer of neurons.
	 * @see Neuron
	 */
	public Layer() {
		nodes = new ArrayList();
	}

	/**
	 * Returns the number of neurons in this layer of a neural network.
	 * @return the number of nodes
	 * @see Neuron
	 */
	public int getNodeCount() {
		return nodes.size();
	}
	
	/**
	 * Returns the ith neuron in this layer of some neural network.
	 * @param i the ith neuron to get
	 * @return the neuron
	 * @see Neuron
	 */
	public Neuron getNode(int i) {
		return (Neuron) nodes.get(i);
	}
	
	/**
	 * Adds a particular neuron to this layer of some neural network.
	 * @param node the neuron to add
	 * @see Neuron
	 */
	public void addNode(Neuron node) {
		nodes.add(node);
	}
	
	/**
	 * Changes each of this layer's neuron activation variables representing the value passed
	 * to them based on the vector input values.
	 * @param values the values of the neurons activation values
	 * @see Neuron
	 */
	public void setActivations(Vector values) {
		for (int i = 0; i < values.size(); i++) {
			getNode(i).setActivation(values.get(i));
		}
	}
	
	/**
	 * Retrieves the list of all the values of the activation variables, each representing the current
	 * value of each neuron in this layer.
	 * @return the list of each neuron's activation values
	 * @see Neuron
	 */
	public Vector getActivations() {
		double[] values = new double[getNodeCount()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getNode(i).getActivation();
		}
		return new DenseVector(values);
	}

    
    /**
     * Retrieves the index of the neuron with the largest activation which represents
     * the value passed to each neuron in this layer.
     * @return the largest activation's neuron's index
     * @see Neuron
     */
    public int getGreatestActivationIndex() {
        int largest = 0;
        double largestValue = getNode(largest).getActivation();
        for (int i = 1; i < getNodeCount(); i++) {
            if (getNode(i).getActivation() > largestValue) {
                largest = i;
                largestValue = getNode(largest).getActivation();
            }
        }
        return largest;
    }

	/**
	 * Connects the neurons in this layer to each of the neurons in another layer by
	 * creating a link between each through this layer's neurons' connect methods. This layer's neurons' values 
	 * are then able to be passed to the following layer after typically having each link's weight value multiplied by it. 
	 * @param layer the soon to be following layer to connect to
	 * @see Neuron
	 * @see Link
	 */
	public void connect(Layer layer) {
		for (int i = 0; i < getNodeCount(); i++) {
			Neuron node = getNode(i);
			for (int j = 0; j < layer.getNodeCount(); j++) {
				node.connect(layer.getNode(j));
			}
		}
	}

	/**
	 * Disconnects the neurons in this layer from each of the neurons in another layer by
	 * calling this layer's neurons' disconnect methods which remove links between these sets
	 * of neurons.
	 * @param layer the layer following this layer to be disconnected with
	 * @see Neuron
	 * @see Link
	 */
	public void disconnect(Layer layer) {
		for (int i = 0; i < getNodeCount(); i++) {
			Neuron node = getNode(i);
			for (int j = 0; j < layer.getNodeCount(); j++) {
				node.disconnect(layer.getNode(i));
			}
		}
	}
    
    /**
     * Gathers a list of all of the incoming links of the neurons from this layer.
     * @return all of the links going into this layer
     * @see Neuron
     * @see Link
     */
    public List getLinks() {
        List links = new ArrayList();
        for (int i = 0; i < nodes.size(); i++) {
            Neuron n = (Neuron) nodes.get(i);
            links.addAll(n.getInLinks());
        }
        return links;
    }

}
