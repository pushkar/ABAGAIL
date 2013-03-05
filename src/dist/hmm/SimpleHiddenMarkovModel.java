package dist.hmm;

import shared.Instance;
import dist.DiscreteDistribution;

/**
 * A hidden markov model implementation that does not
 * use observations when calculating transition
 * probabilities and has no notion of input / output
 * In terms of this implementation it is a
 * HiddenMarkoModel that uses SimpleTransitionDistributions
 * for it's TransitionDistributions
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SimpleHiddenMarkovModel extends ModularHiddenMarkovModel {

	/**
	 * Create a new hidden markov model of the given size
	 * @param stateCount the number of states
	 */
	public SimpleHiddenMarkovModel(int stateCount) {
        super(stateCount);
        StateDistribution[] transitionDistributions =
            new StateDistribution[stateCount];
        for (int i = 0; i < stateCount; i++) {
            double[] probabilities = DiscreteDistribution.random(stateCount).getProbabilities();
            transitionDistributions[i] = new SimpleStateDistribution(probabilities);
        }
        setTransitionDistributions(transitionDistributions);
        double[] probabilities = DiscreteDistribution.random(stateCount).getProbabilities();
        setInitialStateDistribution(new SimpleStateDistribution(probabilities));
	}
    
    /** Default constructor */
    public SimpleHiddenMarkovModel() { }
    
    
    /**
     * Set the transition probability matrix
     * @param transitions matrix
     */
    public void setTransitionProbabilities(double[][] transitions) {
        StateDistribution[] transitionDistributions =
            new StateDistribution[getStateCount()];
        for (int i = 0; i < getStateCount(); i++) {
            transitionDistributions[i] = 
                new SimpleStateDistribution(transitions[i]);
        }
        setTransitionDistributions(transitionDistributions);
    }
    
    /**
     * Get the transition probability matrix
     * @return the transitions matrix
     */
    public double[][] getTransitionProbabilities() {
        double[][] probabilities = new double[getStateCount()][];
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = 
              ((SimpleStateDistribution) getTransitionDistributions()[i])
                  .getProbabilities();
        }
        return probabilities;
    }
    
    /**
     * Set the initial state probabilities
     * @param probabilities the probabilites
     */
    public void setInitialStateProbabilities(double[] probabilities) {
        setInitialStateDistribution(new SimpleStateDistribution(probabilities));
    }
    
    /**
     * Get the initial state probabilites
     * @return the probabilities
     */
    public double[] getInitialStateProbabilities() {
        return ((SimpleStateDistribution) getInitialStateDistribution())
            .getProbabilities();
    }
    
    /**
     * Match the initial state distribution to the given expectations 
     * for the initial observation data
     * @param expectations [k][i] is the expected times in state i initially
     * for observation sequence k
     */
    public void estimateIntialStateDistribution(
            double[][] expectations) {
        estimateIntialStateDistribution(expectations, null);    
    }
    
    /**
     * Match the transitions in state i to the given expectations for the
     * transition observation data
     * @param i the start state
     * @param expectations the expected transitions [t][j] for time t to state j
     * @param sequence the transition observatiosn
     * @param sequence the observation sequence
     */
    public void estimateTransitionDistribution(
            int i, double[][] expectations) {
        estimateTransitionDistribution(i, expectations, null);        
    }
    
    /**
     * Get the initial state probability
     * @param i the initial state
     * @return the probability
     */
    public double initialStateProbability(int i) {
        return initialStateProbability(i, null);
    }
    
    /**
     * Get the probability of transitioning from state i to state j.
     * @param i the first state
     * @param j the second state
     * @return the probability
     */
    public double transitionProbability(int i, int j) {
        return transitionProbability(i, j, null);
    }
    /**
     * Sample a next state given the current state and input
     * @param i the current state
     * @return the next state
     */
    public int sampleState(int i) {
        return sampleState(i, null);
    }
    
    /**
     * Sample the initial state given the current state
     * @return the initial state
     */
    public int sampleInitialState() {
        return sampleInitialState(null);
    }
    
    /**
     * Sample an input given the current observation
     * @param i the current state
     * @return the sample observation
     */
    public Instance sampleObservation(int i) {
        return sampleObservation(i, null);
    }
}
