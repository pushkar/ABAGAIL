package dist.test;

import java.util.Random;

import dist.Distribution;
import dist.DiscreteDistribution;

import shared.DataSet;
import shared.Instance;

import dist.hmm.HiddenMarkovModelReestimator;
import dist.hmm.ForwardBackwardProbabilityCalculator;
import dist.hmm.SimpleHiddenMarkovModel;
import dist.hmm.StateSequenceCalculator;

/**
 * A simple coin flipping test
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class HMMRandomCoinTest {
    
    /**
     * The main method
     * @param args ignored
     */
    public static void main(String[] args) {
        // simple coin flip test
        SimpleHiddenMarkovModel model = new SimpleHiddenMarkovModel(2);
        model.setInitialStateProbabilities(new double[] {
            .5, .5
        });
        model.setOutputDistributions(new Distribution[] {
            new DiscreteDistribution(new double[] { .9, .1}),
            new DiscreteDistribution(new double[] { .9, .1})
        });
        model.setTransitionProbabilities(new double[][] {
            { .5, .5 },
            { .5, .5 }
        });
        Random random = new Random();
        Instance[] sequence = new Instance[1000];
        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = new Instance(random.nextInt(2));
        }
        DataSet[] sequences = new DataSet[] {
            new DataSet(sequence)
        };
        System.out.println(model + "\n");
        System.out.println("Observation Sequences: ");
        for (int i = 0; i < sequences.length; i++) {
            System.out.println(sequences[i]);
        }
        System.out.println();
        ForwardBackwardProbabilityCalculator fbc = new ForwardBackwardProbabilityCalculator(model, sequences[0]);
        System.out.println("Log Probability of first sequence: ");
        System.out.println(fbc.calculateLogProbability());
        System.out.println();
        StateSequenceCalculator vc = new StateSequenceCalculator(model, sequences[0]);
        int[] states = vc.calculateStateSequence();
        System.out.println("Most likely state sequence of first sequence: ");
        for (int i = 0; i < states.length; i++) {
            System.out.print(states[i] + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Reestimations of model based on sequences: ");
        HiddenMarkovModelReestimator bwr = new HiddenMarkovModelReestimator(model, sequences);
        bwr.train();
        System.out.println(model + "\n");
        bwr.train();
        System.out.println(model + "\n");      
        for (int i = 0; i < 1000; i++) {
            bwr.train();     
        }
        System.out.println(model + "\n");
        fbc = new ForwardBackwardProbabilityCalculator(model, sequences[0]);
        System.out.println("Log Probability of first sequence: ");
        System.out.println(fbc.calculateLogProbability());
    }
}
