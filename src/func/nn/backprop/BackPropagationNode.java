

package func.nn.backprop;

import func.nn.Link;
import func.nn.activation.DifferentiableActivationFunction;
import func.nn.feedfwd.FeedForwardNode;

/**
 * A back propagation node that extends the capabilities of <code> FeedForwardNode </code> objects by
 * allowing error to be tracked with respect to the <code> Neuron </code> objects in the following <code> Layer </code>, 
 * if it exists. These nodes also contain some <code> DifferentiableActivationFunction </code> used in back propagation.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BackPropagationNode extends FeedForwardNode {

    /**
     * The error calculated by taking the derivative of the weighted inputs from nodes in the previous
     * layer multiplied by the output error.
     */
    private double inputError;
    
    /**
     * The sum of the weighted error with respect to the output links of this node. If
     * there are no output links, then it is typically the difference between it's output
     * (the activation value) and the expected output.
     * @see BackPropagationLink
     * @see BackPropagationLayer
     */
    private double outputError;
    
	/**
	 * Creates a new back propagation node with a differentiable activation function
	 * that can be used to calculate errors to back propagate. 
	 * @param function the differentiable activation function
	 * @see DifferentiableActivationFunction
	 * @see FeedForwardNode#FeedForwardNode(func.nn.activation.ActivationFunction)
	 */
	public BackPropagationNode(DifferentiableActivationFunction function) {
	    super(function);
    }

    /**
     * Back propagates error values. For nodes that have output links, the method first
     * calculates the sum of the weighted errors of the nodes in the following output layer
     * and stores that in the outputError variable. It then calculates the weighted input sum
     * and determines the derivative of this value multiplied by the outputError before
     * storing the result in the inputError variable. If the node has no output links, it instead 
     * simply copies the current output value that is likely set by the BackPropogationNetwork into
     * the inputError variable instead.
     * @see BackPropagationLink#getWeightedOutError()
     * @see DifferentiableActivationFunction
     * @see DifferentiableActivationFunction#derivative(double)
     * @see func.nn.Neuron#getOutLinkCount()
     * @see func.nn.Neuron#getOutLink(int)
     * @see func.nn.feedfwd.FeedForwardNode#getActivationFunction()
     * @see func.nn.feedfwd.FeedForwardNode#getWeightedInputSum()
     * @see BackPropagationNode#setOutputError(double)
     * @see BackPropagationNode#setInputError(double)
     * @see BackPropagationNode#getOutputError()
     */
	public void backpropagate() {
        if  (getOutLinkCount() > 0) {
            double weightedErrorSum = 0;
            for (int i = 0; i < getOutLinkCount(); i++) {
                BackPropagationLink outLink = 
                    (BackPropagationLink) getOutLink(i);
                weightedErrorSum += outLink.getWeightedOutError();
            }
            setOutputError(weightedErrorSum);
            DifferentiableActivationFunction act =
                (DifferentiableActivationFunction) getActivationFunction();
            setInputError(act.derivative(getWeightedInputSum()) * getOutputError());
        } else {
            setInputError(getOutputError());
        }
    }

    /**
     * Back propagates error into the incoming links which add to their total error
     * the product of this node's inputError value by their own input value.
     * @see func.nn.Neuron#getInLinkCount()
     * @see func.nn.Neuron#getInLink(int)
     * @see func.nn.backprop.BackPropagationLink#backpropagate()
     */
    public void backpropagateLinks() {
        for (int i = 0; i < getInLinkCount(); i++) {
            BackPropagationLink inLink = 
                (BackPropagationLink) getInLink(i); 
            inLink.backpropagate();
        }
    }

    /**
     * Updates the incoming links' weights with the given rule.
     * @param rule the rule to use
     * @see WeightUpdateRule#update(BackPropagationLink)
     * @see func.nn.Neuron#getInLinkCount()
     * @see func.nn.Neuron#getInLink(int)
     */
    public void updateWeights(WeightUpdateRule rule) {
        for (int i = 0; i < getInLinkCount(); i++) {
            BackPropagationLink inLink = 
                (BackPropagationLink) getInLink(i); 
            rule.update(inLink);
        }
    }

    /**
     * Sets the error for this node with respect to
     * its output, likely when compared to some expected value.
     * @param error the new error value
     */
    public void setOutputError(double error) {
        outputError = error;
    }
    
    /**
     * Retrieves the output error for this node.
     * @return the output error
     * @see BackPropagationNode#outputError
     */
    public double getOutputError() {
        return outputError;
    }

    /**
     * Retrieves the input error of this node.
     * @return the input error
     * @see BackPropagationNode#inputError
     */
    public double getInputError() {
        return inputError;
    }
    
    /**
     * Sets the input error value. The method is generally used in the backpropagate() method 
     * to set the error with respect to the weighted input of the node.
     * @param error the error
     * @see BackPropagationNode#backpropagate()
     */
    public void setInputError(double error) {
        inputError = error;
    }
        
    /**
     * Clears all of the error for the incoming links by setting them to 0. The current error
     * is saved, however, in the lastError variable.
     * @see func.nn.Neuron#getInLinkCount()
     * @see func.nn.Neuron#getInLink(int)
     * @see BackPropagationLink#clearError()
     */
    public void clearError() {
        for (int i = 0; i < getInLinkCount(); i++) {
            BackPropagationLink inLink = 
                (BackPropagationLink) getInLink(i); 
            inLink.clearError();
        }
    }

    
    /**
     * Creates a back propagation link that is typically connected to some neuron in
     * the previous or following layer.
     * @see func.nn.Neuron#createLink()
     * @see BackPropagationLink#BackPropagationLink()
     */
    public Link createLink() {
        return new BackPropagationLink();
    }

}
