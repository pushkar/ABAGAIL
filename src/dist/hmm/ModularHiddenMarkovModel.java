package dist.hmm;



import dist.Distribution;

import shared.Copyable;
import shared.DataSet;
import shared.Instance;

/**
 * The abstract base class representing a hidden markov model
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ModularHiddenMarkovModel implements HiddenMarkovModel, Copyable {
    
    /**
     * The array of transition probability functions
     */
    private StateDistribution[] transitionDistributions;

    /**
     * The array of output probability functions
     */
    private Distribution[] outputDistributions;
    
    /**
     * The initial state probability distribution
     */
    private StateDistribution initialStateDistribution;
 
    /**
     * Create a new hidden markov model of the given size
     * @param stateCount the number of states
     */
    public ModularHiddenMarkovModel(int stateCount) {
        initialStateDistribution = null;
        transitionDistributions = new StateDistribution[stateCount];
        outputDistributions = new Distribution[stateCount];
    }
    
    /** Default constructor */
    public ModularHiddenMarkovModel() { }
    
    /**
     * Get the state count for this model
     * @return the number of states in the model
     */
    public int getStateCount() {
        return transitionDistributions.length;
    }

    /**
     * Set the output functions
     * @param functions the output functions
     */
    public void setOutputDistributions(Distribution[] functions) {
        outputDistributions = functions;
    }
    
    /**
     * Get the output distributions
     * @return the distributions;
     */
    public Distribution[] getOutputDistributions() {
        return outputDistributions;
    }

    /**
     * Set the transition functions
     * @param functions the transition functions
     */
    public void setTransitionDistributions(StateDistribution[] functions) {
        transitionDistributions = functions;
    }
    
    /**
     * Get the transition distributions
     * @return the distributions
     */
    public StateDistribution[] getTransitionDistributions() {
        return transitionDistributions;
    }
    
    
    /**
     * Set the intial state distribution
     * @param distribution the distribution
     */
    public void setInitialStateDistribution(StateDistribution distribution) {
        initialStateDistribution = distribution;
    }
    
    /**
     * Get the initial state distribution
     * @return the initial state distribution
     */
    public StateDistribution getInitialStateDistribution() {
        return initialStateDistribution;
    }
    
    /**
     * Get the initial state probability
     * @param i the initial state
     * @param o the initial observation
     * @return the probability
     */
    public double initialStateProbability(int i, Instance o) {
        return initialStateDistribution.p(i, o);
    }

    /**
     * Get the probability of transitioning from state i to state j,
     * with observation o
     * @param i the first state
     * @param j the second state
     * @param o the observation at state i
     * @return the probability
     */
    public double transitionProbability(int i, int j, Instance o) {
        return transitionDistributions[i].p(j, o);
    }
    
    /**
     * Get the probability of observing o in state i
     * @param i the current state
     * @param o the observation
     * @return the probability
     */
    public double observationProbability(int i, Instance o) {
        return outputDistributions[i].p(o);
    }
    
    /**
     * @see dist.hmm.HiddenMarkovModel#sampleState(int, shared.Instance)
     */
    public int sampleState(int i, Instance o) {
        return transitionDistributions[i].generateRandomState(o);
    }

    /**
     * @see dist.hmm.HiddenMarkovModel#sampleInitialState(shared.Instance)
     */
    public int sampleInitialState(Instance o) {
        return initialStateDistribution.generateRandomState(o);
    }

    /**
     * @see dist.hmm.HiddenMarkovModel#sampleObservation(int, shared.Instance)
     */
    public Instance sampleObservation(int i, Instance o) {
        return outputDistributions[i].sample(o);
    }
    
    /**
     * Match the outputs in state i to the given expectaions for the given 
     * sequence
     * @param i the state 
     * @param expectations the expectations
     * @param sequence the sequence
     */
    public void estimateOutputDistribution(int i, DataSet sequence) {
       outputDistributions[i].estimate(sequence); 
    }
    
    /**
     * Match the initial state distribution to the given expectations and observations
     * @param expectations [k][i] is the expected times in state i initially
     * for observation sequence k
     * @param observations the observation sequence
     */
    public void estimateIntialStateDistribution(double[][] expectations, DataSet observations) {
        initialStateDistribution.estimate(expectations, observations);
    }
    
    /**
     * Match the transitions in state i to the given expectations for the given
     * sequence
     * @param i the start state
     * @param expectations the expected transitions [t][j] for time t to state j
     * @param sequence the observation sequence
     */
    public void estimateTransitionDistribution(int i, double[][] expectations, DataSet sequence) {
        transitionDistributions[i].estimate(expectations, sequence);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
       String result = "";
       result += "Transition Distributions\n";
       for (int i = 0; i < getStateCount(); i++) {
           result += transitionDistributions[i] + "\n"; 
       }
       result += "Output Distributions\n";
       for (int i = 0; i < getStateCount(); i++) {
           result += outputDistributions[i] + "\n"; 
       }
       result += "Initial Distribution\n" 
          + initialStateDistribution + "\n";
       return result;
    }

    /**
     * @see shared.Copyable#copy()
     */
    public Copyable copy() {
        ModularHiddenMarkovModel copy = new ModularHiddenMarkovModel();
        copy.setInitialStateDistribution((StateDistribution) ((Copyable) initialStateDistribution).copy());
        StateDistribution[] transitionCopies = new StateDistribution[transitionDistributions.length];
        for (int i = 0; i < transitionCopies.length; i++) {
            transitionCopies[i] = (StateDistribution) ((Copyable) transitionDistributions[i]).copy();
        }
        copy.setTransitionDistributions(transitionCopies);
        Distribution[] outputCopies = new Distribution[outputDistributions.length];
        for (int i = 0; i < outputCopies.length; i++) {
            outputCopies[i] = (Distribution) ((Copyable) outputDistributions[i]).copy();
        }
        copy.setOutputDistributions(outputCopies);
        return copy;
    }



}
