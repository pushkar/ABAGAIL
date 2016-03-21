package func.nn.backprop;

import shared.DataSet;
import shared.ErrorMeasure;
import shared.GradientErrorMeasure;
import shared.Instance;
import shared.filt.RandomOrderFilter;

import func.nn.NetworkTrainer;
import func.nn.NeuralNetwork;

/**
 * An stochastic back propagation trainer for a neural network based on a given <code> DataSet </code>, 
 * <code> BackPropagationNeuralNetwork </code>, and an <code> ErrorMeasure </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class StochasticBackPropagationTrainer extends NetworkTrainer {
    
    /**
     * The link weight update rule to use.
     * @see WeightUpdateRule
     */
    private WeightUpdateRule rule;
    
    /**
     * Creates a new stochastic back propagation trainer given a data set to train on, a network to train, 
     * a method of error measure to reduce, and a rule for updating the weights.
     * @param patterns the data set to train on
     * @param network the network to train by adjusting its weights
     * @param errorMeasure the error measure to use
     * @param rule the weight update rule
     * @see DataSet
     * @see BackPropagationNetwork
     * @see GradientErrorMeasure
     * @see WeightUpdateRule
     * @see NetworkTrainer#NetworkTrainer(DataSet, NeuralNetwork, ErrorMeasure)
     */
    public StochasticBackPropagationTrainer(DataSet patterns, 
            BackPropagationNetwork network, 
            GradientErrorMeasure errorMeasure,
            WeightUpdateRule rule) {
        super(patterns, network, errorMeasure);
        this.rule = rule;
    }

    /**
     * Trains the neural network to predict the output labels of the data by running the data
     * through the network in random order and comparing each prediction with each true value 
     * using the provided error measure. It then back propagates the error, updates the weights 
     * and clears the error as each data instance passes through before running the next one.
     * @return the mean error for the data instances
     * @see shared.Trainer#train()
     * @see BackPropagationNetwork#setInputValues(util.linalg.Vector)
     * @see BackPropagationNetwork#run()
     * @see BackPropagationNetwork#setOutputErrors(double[])
     * @see BackPropagationNetwork#backpropagate()
     * @see BackPropagationNetwork#updateWeights(WeightUpdateRule)
     * @see BackPropagationNetwork#clearError()
     * @see shared.GradientErrorMeasure#gradient(Instance, Instance)
     * @see shared.GradientErrorMeasure#value(Instance, Instance)
     * @see shared.DataSet
     * @see Instance
     * @see shared.Trainer#train()
     */
    public double train() {
        BackPropagationNetwork network =
            (BackPropagationNetwork) getNetwork();
        GradientErrorMeasure measure =
            (GradientErrorMeasure) getErrorMeasure();
        DataSet patterns = getDataSet();
        RandomOrderFilter randomizer = new RandomOrderFilter();
        randomizer.filter(patterns);
        double error = 0;
        for (int i = 0; i < patterns.size(); i++) {
            Instance pattern = patterns.get(i);
            network.setInputValues(pattern.getData());
            network.run();
            Instance output = new Instance(network.getOutputValues());
            double[] errors = measure.gradient(output, pattern);
            error += measure.value(output, pattern);
            network.setOutputErrors(errors);
            network.backpropagate();
            network.updateWeights(rule);
            network.clearError();
        }
        return error / patterns.size();
    }
    

}
