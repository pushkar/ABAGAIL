package opt.example;

import dist.Distribution;

import opt.*;
import opt.ga.*;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;

import func.nn.NeuralNetwork;
import util.linalg.Vector;

/**
 * A class for performing neural network optimzation
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class NeuralNetworkOptimizationProblem implements HillClimbingProblem, GeneticAlgorithmProblem {

    /**
     * The evaluation function
     */
    private EvaluationFunction eval;
    /**
     * The cross over function
     */
    private CrossoverFunction crossover;
    /**
     * The neighbor function
     */
    private NeighborFunction neighbor;
    /**
     * The mutation function
     */
    private MutationFunction mutate;
    /**
     * The distribution
     */
    private Distribution dist;

    private double maxRandom;
    
    /**
     * Make a new neural network optimization
     * @param examples the examples
     * @param network the neural network
     * @param measure the error measure
     */
    public NeuralNetworkOptimizationProblem(DataSet examples,
             NeuralNetwork network, ErrorMeasure measure) {
        this(examples, network, measure, 1.0, 0.5);
    }

    /**
     * Make a new neural network optimization
     * @param examples the examples
     * @param network the neural network
     * @param measure the error measure
     * @param maxNeighborOffset the maximum offset for the neighbor function
     */
    public NeuralNetworkOptimizationProblem(DataSet examples,
                                            NeuralNetwork network,
                                            ErrorMeasure measure,
                                            double maxNeighborOffset,
                                            double maxRandom) {
        eval = new NeuralNetworkEvaluationFunction(network, examples, measure);
        crossover = new UniformCrossOver();
        neighbor = new ContinuousAddAllNeighbor(maxNeighborOffset);
        mutate = new ContinuousAddAllMutation(maxNeighborOffset);
        dist = new NeuralNetworkWeightDistribution(network.getLinks().size());
        this.maxRandom = maxRandom;
    }

    /**
     * @see opt.OptimizationProblem#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        return eval.value(d);
    }

    /**
     * @see opt.OptimizationProblem#random()
     */
    public Instance random() {
        Vector sampleWeights = dist.sample(null).getData();
        sampleWeights.times(2 * maxRandom);
        return new Instance(sampleWeights);
    }

    /**
     * @see opt.OptimizationProblem#neighbor(opt.Instance)
     */
    public Instance neighbor(Instance d) {
        return neighbor.neighbor(d);
    }
    

    /**
     * @see opt.GeneticAlgorithmProblem#mate(opt.Instance, opt.Instance)
     */
    public Instance mate(Instance da, Instance db) {
        return crossover.mate(da, db);
    }

    /**
     * @see opt.GeneticAlgorithmProblem#mutate(opt.Instance)
     */
    public void mutate(Instance d) {
        mutate.mutate(d);
    }

}
