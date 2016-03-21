package func.nn.backprop;

import func.nn.Link;



/**
 * A subclass of <code> Link </code> that adjusts its weights based on error that is propagated
 * back through the network from the output <code> BackPropagationLayer </code> using some 
 * <code> WeightUpdateRule </code>. A link connects two neurons in adjacent layers of a neural network 
 * on which values pass from some input neuron after potentially being multiplied by the given weight of 
 * the link to be stored with others as a sum in the activation variable of some output neuron.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationLink extends Link {

    /**
     * The error calculated by finding the product of the error from the output node and the input value. 
     */
    private double error;
    
    /**
     * The previous error that is determined when the current error changes. It is often used in training 
     * algorithms involving a momentum type term to update the rate at which the weight changes.
     */
    private double lastError;
    
    /**
     * The last change made to this link (last delta), sometimes used in algorithms momentum type terms.
     */
    private double lastChange;
    
    /**
     * A variable used to determine the magnitude at which the weight changes with respect to the error.
     */
    private double learningRate;
    
    /**
     * Changes the weight value of the link by a given amount and stores that change in the lastChange
     * variable, potentially to be used in algorithms momentum type terms.
     * @param delta the change in the weight
     * @see func.nn.Link#changeWeight(double)
     * @see BackPropagationLink#lastChange
     */
    public void changeWeight(double delta) {
         super.changeWeight(delta);
         lastChange = delta;
    }
    
    /**
     * Adds error to this link from the product of the value sent from the incoming
     * node and the error from the outgoing node.
     * @see BackPropagationLink#addError(double)
     * @see BackPropagationLink#getInValue()
     * @see BackPropagationLink#getOutError()
     */
    public void backpropagate() {
        addError(getInValue() * getOutError());
    }
    
    /**
     * Adds error to this link.
     * @param error the error to add
     * @see BackPropagationLink#error
     */
    public void addError(double error) {
        this.error += error;
    }
    
    /**
     * Clears out the error by setting it to zero and  
     * stores the current error in the last error variable.
     * @see BackPropagationLink#lastError
     * @see BackPropagationLink#error
     */
    public void clearError() {
        lastError = error;
        error = 0;
    }
    
    /**
     * Gets the error derivative with respect to this weight.
     * @return the error derivative value
     * @see BackPropagationLink#error
     */
    public double getError() {
        return error;
    }
    
    /**
     * Sets the error value based on a given double.
     * @param error the error to set
     * @see BackPropagationLink#error
     */
    public void setError(double error) {
    	this.error = error;
    }

    /**
     * Retrieves the last change in the weight often used in momentum terms of algorithms.
     * @return the last change in weight
     * @see BackPropagationLink#lastChange
     */
    public double getLastChange() {
        return lastChange;
    }

    /**
     * Retrieves the last error value often used in momentum terms of algorithms.
     * @return the last error value
     * @see BackPropagationLink#lastError
     */
    public double getLastError() {
        return lastError;
    }
    
    /**
     * Sets the learning rate to the given double.
     * @param learningRate the learning rate
     * @see BackPropagationLink#learningRate
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Retrieves the learning rate.
     * @return the learning rate
     * @see BackPropagationLink#learningRate
     */
    public double getLearningRate() {
        return learningRate;
    }
    
    /**
     * Retrieves the output error found in the input error variable of the output node.
     * @return the output error
     * @see BackPropagationNode#getInputError()
     * @see BackPropagationLink#getOutNode()
     */
    public double getOutError() {
        return ((BackPropagationNode) getOutNode()).getInputError();
    }
    
    /**
     * Retrieves the weighted output error which is the product of the input error
     * into the outgoing node and the weight of this link.
     * @return the output error times the weight of the link
     * @see BackPropagationNode#getInputError()
     * @see BackPropagationLink#getOutNode()
     * @see BackPropagationLink#getWeight()
     */
    public double getWeightedOutError() {
        return ((BackPropagationNode) getOutNode()).getInputError()
            * getWeight();
    }
    
    /**
     * Retrieves the input error of the input node.
     * @return the input error
     * @see BackPropagationNode#getInputError()
     * @see BackPropagationLink#getInNode()
     */
    public double getInError() {
        return ((BackPropagationNode) getInNode()).getInputError();
    }

    /**
     * Retrieves the weighted input error which is the product of the input nodes input error
     * and the weight if this link.
     * @return the weighted error
     * @see BackPropagationNode#getInputError()
     * @see BackPropagationLink#getInNode()
     * @see BackPropagationLink#getWeight()
     */
    public double getWeightedInError() {
        return ((BackPropagationNode) getInNode()).getInputError()
             * getWeight();
    }
}
