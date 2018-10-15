package dist.hmm;

import shared.DataSet;
import shared.Instance;


/**
 * An interface for state probalility functions
 * that represent the probabilty of transitioning to
 * a state and also the probability of starting in a state
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface StateDistribution {
    
    /**
     * Get the probability of the next state
     * @param nextState the next state
     * @param observ the observation
     * @return the probability
     */
    public abstract double p(int nextState, Instance observ);
    
    /**
     * Generate the next state
     * @param o the observation
     * @return the next state
     */
    public abstract int generateRandomState(Instance o);
    
    /**
     * Generate the most likely next state
     * @param o the observation
     * @return the next state
     */
    public abstract int mostLikelyState(Instance o);  
    
    /**
     * Match the given expectations and observations
     * @param expectations entry [k][j] is the probability of transitioning
     * from this state to state j correpsonding to observation k, k can be
     * seen as kind of like t all though it is not in practice since
     * observations is many sequences glued together
     * @param sequence the sequence of corresponding observations
     */ 
    public abstract void estimate(double[][] expectations, DataSet sequence);
}