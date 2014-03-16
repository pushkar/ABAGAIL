package opt.example;

import dist.Distribution;

import opt.ContinuousAddOneNeighbor;
import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.ga.ContinuousAddOneMutation;
import opt.ga.UniformCrossOver;
import opt.ga.CrossoverFunction;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;

import func.nn.NeuralNetwork;

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
    
    /**
     * Make a new neural network optimization
     * @param examples the examples
     * @param network the neural network
     * @param measure the error measure
     */
    public NeuralNetworkOptimizationProblem(DataSet examples,
             NeuralNetwork network, ErrorMeasure measure) {
        eval = new NeuralNetworkEvaluationFunction(network, examples, measure);
        crossover = new UniformCrossOver();
        neighbor = new ContinuousAddOneNeighbor();
        mutate = new ContinuousAddOneMutation();
        dist = new NeuralNetworkWeightDistribution(network.getLinks().size());
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
        return dist.sample(null);
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
