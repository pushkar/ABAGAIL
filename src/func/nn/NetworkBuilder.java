package func.nn;
import func.nn.activation.ActivationFunction;
import func.nn.feedfwd.FeedForwardNetwork;
import shared.DataSet;
import shared.ErrorMeasure;

/**
 * Interface for neural network builder
 * @author John Mansfield
 * @version 1.0
 */

public interface NetworkBuilder {

  /**
   * Set neural net layers
   * @param layers an array with the width and depth of each layer
   * @return NetworkBuilder instance
   */
  public abstract NetworkBuilder withLayers(int[] layers);

  /**
   * Set the DataSet
   * @param set - the DataSet
   * @return NetworkBuilder instance
   */
  public abstract NetworkBuilder withDataSet(DataSet set);

  /**
   * Set the ActivationFunction
   * @param activationFunction
   * @return NetworkBuilder instance
   */
  public abstract NetworkBuilder withActivationFunction(ActivationFunction activationFunction);

  /**
   * Set the ErrorMeasure
   * @param errorMeasure
   * @return NetworkBuilder instance
   */
  public abstract NetworkBuilder withErrorMeasure(ErrorMeasure errorMeasure);

  /**
   * Train the network
   * @return the trained network for testing
   */
  public abstract FeedForwardNetwork train();
}
