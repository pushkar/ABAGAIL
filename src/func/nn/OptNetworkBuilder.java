package func.nn;

import func.nn.activation.*;
import func.nn.feedfwd.FeedForwardNetwork;
import func.nn.feedfwd.FeedForwardNeuralNetworkFactory;
import opt.*;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.StandardGeneticAlgorithm;
import shared.*;
import java.text.DecimalFormat;

/**
 * Optimization Alg. - Neural Network Builder
 * @author John Mansfield
 * @version 1.0
 *
 * example usage:
 *     FeedForwardNetwork network = new OptNetworkBuilder()
 *       .withLayers(new int[] {25,10,2})
 *       .withDataSet(new DataSet(instances))
 *       .withIterations(500)
 *       .withSA(10000, .9)
 *       .train();
 */

public class OptNetworkBuilder implements NetworkBuilder {
  /**
   * NN layers
   */
  private int[] layers;

  /**
   * Neural Network
   */
  private FeedForwardNetwork network;

  /**
   * Activation Function
   */
  private ActivationFunction activationFunction;

  /**
   * Network factory
   */
  private FeedForwardNeuralNetworkFactory factory;

  /**
   * Error Measure
   */
  private ErrorMeasure errorMeasure;

  /**
   * DataSet
   */
  private DataSet set;

  /**
   * Iterations
   */
  private int iters;

  /**
   * SA temperature
   */
  private double t;

  /**
   * SA cooling
   */
  private double cooling;

  /**
   * GA population size
   */
  private int populationSize;

  /**
   * GA toMate percent
   */
  private int toMate;

  /**
   * GA toMutate percent
   */
  private int toMutate;

  /**
   * optAlg
   */
  private Class optAlg;

  /**
   * Constructor - initialize defaults
   */
  public OptNetworkBuilder(){
    layers = new int[]{100, 1};
    network = new FeedForwardNetwork();
    activationFunction = new HyperbolicTangentSigmoid();
    factory = new FeedForwardNeuralNetworkFactory();
    iters = 1000;
    errorMeasure = new SumOfSquaresError();
    optAlg=RandomizedHillClimbing.class;
  }

  /**
   * Set Activation Function
   */
  public OptNetworkBuilder withActivationFunction(ActivationFunction activationFunction) {
    this.activationFunction = activationFunction;
    return this;
  }

  /**
   * Set depth and width of NN layers
   */
  public OptNetworkBuilder withLayers(int[] layers) {
    this.layers = layers;
    return this;
  }

  /**
   * Set the DataSet
   */
  public OptNetworkBuilder withDataSet(DataSet set) {
    this.set = set;
    return this;
  }

  /**
   * Set Iterations
   */
  public OptNetworkBuilder withIterations(int iters) {
    this.iters = iters;
    return this;
  }

  /**
   * Set the Error Measure
   */
  public OptNetworkBuilder withErrorMeasure(ErrorMeasure errorMeasure) {
    this.errorMeasure = errorMeasure;
    return this;
  }

  /**
   * RHC is the default optimization algorithm
   */
  public OptNetworkBuilder withRHC() {
    return this;
  }

  /**
   * Set optimization algorithm to Simulated Annealing
   */
  public OptNetworkBuilder withSA(double t, double cooling) {
    this.t=t;
    this.cooling=cooling;
    this.optAlg= SimulatedAnnealing.class;
    return this;
  }

  /**
   * Set optimization algorithm to Genetic Algorithm
   */
  public OptNetworkBuilder withGA(int populationSize, int toMate, int toMutate) {
    this.populationSize=populationSize;
    this.toMate=toMate;
    this.toMutate=toMutate;
    this.optAlg= StandardGeneticAlgorithm.class;
    return this;
  }

  /**
   * Build NN and Train
   * @return the network for testing
   */
  public FeedForwardNetwork train() {

    if (set==null){
      throw new RuntimeException("Invalid DataSet");
    }

    //build
    network = factory.createClassificationNetwork(this.layers, (DifferentiableActivationFunction) this.activationFunction);
    NeuralNetworkOptimizationProblem nnop = new NeuralNetworkOptimizationProblem(set, network, errorMeasure);

    //set opt alg
    OptimizationAlgorithm oa;
    if (optAlg == SimulatedAnnealing.class){
      oa = new SimulatedAnnealing(this.t, this.cooling, nnop);
    }
    else if (optAlg== StandardGeneticAlgorithm.class){
      oa = new StandardGeneticAlgorithm(this.populationSize, this.toMate, this.toMutate, nnop);
    }
    else {
      oa = new RandomizedHillClimbing(nnop);
    }

    //opt network train - from AbaloneTest.java
    double error;
    DecimalFormat df = new DecimalFormat("0.000");
    for (int i = 0; i < iters; i++) {
      oa.train();
      error=0;
      for(int j = 0; j < set.getInstances().length; j++) {
        network.setInputValues(set.getInstances()[j].getData());
        network.run();
        Instance output = set.getInstances()[j].getLabel(), example = new Instance(network.getOutputValues());
        example.setLabel(new Instance(network.getOutputValues()));
        error += errorMeasure.value(output, example);
      }
      System.out.println(i + " | " + iters + ": " + df.format(error / set.getInstances().length));
    }
    return network;
  }
}

