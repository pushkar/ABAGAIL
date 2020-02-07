package func.nn;
import func.nn.activation.*;
import func.nn.backprop.*;
import shared.*;

/**
 * Backprop - Neural Network Builder
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

public class BackpropNetworkBuilder implements NetworkBuilder{
  /**
   * NN layers
   */
  private int[] layers;

  /**
   * Backprop Network
   */
  private BackPropagationNetwork network;

  /**
   * Activation Function
   */
  private ActivationFunction activationFunction;

  /**
   * Update Rule
   */
  private WeightUpdateRule updateRule;

  /**
   * Backprop factory
   */
  private BackPropagationNetworkFactory factory;

  /**
   * Error Measure
   */
  private ErrorMeasure errorMeasure;

  /**
   * DataSet
   */
  private DataSet set;

  /**
   * Iterations (when not using convergence trainer)
   */
  private int iters;

  /**
   * Constructor - initialize defaults
   */
  public BackpropNetworkBuilder(){
    layers = new int[]{100, 1};
    network = new BackPropagationNetwork();
    activationFunction = new LogisticSigmoid();
    updateRule = new RPROPUpdateRule();
    factory = new BackPropagationNetworkFactory();
    iters = 0;
    errorMeasure = new SumOfSquaresError();
  }

  /**
   * Set Activation Function
   */
  public BackpropNetworkBuilder withActivationFunction(ActivationFunction activationFunction) {
    if (activationFunction instanceof DifferentiableActivationFunction) {
      this.activationFunction = activationFunction;
    }
    else {
      throw new IllegalArgumentException();
    }
    return this;
  }

  /**
   * Set depth and width of NN layers
   */
  public BackpropNetworkBuilder withLayers(int[] layers) {
    this.layers = layers;
    return this;
  }

  /**
   * Set DataSet
   */
  public BackpropNetworkBuilder withDataSet(DataSet set) {
    this.set = set;
    return this;
  }

  /**
   * Set Update Rule
   */
  public BackpropNetworkBuilder withUpdateRule(WeightUpdateRule updateRule) {
    this.updateRule = updateRule;
    return this;
  }

  /**
   * Set Iterations
   */
  public BackpropNetworkBuilder withIterations(int iters) {
    this.iters = iters;
    return this;
  }

  /**
   * Set Gradient Error Measure
   */
  public BackpropNetworkBuilder withErrorMeasure(ErrorMeasure errorMeasure) {
    if (errorMeasure instanceof GradientErrorMeasure){
      this.errorMeasure = errorMeasure;
    }
    else {
      throw new IllegalArgumentException();
    }
    return this;
  }

  /**
   * Build NN and Train
   * @return the network for testing
   */
  public BackPropagationNetwork train() {

    if (set==null){
      throw new RuntimeException("Invalid DataSet");
    }

    //build
    DifferentiableActivationFunction[] activationFunctions = new DifferentiableActivationFunction[this.layers.length];
    activationFunctions[this.layers.length - 1] = new LinearActivationFunction();
    for (int i = 0; i < this.layers.length - 1; i++) {
      activationFunctions[i] = (DifferentiableActivationFunction) this.activationFunction;
    }
    network = factory.createClassificationNetwork(this.layers, activationFunctions);

    //train
    double error;
    if (iters==0){
      ConvergenceTrainer trainer = new ConvergenceTrainer(new BatchBackPropagationTrainer(set, network, (GradientErrorMeasure) errorMeasure, updateRule));
      System.out.println("Running convergence trainer.");
      error=trainer.train();
      System.out.println("training error: " + error);
      System.out.print("number of iterations: " + trainer.getIterations());
    }
    else {
      for (int i = 0; i < iters; i++) {
        BatchBackPropagationTrainer trainer = new BatchBackPropagationTrainer(set, network, (GradientErrorMeasure) errorMeasure, updateRule);
        error=trainer.train();
        System.out.println("training iter: " + i + " training error: " + error);
      }
    }
    return network;
  }
}
