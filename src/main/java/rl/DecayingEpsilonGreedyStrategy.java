package rl;

import dist.Distribution;

/**
 * An epsilon greedy exploration strategy
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DecayingEpsilonGreedyStrategy implements ExplorationStrategy {
    /**
     * The epsilon value
     */
    private double epsilon;
    /**
     * The decay value
     */
    private double decay;
    
    /**
     * Make a epsilon greedy strategy
     * @param epsilon the epsilon value
     * @param decay the decay value
     */
    public DecayingEpsilonGreedyStrategy(double epsilon, double decay) {
        this.epsilon = epsilon;
        this.decay = decay;
    }

    /**
     * @see rl.ExplorationStrategy#action(double[])
     */
    public int action(double[] qvalues) {
        if (Distribution.random.nextDouble() < epsilon) {
            return Distribution.random.nextInt(qvalues.length);
        }
        epsilon *= decay;
        int best = 0;
        for (int i = 1; i < qvalues.length; i++) {
            if (qvalues[best] < qvalues[i]) {
                best = i;
            }
        }
        return best;
    }

}
