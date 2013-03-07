package rl;


/**
 * A completely greedy strategy
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class GreedyStrategy {


    /**
     * @see rl.ExplorationStrategy#action(double[])
     */
    public int action(double[] qvalues) {
        int best = 0;
        for (int i = 1; i < qvalues.length; i++) {
            if (qvalues[best] < qvalues[i]) {
                best = i;
            }
        }
        return best;
    }
}
