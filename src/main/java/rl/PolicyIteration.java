package rl;

/**
 * A policy learner that learns policies through policy iteration
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class PolicyIteration implements PolicyLearner {
    /**
     * The tolerance for changes
     */
    private static final double TOLERANCE = 1E-6;
    /**
     * The policy
     */
    private Policy policy;
    /**
     * The process
     */
    private MarkovDecisionProcess process;
    /**
     * The decay value
     */
    private double gamma;

    
    /**
     * Make a new value iteration
     * @param gamma the gamma decay value
     */
    public PolicyIteration(double gamma, MarkovDecisionProcess process) {
        this.gamma = gamma;
        this.process = process;
        policy = new Policy(process.getStateCount(), process.getActionCount());
    }
    
    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        int stateCount = process.getStateCount();
        int actionCount = process.getActionCount();
        // perform value iteration with the policy
        double[] values = new double[stateCount];
        boolean valuesChanged = false;
        do {
            valuesChanged = false;
            // loop through all the states
            for (int i = 0; i < stateCount; i++) {
                // utility = reward if in terminal state
                if (process.isTerminalState(i)) {
                    values[i] = process.reward(i, 0);
                    continue;
                }
                // calculate the new value
                int action = policy.getAction(i);
                double actionVal = 0;
                for (int j = 0; j < stateCount; j++) {
                    actionVal += process.transitionProbability(i, j, action)
                            * values[j];
                }
                // val = reward + decay * expected value
                double val = process.reward(i, action) + gamma * actionVal;
                // check if we're done
                if (Math.abs(values[i] - val) > TOLERANCE) {
                    valuesChanged = true;
                }
                values[i] = val;
            }
        } while (valuesChanged);
        int changed = 0;
        // calculate the new policy
        for (int i = 0; i < stateCount; i++) {
            // find the maximum action
            double maxActionVal = -Double.MAX_VALUE;
            int maxAction = 0;
            for (int a = 0; a < actionCount; a++) {
                double actionVal = 0;
                for (int j = 0; j < stateCount; j++) {
                    actionVal += process.transitionProbability(i, j, a)
                            * values[j];
                }
                actionVal = process.reward(i, a) + gamma * actionVal;
                if (actionVal > maxActionVal) {
                    maxActionVal = actionVal;
                    maxAction = a;
                }
            }
            if (policy.getAction(i) != maxAction) {
                changed++;
            }
            policy.setAction(i, maxAction);
        }
        return changed;
    }
    /**
     * @see rl.PolicyLearner#getPolicy()
     */
    public Policy getPolicy() {
        return policy;
    }

}

