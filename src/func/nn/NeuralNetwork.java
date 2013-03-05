

package func.nn;

import java.io.Serializable;
import java.util.List;

import util.linalg.Vector;

/**
 * An abstract class representing a network
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class NeuralNetwork implements Serializable {
	
	/**
	 * Get the output values
	 * @return the output values
	 */
	public abstract Vector getOutputValues();
	
	/**
	 * Set intput values
	 * @param values the new values
	 */
	public abstract void setInputValues(Vector values);
    
    /**
     * Run the network on the input values and
     * generate the output values.
     */
    public abstract void run();
    
    /**
     * Get all of the weights in the neural network
     * @return all of the weights in the network
     */
    public abstract List getLinks();
    
    /**
     * Get link values
     * @return the link values
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
     * Set link values
     * @param weights the link values
     */
    public void setWeights(double[] weights) {
        List links = getLinks();
        for (int i = 0; i < weights.length; i++) {
            Link l = (Link) links.get(i);
            l.setWeight(weights[i]);
        }
    }

    /**
     * Set the weights of a neural network
     * @param weights the weight vector
     */
    public void setWeights(Vector weights) {
        List links = getLinks();
        for (int i = 0; i < weights.size(); i++) {
            Link l = (Link) links.get(i);
            l.setWeight(weights.get(i));
        }
    }

}
