package opt.example;

import util.linalg.Vector;
import func.nn.NeuralNetwork;
import opt.EvaluationFunction;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;

/**
 * An evaluation function that uses a neural network
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class NeuralNetworkEvaluationFunction implements EvaluationFunction {
    /**
     * The network
     */
    private NeuralNetwork network;
    /**
     * The examples
     */
    private DataSet examples;
    /**
     * The error measure
     */
    private ErrorMeasure measure;
    
    /**
     * Make a new neural network evaluation function
     * @param network the network
     * @param examples the examples
     * @param measure the error measure
     */
    public NeuralNetworkEvaluationFunction(NeuralNetwork network,
            DataSet examples, ErrorMeasure measure) {
        this.network = network;
        this.examples = examples;
        this.measure = measure;
    }

    /**
     * @see opt.OptimizationProblem#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        // set the links
        Vector weights = d.getData();
        network.setWeights(weights);
        // calculate the error
        double error = 0;
        for (int i = 0; i < examples.size(); i++) {
            network.setInputValues(examples.get(i).getData());
            network.run();
            error += measure.value(new Instance(network.getOutputValues()), examples.get(i));
        }
        // the fitness is 1 / error
        return 1 / error;
    }

}
