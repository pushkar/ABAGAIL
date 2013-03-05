package func.nn.backprop;

/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class StandardUpdateRule extends WeightUpdateRule {
    
    /**
     * The learning rate to use
     */
    private double learningRate;
    
    /**
     * The momentum to use
     */
    private double momentum;

    /**
     * Create a new standard momentum update rule
     * @param learningRate the learning rate
     * @param momentum the momentum
     */
    public StandardUpdateRule(double learningRate, double momentum) {
        this.momentum = momentum;
        this.learningRate = learningRate;            
    }
    
    /**
     * Create a new standard update rule
     */
    public StandardUpdateRule() {
    	this(.2, .9);
    }

    /**
     * @see nn.backprop.BackPropagationUpdateRule#update(nn.backprop.BackPropagationLink)
     */
    public void update(BackPropagationLink link) {
        link.changeWeight(-learningRate * link.getError()
            + link.getLastChange() * momentum);
    }

}
