package func.nn.backprop;

import shared.DataSet;
import shared.GradientErrorMeasure;
import shared.Instance;
import shared.filt.RandomOrderFilter;

import func.nn.NetworkTrainer;

/**
 * A standard batch back propagation trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class StochasticBackPropagationTrainer extends NetworkTrainer {
    
    /**
     * The weight update rule to use
     */
    private WeightUpdateRule rule;
    
    /**
     * Make a new back propagation trainer
     * @param patterns the patterns to train on
     * @param network the network to train
     * @param errorMeasure the error measure to use
     */
    public StochasticBackPropagationTrainer(DataSet patterns, 
            BackPropagationNetwork network, 
            GradientErrorMeasure errorMeasure,
            WeightUpdateRule rule) {
        super(patterns, network, errorMeasure);
        this.rule = rule;
    }

    /**
     * @see nn.Trainer#train()
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
