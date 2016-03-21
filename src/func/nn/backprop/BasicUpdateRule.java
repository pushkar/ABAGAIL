package func.nn.backprop;

/**
 * A subclass of <code> WeightUpdateRule </code> with a very basic update rule involving no momentum for
 * updating the weight value of a <code> BackPropagationLink </code> object used to pass values between 
 * <code> BackPropagationNode </code> objects that lie within two consecutive <code> BackPropagationLayer </code>
 * objects of a <code> BackPropagationNetwork </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BasicUpdateRule extends WeightUpdateRule {
    /**
     * The learning rate to use that determines the magnitude with which a link's weight values change with respect
     * to the error determined by each node.
     * @see BackPropagationLink
     * @see BackPropagationNode 
     */
    private double learningRate;


    /**
     * Creates a new basic update rule object with a given learning rate that determines the magnitude
     * with which each link's weights are adjusted with respect to the error determined by each node.
     * @param learningRate the learning rate
     * @see BackPropagationLink
     * @see BackPropagationNode
     * @see BasicUpdateRule#BasicUpdateRule()
     * @see BasicUpdateRule#learningRate
     */
    public BasicUpdateRule(double learningRate) {
        this.learningRate = learningRate;            
    }
    
    /**
     * Creates a new generic basic update rule with a learning rate of 0.01.
     * @see BasicUpdateRule#BasicUpdateRule(double)
     * @see BasicUpdateRule#learningRate
     */
    public BasicUpdateRule() {
        this(.01);
    }

    /**
     * Updates a given link's weight value by adding to it the product of the opposite of
     * the learning rate and the link's current error.
     * @param link a back propagation link whose weight is to be updated
     * @see BackPropagationLink#changeWeight(double)
     * @see BackPropagationLink#getError()
     * @see BasicUpdateRule#learningRate
     */
    public void update(BackPropagationLink link) {
        link.changeWeight(-learningRate * link.getError());
    }

}
