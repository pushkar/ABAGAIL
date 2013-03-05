package dist.hmm;

import shared.DataSet;

/**
 * A Viterbi algorithm implementation that
 * uses log probabilities to prevent underflow.
 * The Viterbi algorithm is used to determine the most
 * likely state sequence for a given sequence of observations.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class StateSequenceCalculator {

    /**
     * The hidden markov model being used
     */
    private HiddenMarkovModel model;
    
    /**
     * The observation sequence being used
     */
    private DataSet observations;

    /**
     * The [t][i] entry of this matrix represents
     * the maximum value of log P(q1, q2, ..., qt,
     * O1, O2, ..., Ot) for some chain of states
     * q1, q2, .... qt
     */
    private double[][] probabilities;
    
    /**
     * The [t][i] entry of this matrix represents
     * the q(t-1) value for max log P(q1, q2, ... qt,
     * O1, O2, ..., Ot) as calculated in the above
     * probabilities.  Through this matrix the chain
     * of q1, q2, ..., qt values can be reconstructed.
     */
    private int[][] chain;
    
    /**
     * Create a new state sequence calculator
     * @param model the hidden markov model
     * @param observations the observation sequence
     */
    public StateSequenceCalculator(HiddenMarkovModel model, 
            DataSet observations) {
       this.model = model;
       this.observations = observations;
    }
    
    /**
     * Calculate the most probable state sequence
     * @return the state sequence
     */
    public int[] calculateStateSequence() {
        probabilities = new double[observations.size()][model.getStateCount()];
        chain = new int[observations.size()][model.getStateCount()];
        calcuateForward();
        return calcuateBackward();
    }

    /**
     * Backtrack after calculating forward
     * @return the backward state path
     */
    private int[] calcuateBackward() {
        // find the largest at the end
        double max = Double.NEGATIVE_INFINITY;
        int argMax = Integer.MIN_VALUE;
        for (int i = 0; i < model.getStateCount(); i++) {
            if (probabilities[observations.size() - 1][i] > max) {
                max = probabilities[observations.size() - 1][i];
                argMax = i;
            }
        }
        // backtrack
        int[] states = new int[observations.size()];
        states[observations.size() - 1] = argMax;
        for (int t = observations.size() - 2; t >= 0; t--) {
            states[t] = chain[t + 1][states[t + 1]];
        }
        return states;
    }

    /**
     * Calculate the probabilites forward
     */
    private void calcuateForward() {
        // initial setup
        for (int i = 0; i < model.getStateCount(); i++) {
            probabilities[0][i] = 
                 Math.log(model.initialStateProbability(i, observations.get(0)))
                    + Math.log(model.observationProbability(i, observations.get(0)));
            chain[0][i] = 0;
        }
        // recursion
        for (int t = 1; t < observations.size(); t++) {
            for (int i = 0; i < model.getStateCount(); i++) {
                double max = Double.NEGATIVE_INFINITY;
                int argMax = Integer.MIN_VALUE;
                // find the most probable jump from t-1 to t, i
                for (int j = 0; j < model.getStateCount(); j++) {
                    double value = probabilities[t-1][j] + 
                        Math.log(model.transitionProbability(j, i, observations.get(t)));
                    if (value > max) {
                        max = value;
                        argMax = j;
                    }
                }
                probabilities[t][i] = max 
                    + Math.log(model.observationProbability(i, observations.get(t)));
                chain[t][i] = argMax;
            }
        }
    }
    
}
