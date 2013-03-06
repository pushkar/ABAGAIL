package rl;

import dist.Distribution;
import util.ABAGAILArrays;

/**
 * A policy maps states to actions
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class Policy {
    /**
     * The actions to perform in each state
     */
    private int[] actions;
  
    /**
     * Make a new policy
     * @param actions the actions
     */
    public Policy(int[] actions) {
        this.actions = actions;
    }
    /**
     * Make a new random policy
     * @param numStates the number of states
     * @param numActions the number of actions
     */
    public Policy(int numStates, int numActions) {
        actions = new int[numStates];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = Distribution.random.nextInt(numActions);
        }
    }

    /**
     * Get the action for the given state
     * @param state the state
     * @return the action
     */
    public int getAction(int state) {
        return actions[state];
    }
    /**
     * Set the action for a state
     * @param state the state
     * @param action the action
     */
    public void setAction(int state, int action) {
        actions[state] = action;
    }
    /**
     * Get the actions
     * @return returns the actions.
     */
    public int[] getActions() {
        return actions;
    }
    /**
     * Set the actions
     * @param actions the actions to set.
     */
    public void setActions(int[] actions) {
        this.actions = actions;
    }
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
       return ABAGAILArrays.toString(actions); 
    }

}
