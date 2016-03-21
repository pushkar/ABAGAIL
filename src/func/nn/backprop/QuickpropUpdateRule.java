package func.nn.backprop;

/**
 * A subclass of <code> WeightUpdateRule </code> with an update rule from the Quickpropagation algorithm for updating 
 * the weight value using parabolic minimums between two points defined by two recent error changes and weight changes 
 * of a <code> BackPropagationLink </code> object used to pass values between <code> BackPropagationNode </code> 
 * objects that lie within two consecutive <code> BackPropagationLayer </code> objects of a <code> BackPropagationNetwork </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class QuickpropUpdateRule extends WeightUpdateRule {
    
    /**
     * The learning rate to use that determines the magnitude with which a link's weight values change with respect
     * to the error determined by each node.
     */
    private double learningRate;

    /**
     * Creates a new quickprop update rule object with a given learning rate that determines the magnitude
     * with which each link's weights are adjusted with respect to the error determined by each node.
     * @param learningRate the learning rate
     * @see BackPropagationLink
     * @see BackPropagationNode
     * @see QuickpropUpdateRule#QuickpropUpdateRule()
     * @see QuickpropUpdateRule#learningRate
     */
    public QuickpropUpdateRule(double learningRate) {
        this.learningRate = learningRate;
    }
    
    /**
     * Creates a new generic quickprop update rule with a learning rate of 0.2.
     * @see QuickpropUpdateRule#QuickpropUpdateRule(double)
     * @see QuickpropUpdateRule#learningRate
     */
    public QuickpropUpdateRule() {
    	this(.2);
    }

    /**
     * Updates a given link's weight value by using the basic update rule if the last error
     * of the link was zero (occurs on the first update) and by finding the error parabolically 
     * otherwise by treating the consecutive two error values of two different weights as endpoints 
     * of a secant line on a parabolic curve and determining its minimum.
     * @param link a back propagation link whose weight is to be updated
     * @see BackPropagationLink#getLastError()
     * @see BackPropagationLink#changeWeight(double)
     * @see BackPropagationLink#getError()
     * @see BackPropagationLink#getLastChange()
     * @see QuickpropUpdateRule#learningRate
     */
    public void update(BackPropagationLink link) {
        if (link.getLastError() == 0) {
            // the first run
            link.changeWeight(-learningRate * link.getError());
        } else {
            // jump to parabola min
            link.changeWeight(link.getError() 
                / (link.getLastError() - link.getError())
                    * link.getLastChange()
                - learningRate * link.getError());
        }
    }

}
