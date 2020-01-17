package func.nn;
import func.nn.activation.*;
import func.nn.backprop.*;
import shared.*;

/**
 * Neural Network Builder
 * @author John Mansfield
 * @version 1.0
 *
 * example usage:
 *     BackPropagationNetwork network = new NetworkBuilder()
 *       .withUpdateRule(new ADAM(.025, .9, .95))
 *       .withLayers(new int[] {25,10,2})
 *       .withDataSet(new DataSet(instances))
 *       .withIterations(500)
 *       .train();
 */

public class NetworkBuilder {
  /**
   * NN layers
   */
  private int[] layers;

  /**
   * Backprop Network
   */
  private static BackPropagationNetwork network;

  /**
   * Activation Function
   */
  private static DifferentiableActivationFunction activationFunction;

  /**
   * Update Rule
   */
  private static WeightUpdateRule updateRule;

  /**
   * Backprop factory
   */
  private static BackPropagationNetworkFactory factory;

  /**
   * Error Measure
   */
  private static GradientErrorMeasure errorMeasure;

  /**
   * DataSet
   */
  private static DataSet set;

  /**
   * Iterations (when not using convergence trainer)
   */
  private static int iters;

  /**
   * Constructor - initialize defaults
   */
  public NetworkBuilder(){
    layers = new int[]{10, 2};
    network = new BackPropagationNetwork();
    activationFunction = new LogisticSigmoid();
    updateRule = new RPROPUpdateRule();
    factory = new BackPropagationNetworkFactory();
    iters = 0;
    errorMeasure = new SumOfSquaresError();
  }

  /**
   * Set Activation Function (Default: LogisticSigmoid)
   */
  public NetworkBuilder withActivationFunction(DifferentiableActivationFunction activationFunction) {
    this.activationFunction = activationFunction;
    return this;
  }

  /**
   * Set Depth and Width of NN Layers (Default: {20, 10, 2})
   */
  public NetworkBuilder withLayers(int[] layers) {
    this.layers = layers;
    return this;
  }

  /**
   * Set DataSet
   */
  public NetworkBuilder withDataSet(DataSet set) {
    this.set = set;
    return this;
  }

  /**
   * Set Update Rule (Default: RPROPUpdateRule)
   */
  public NetworkBuilder withUpdateRule(WeightUpdateRule updateRule) {
    this.updateRule = updateRule;
    return this;
  }

  /**
   * Set Iterations (Default: Convergence Trainer/iters=0)
   */
  public NetworkBuilder withIterations(int iters) {
    this.iters = iters;
    return this;
  }

  /**
   * Set Gradient Error Measure (Default: SumOfSquaresError())
   */
  public NetworkBuilder withErrorMeasure(GradientErrorMeasure errorMeasure) {
    this.errorMeasure = errorMeasure;
    return this;
  }

  /**
   * Build NN and Train
   * @return the network for testing
   */
  public BackPropagationNetwork train() {

    if (set==null){
      System.out.println("invalid dataset");
      return network;
    }

    //build
    DifferentiableActivationFunction[] differentiableActivationFunctions = new DifferentiableActivationFunction[this.layers.length];
    differentiableActivationFunctions[this.layers.length - 1] = new LinearActivationFunction();;
    for (int i = 0; i < this.layers.length - 1; i++) {
      differentiableActivationFunctions[i] = this.activationFunction;
    }
    network = factory.createClassificationNetwork(this.layers, differentiableActivationFunctions);

    //train
    double error;
    if (iters==0){
      ConvergenceTrainer trainer = new ConvergenceTrainer(new BatchBackPropagationTrainer(set, network, errorMeasure, updateRule));
      System.out.println("Running convergence trainer.");
      error=trainer.train();
      System.out.println("training error: " + error);
    }
    else {
      for (int i = 0; i < iters; i++) {
        BatchBackPropagationTrainer trainer = new BatchBackPropagationTrainer(set, network, errorMeasure, updateRule);
        error=trainer.train();
        System.out.println("training iter: " + i + " training error: " + error);
      }
    }
    return network;
  }
}
