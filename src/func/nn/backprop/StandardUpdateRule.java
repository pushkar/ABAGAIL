package func.nn.backprop;

/**
 * A subclass of <code> WeightUpdateRule </code> that is a standard update rule involving a learning rate and a 
 * momentum term for updating the weight value of a <code> BackPropagationLink </code> object used to pass values 
 * between <code> BackPropagationNode </code> objects that lie within two consecutive <code> BackPropagationLayer </code> 
 * objects of a <code> BackPropagationNetwork </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class StandardUpdateRule extends WeightUpdateRule {
    
    /**
     * The learning rate to use that determines the magnitude with which a link's weight values change with respect
     * to the error determined by each node.
     * @see BackPropagationLink
     * @see BackPropagationNode 
     */
    private double learningRate;
    
    /**
     * The momentum to use that determines an additional magnitude with which the link's weight values change
     * depending on the previous weight change in order to help the network keep from becoming as easily
     * stuck in a local minimum.
     * @see BackPropagationLink
     * @see BackPropagationNode
     * @see BackPropagationNetwork
     */
    private double momentum;

    /**
     * Creates a new standard update rule object with a given learning rate that determines the magnitude
     * with which each link's weights are adjusted with respect to the error determined by each node and a 
     * given momentum that adjusts the weights an additional amount based on the most recent weight change.
     * @param learningRate the learning rate
     * @param momentum the momentum rate
     * @see BackPropagationLink
     * @see BackPropagationNode
     * @see StandardUpdateRule#StandardUpdateRule()
     * @see StandardUpdateRule#learningRate
     * @see StandardUpdateRule#momentum
     */
    public StandardUpdateRule(double learningRate, double momentum) {
        this.momentum = momentum;
        this.learningRate = learningRate;            
    }
    
    /**
     * Creates a new standard update rule with a learning rate of 0.2 and momentum of 0.9.
     * @see StandardUpdateRule#StandardUpdateRule(double,double)
     * @see StandardUpdateRule#learningRate
     */
    public StandardUpdateRule() {
    	this(.2, .9);
    }

    /**
     * Updates a given link's weight value by adding to it the product of the opposite of
     * the learning rate with the link's current error and the product of the link's
     * last weight change with the momentum value.
     * @param link a back propagation link whose weight is to be updated
     * @see BackPropagationLink#changeWeight(double)
     * @see BackPropagationLink#getError()
     * @see BackPropagationLink#getLastChange()
     * @see StandardUpdateRule#learningRate
     * @see StandardUpdateRule#momentum
     */
    public void update(BackPropagationLink link) {
        link.changeWeight(-learningRate * link.getError()
            + link.getLastChange() * momentum);
    }

}
