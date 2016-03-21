

package func.nn;

import java.io.Serializable;
import java.util.List;

import util.linalg.Vector;

/**
 * An abstract class representing a neural network made up of <code> Layer </code> objects containing <code> Neuron </code> objects
 * connected by <code> Link </code> objects between consecutive layers.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class NeuralNetwork implements Serializable {
	
	/**
	 * Retrieves the output values.
	 * @return the output values from the output layer of the network
	 */
	public abstract Vector getOutputValues();
	
	/**
	 * Sets input values typically into the input layer of the network to be passed between layers to the 
	 * output layer.
	 * @param values the new input values
	 */
	public abstract void setInputValues(Vector values);
    
    /**
     * Runs the network on the input values by passing them along links between neurons in consecutive layers and
     * generates the output values.
     * @see Layer
     * @see Neuron
     * @see Link
     */
    public abstract void run();
    
    /**
     * Retrieves all of the links in the neural network.
     * @return all of the weights in the network
     * @see Link
     */
    public abstract List getLinks();
    
    /**
     * Retrieves all of the link weights in the neural network.
     * @return the link weights
     * @see Link
     */
    public double[] getWeights() {
        List links = getLinks();
        double[] weights = new double[links.size()];
        for (int i = 0; i < weights.length; i++) {
            Link l = (Link) links.get(i);
            weights[i] = l.getWeight();
        }
        return weights;
    }
    
    /**
     * Sets the link weights used to multiply values that are passed from their input neuron to their output neuron.
     * @param weights the link weights
     * @see Link
     * @see Neuron
     * @see NeuralNetwork#setWeights(Vector)
     */
    public void setWeights(double[] weights) {
        List links = getLinks();
        for (int i = 0; i < weights.length; i++) {
            Link l = (Link) links.get(i);
            l.setWeight(weights[i]);
        }
    }

    /**
     * Sets the link weights used to multiply values that are passed from their input neurons to their output neurons.
     * @param weights the weight vector
     * @see Link
     * @see Neuron
     * @see NeuralNetwork#setWeights(double[])
     */
    public void setWeights(Vector weights) {
        List links = getLinks();
        for (int i = 0; i < weights.size(); i++) {
            Link l = (Link) links.get(i);
            l.setWeight(weights.get(i));
        }
    }

}
