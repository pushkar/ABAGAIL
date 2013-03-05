package func.nn.backprop;

import func.nn.Link;



/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationLink extends Link {

    /**
     * The derivative of the error function
     * in respect to this node, or in the case
     * of batch training possibly the sum
     * of derivative of the error functions for
     * many patterns.
     */
    private double error;
    
    /**
     * The last derivative of the error function
     * in respect to this node, sometimes
     * used in training algorithms that use
     * momentum type terms.
     */
    private double lastError;
    
    /**
     * The last change made to this link (last delta),
     * sometimes used in algorithms with momentum
     * type terms.
     */
    private double lastChange;
    
    /**
     * A learning rate term which is used in
     * some algorithms that have weight specific
     * learning rates.
     */
    private double learningRate;
    
    /**
     * @see nn.Link#changeWeight(double)
     */
    public void changeWeight(double delta) {
         super.changeWeight(delta);
         lastChange = delta;
    }
    
    /**
     * Backpropagate error values into this link
     */
    public void backpropagate() {
        addError(getInValue() * getOutError());
    }
    
    /**
     * Add error to this link
     * @param error the error to add
     */
    public void addError(double error) {
        this.error += error;
    }
    
    /**
     * Clear out the error and 
     * set the current error to be the last error
     */
    public void clearError() {
        lastError = error;
        error = 0;
    }
    
    /**
     * Get the error derivative with respect to this weight
     * @return the error derivative value
     */
    public double getError() {
        return error;
    }
    
    /**
     * Set the error
     * @param error the error to set
     */
    public void setError(double error) {
    	this.error = error;
    }

    /**
     * Get the last change in the weight
     * @return the last change in weight
     */
    public double getLastChange() {
        return lastChange;
    }

    /**
     * Get the last error value
     * @return the last error value
     */
    public double getLastError() {
        return lastError;
    }
    
    /**
     * Set the learning rate
     * @param learningRate the learning rate
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Get the learning rate
     * @return the learning rate
     */
    public double getLearningRate() {
        return learningRate;
    }
    
    /**
     * Get the output error
     * @return the output error
     */
    public double getOutError() {
        return ((BackPropagationNode) getOutNode()).getInputError();
    }
    
    /**
     * Get the weighted output error
     * @return the output error times the weigh tof the link
     */
    public double getWeightedOutError() {
        return ((BackPropagationNode) getOutNode()).getInputError()
            * getWeight();
    }
    
    /**
     * Get the input error
     * @return the input error
     */
    public double getInError() {
        return ((BackPropagationNode) getInNode()).getInputError();
    }

    /**
     * Get the weighted input error
     * @return the weighted error
     */
    public double getWeightedInError() {
        return ((BackPropagationNode) getInNode()).getInputError()
             * getWeight();
    }
}
