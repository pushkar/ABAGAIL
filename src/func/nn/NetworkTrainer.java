package func.nn;

import shared.DataSet;
import shared.ErrorMeasure;
import shared.Trainer;

/**
 * A class that represents a trainer for
 * a neural network
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class NetworkTrainer implements Trainer {
    
    /**
     * The patterns that are being trained on
     */
    private DataSet patterns;
    
    /**
     * The network being trained
     */
    private NeuralNetwork network;
    
    /**
     * The error measure to use in training
     */
    private ErrorMeasure errorMeasure;
    
    /**
     * Make a new network trainer
     * @param patterns the patterns
     * @param network the network
     */
    public NetworkTrainer(DataSet patterns, NeuralNetwork network,
            ErrorMeasure errorMeasure) {
        this.patterns = patterns;
        this.network = network;
        this.errorMeasure = errorMeasure;
    }

    /**
     * Get the network
     * @return the network
     */
    public NeuralNetwork getNetwork() {
        return network;
    }
    
    /**
     * Get the error measure to use when training
     * @return the error measure
     */
    public ErrorMeasure getErrorMeasure() {
        return errorMeasure;
    }

    /**
     * Get the patterns
     * @return the pattern
     */
    public DataSet getDataSet() {
        return patterns;
    }
    

}
