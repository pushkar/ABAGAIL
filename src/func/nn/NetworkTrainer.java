package func.nn;

import shared.DataSet;
import shared.ErrorMeasure;
import shared.Trainer;

/**
 * An abstract class that represents a trainer for a neural network based on a given <code> DataSet </code>, 
 * <code> NeuralNetwork </code>, and an <code> ErrorMeasure </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class NetworkTrainer implements Trainer {
    
    /**
     * The data set, having attributes and labels, that a network is trained on.
     * @see DataSet 
     */
    private DataSet patterns;
    
    /**
     * The network being trained by adjusting the weights of its links.
     * @see NeuralNetwork
     */
    private NeuralNetwork network;
    
    /**
     * The error measure to use in training that the network intends to reduce.
     * @see ErrorMeasure
     */
    private ErrorMeasure errorMeasure;
    
    /**
     * Creates a new network trainer given a data set to train on, a network to train, and a method of error measure to reduce.
     * @param patterns the data set to train on
     * @param network the network to train by adjusting its weights
     * @param errorMeasure the error measure to use
     * @see DataSet
     * @see NeuralNetwork
     * @see ErrorMeasure
     */
    public NetworkTrainer(DataSet patterns, NeuralNetwork network,
            ErrorMeasure errorMeasure) {
        this.patterns = patterns;
        this.network = network;
        this.errorMeasure = errorMeasure;
    }

    /**
     * Retrieves the network being trained.
     * @return the network
     * @see NeuralNetwork
     */
    public NeuralNetwork getNetwork() {
        return network;
    }
    
    /**
     * Retrieves the error measure used when training.
     * @return the error measure
     * @see ErrorMeasure
     */
    public ErrorMeasure getErrorMeasure() {
        return errorMeasure;
    }

    /**
     * Retrieves the data set.
     * @return the data set
     * @see DataSet
     */
    public DataSet getDataSet() {
        return patterns;
    }
    

}
