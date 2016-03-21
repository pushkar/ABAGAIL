package func.nn.feedfwd;

import func.nn.LayeredNetwork;

/**
 * A <code> LayeredNetwork </code> object that feeds values through layers from the input layer to the output layer
 * along <code> Link </code> objects connecting <code> FeedForwardNode </code> objects within each <code> Layer </code> object.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FeedForwardNetwork extends LayeredNetwork {

    /**
     * Asks each layer of the network starting with the first hidden layer to call forward values from the previous layer until 
     * they reach the output layer.
     * @see func.nn.NeuralNetwork#run()
     * @see LayeredNetwork#getHiddenLayerCount()
     * @see LayeredNetwork#getHiddenLayer(int)
     * @see LayeredNetwork#getOutputLayer()
     * @see FeedForwardLayer#feedforward()
     */
    public void run() {
        for (int i = 0; i < getHiddenLayerCount(); i++) {
            ((FeedForwardLayer) getHiddenLayer(i)).feedforward();
        }
        ((FeedForwardLayer) getOutputLayer()).feedforward();
    }
    
    
}
