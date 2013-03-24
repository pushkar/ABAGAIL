package opt.example;

import dist.AbstractDistribution;

import shared.DataSet;
import shared.Instance;

/**
 * A distribution for neural network weights
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class NeuralNetworkWeightDistribution extends AbstractDistribution {
   
    /** 
     * The weight count
     */ 
    private int weightCount;
    
    /**
     * Make a new neural network weight distribution
     * @param weightCount the weight count
     */
    public NeuralNetworkWeightDistribution(int weightCount) {
        this.weightCount = weightCount;
    }

    /**
     * @see dist.Distribution#probabilityOf(shared.Instance)
     */
    public double p(Instance i) {
        return 1;
    }

    /**
     * @see dist.Distribution#generateRandom(shared.Instance)
     */
    public Instance sample(Instance ignored) {
        double[] weights = new double[weightCount];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextDouble() - .5;
        }
        return new Instance(weights);
    }

    /**
     * @see dist.Distribution#generateMostLikely(shared.Instance)
     */
    public Instance mode(Instance ignored) {
        return sample(ignored);
    }

    /**
     * @see dist.Distribution#estimate(shared.DataSet)
     */
    public void estimate(DataSet observations) {
        return;
    }
    
    

}
