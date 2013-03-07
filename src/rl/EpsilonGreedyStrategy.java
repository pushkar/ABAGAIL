package rl;

import dist.Distribution;

/**
 * An epsilon greedy exploration strategy
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class EpsilonGreedyStrategy implements ExplorationStrategy {
    /**
     * The epsilon value
     */
    private double epsilon;
    
    /**
     * Make a epsilon greedy strategy
     * @param epsilon the epsilon value
     */
    public EpsilonGreedyStrategy(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * @see rl.ExplorationStrategy#action(double[])
     */
    public int action(double[] qvalues) {
        if (Distribution.random.nextDouble() < epsilon) {
            return Distribution.random.nextInt(qvalues.length);
        }
        int best = 0;
        for (int i = 1; i < qvalues.length; i++) {
            if (qvalues[best] < qvalues[i]) {
                best = i;
            }
        }
        return best;
    }

}
