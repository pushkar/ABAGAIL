package dist.hmm;

import shared.DataSet;
import shared.Instance;
import shared.Trainer;

/**
 * An implementation of the baum welch re estimation algorithm.
 * Takes in a hidden markov model and set of observation sequences,
 * then re estimates the parameters of the hidden markov model
 * based on expected values calculated through the use
 * of forward backward calculator
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class HiddenMarkovModelReestimator implements Trainer {
 
    /**
     * The array of observation sequences
     */
    private DataSet[] observationSequences;
    
    /**
     * The hidden markov model itself
     */
    private HiddenMarkovModel model;

    /**
     * The [k][t][i][j] value is the expected number of
     * transitions between i and j at time t given
     * observation sequence k
     */
    private double[][][][] transitionExpectations;
    
    /**
     * The [k][t][i] value is the expected number of
     * times in state i at time t given sequence k
     */
    private double[][][] stateExpectations;
    
    /**
     * The output observations
     */
    private DataSet outputObservations;
    
    /**
     * The transition observations
     */
    private DataSet transitionObservations;
    
    /**
     * The initial observations
     */
    private DataSet initialObservations;

    /**
     * Make a new reestimator
     * @param model the hidden markov model
     * @param observationSequences the observation sequencess
     * @param inputSequences the corresponding input sequences
     */
    public HiddenMarkovModelReestimator(HiddenMarkovModel model,
            DataSet[] observationSequences) {
        this.model = model;
        this.observationSequences = observationSequences;
		stateExpectations = new double[observationSequences.length][][];
		transitionExpectations = new double[observationSequences.length][][][];  
    	initializeObservations();
    }
    
    /**
     * Initialize the sequences used in training
     */
    public void initializeObservations() {
    	initializeOutputObservations();
    	initializeTransitionObservations();
    	initializeInitialObservations();
    }
    
    /**
     * Initialize the output observations
     *
     */
    public void initializeOutputObservations() {
		int totalTime = 0;
		for (int k = 0; k < observationSequences.length; k++) {
			totalTime += observationSequences[k].size();
		}
		Instance[] outputObservationsInstances = new Instance[totalTime];
		int j = 0;
		for (int k = 0; k < observationSequences.length; k++) {
            Instance[] cur = observationSequences[k].getInstances();
			System.arraycopy(cur, 0, outputObservationsInstances, j, cur.length);
			j += cur.length;
		}
        outputObservations = new DataSet(outputObservationsInstances,
            observationSequences[0].getDescription());
    }
    
    /**
     * Initialize the initial observations array
     */
    public void initializeInitialObservations() {
		Instance[] initialObservationsInstances = new Instance[observationSequences.length];
		  for (int k = 0; k < observationSequences.length; k++) {
			initialObservationsInstances[k] = observationSequences[k].get(0);
		}
        initialObservations = new DataSet(initialObservationsInstances,
            observationSequences[0].getDescription());
    }
    
    /**
     * Initialize the transition observations array
     */
    public void initializeTransitionObservations() {
		int totalTime = 0;	
		for (int k = 0; k < observationSequences.length; k++) {
			totalTime += observationSequences[k].size() - 1;
		}
		Instance[] transitionObservationsInstances = new Instance[totalTime];
		int j = 0;
		for (int k = 0; k < observationSequences.length; k++) {
            Instance[] cur = observationSequences[k].getInstances();
			System.arraycopy(cur, 1, transitionObservationsInstances, j, cur.length - 1);
			j += cur.length - 1;
		}
        transitionObservations = new DataSet(transitionObservationsInstances,
            observationSequences[0].getDescription());
    }
    
    /**
     * Restimate the model
     * @return the sum of log probabilities for the model / sequences
     */
    public double train() {
        double probability = 0;
        for (int k = 0; k < observationSequences.length; k++) {
            DataSet observationSequence = observationSequences[k];
            ForwardBackwardProbabilityCalculator fbc =
                new ForwardBackwardProbabilityCalculator(model, observationSequence);
            double[][] forwardProbabilities = fbc.calculateForwardProbabilities();
            double[][] backwardProbabilities = fbc.calculateBackwardProbabilities();
            stateExpectations[k] = calculateStateExpectations(
               observationSequence, forwardProbabilities,
               backwardProbabilities);
            transitionExpectations[k] = calculateTransitionExpectations(
               observationSequence, forwardProbabilities, 
               backwardProbabilities);         
           probability += fbc.calculateLogProbability();
        }
        reestimateInitialStateDistribution();
        reestimateTransitionDistributions();
        reestimateOutputDistributions();
        return probability / observationSequences.length;
    }
    
    /**
     * Calculate the transition probabilities for observation sequence k
     */
    public double[][][] calculateTransitionExpectations(
            DataSet observationSequence, double[][] forwardProbabilities,
            double[][] backwardProbabilities) {
        double[][][] transitions = new double[observationSequence.size() - 1]
            [model.getStateCount()][model.getStateCount()];
        for (int t = 0; t < observationSequence.size() - 1; t++) {
            double sum = 0;
            for (int i = 0; i < model.getStateCount(); i++) {
                for (int j = 0; j < model.getStateCount(); j++) {
                    transitions[t][i][j] = forwardProbabilities[t][i]
                       * model.transitionProbability(i, j, observationSequence.get(t + 1))
                       * model.observationProbability(j, observationSequence.get(t + 1))
                       * backwardProbabilities[t + 1][j];
                    sum += transitions[t][i][j];
                }
            }
            for (int i = 0; i < model.getStateCount(); i++) {
                for (int j = 0; j < model.getStateCount(); j++) {
                    transitions[t][i][j] /= sum;   
                }
            }
        }
        return transitions;
    }

    /**
     * Calculate the state probabilities for observation sequence k
     */
    public double[][] calculateStateExpectations(
            DataSet observationSequence, double[][] forwardProbabilities,
            double[][] backwardProbabilities) {
        double[][] states = new double[observationSequence.size()]
            [model.getStateCount()];
        for (int t = 0; t < observationSequence.size(); t++) {
            double sum = 0;
            for (int i = 0; i < model.getStateCount(); i++) {
                states[t][i] = forwardProbabilities[t][i] * 
                   backwardProbabilities[t][i];
                sum += states[t][i];
            }
            for (int i = 0; i < model.getStateCount(); i++) {
                states[t][i] /= sum;      
            }
        }
        return states;
    }
    
    /**
     * Reestimate the initial state probabilities
     */
    public void reestimateInitialStateDistribution() {
        double[][] initialStateProbabilities = new double[observationSequences.length]
             [model.getStateCount()];
        for (int k = 0; k < observationSequences.length; k++) {
            for (int i = 0; i < model.getStateCount(); i++) {
                initialStateProbabilities[k][i] = stateExpectations[k][0][i];
            }
        }
        model.estimateIntialStateDistribution(initialStateProbabilities, initialObservations);
    }


    /**
     * Reestimate the transition probabilities
     */
    public void reestimateTransitionDistributions() {
        double[][] probabilities = new double[transitionObservations.size()]
            [model.getStateCount()];
        for (int i = 0; i < model.getStateCount(); i++) {
            for (int j = 0; j < model.getStateCount(); j++) { 
                int counter = 0;
                for (int k = 0; k < observationSequences.length; k++) {
                    for (int t = 0; t < observationSequences[k].size() - 1; t++) {
                        probabilities[counter][j] = transitionExpectations[k][t][i][j];
                        counter++;   
                    }
                }     
            }
            model.estimateTransitionDistribution(i, probabilities, transitionObservations);
        }
    }


    /**
     * Reestimate the output probabilities
     */
    public void reestimateOutputDistributions() {
         for (int i = 0; i < model.getStateCount(); i++) {
            int counter = 0;
            for (int k = 0; k < observationSequences.length; k++) {
                for (int t = 0; t < observationSequences[k].size(); t++) {
                    observationSequences[k].get(t).setWeight(stateExpectations[k][t][i]);
                    counter++;   
                }
            }
            model.estimateOutputDistribution(i, outputObservations);
        }
    }

    /**
     * Get the model
     * @return returns the model
     */
    public HiddenMarkovModel getModel() {
        return model;
    }
    /**
     * Set the model}
     * @param model The model to set
     */
    public void setModel(HiddenMarkovModel model) {
        this.model = model;
    }
}
