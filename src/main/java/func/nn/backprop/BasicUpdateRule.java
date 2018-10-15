package func.nn.backprop;

/**
 * Very basic update rule with no momentum
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BasicUpdateRule extends WeightUpdateRule {
    /**
     * The learning rate to use
     */
    private double learningRate;


    /**
     * Create a new basic update rule
     * @param learningRate the learning rate
     */
    public BasicUpdateRule(double learningRate) {
        this.learningRate = learningRate;            
    }
    
    /**
     * Create a new basic update rule
     */
    public BasicUpdateRule() {
        this(.01);
    }

    /**
     * @see nn.backprop.BackPropagationUpdateRule#update(nn.backprop.BackPropagationLink)
     */
    public void update(BackPropagationLink link) {
        link.changeWeight(-learningRate * link.getError());
    }

}
