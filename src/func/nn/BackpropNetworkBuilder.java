package func.nn;
import func.nn.activation.*;
import func.nn.backprop.*;
import shared.*;
import shared.filt.RandomOrderFilter;
import shared.filt.TestTrainSplitFilter;
import shared.writer.CSVWriter;

import java.io.File;
import java.io.IOException;

/**
 * Backprop - Neural Network Builder
 * @author John Mansfield
 * @version 1.1
 *
 * example usage:
 *     BackPropagationNetwork network = new NetworkBuilder()
 *       .withLayers(new int[] {25,10,2})
 *       .withDataSet(new DataSet(instances), percentTrain)
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
   * Test set
   */
  private DataSet testSet;

  /**
   * Iterations
   */
  private int iters;

  /**
   * Constructor - initialize defaults
   */
  public BackpropNetworkBuilder(){
    layers = new int[]{100, 1};
    network = new BackPropagationNetwork();
    activationFunction = new RELU();
    updateRule = new Adam();
    factory = new BackPropagationNetworkFactory();
    iters = 500;
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
   * Set DataSet and train percent
   */
  public BackpropNetworkBuilder withDataSet(DataSet train, DataSet test) {
    this.set=train;
    this.testSet=test;
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
   * Get network out of sample error
   */
  private double test(BackPropagationNetwork network, DataSet patterns, GradientErrorMeasure measure) {
    double error=0;
    for(int i=0; i<patterns.size(); i++){
      Instance pattern = patterns.get(i);
      network.setInputValues(pattern.getData());
      network.run();
      Instance output = new Instance(network.getOutputValues());
      error += measure.value(output, pattern);
    }
    return error / patterns.size();
  }

  /**
   * Build NN and Train/Test
   * @return the network
   */
  //todo add verbose param option
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

    //setup out to csv
    double error;
    double testError;
    String fileName = "NNOut.csv";
    String[] fields = {"train", "test"};
    String workingDir = System.getProperty("user.dir");
    System.out.println("\nSaving train test error to: \n" + workingDir + File.separator + fileName);
    CSVWriter csv = new CSVWriter(workingDir + File.separator + fileName, fields);
    try {
      csv.open();
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < iters; i++) {
      //train
      BatchBackPropagationTrainer trainer = new BatchBackPropagationTrainer(set, network, (GradientErrorMeasure) errorMeasure, updateRule);
      error=trainer.train();
      //System.out.print("training iter: " + i + " training error: " + error);
      //test
      testError=test(network, testSet, (GradientErrorMeasure) errorMeasure);
      //System.out.println(" test error:" + testError);

      try {
        csv.write(""+error);
        csv.write(""+testError);
        csv.nextRecord();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      csv.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return network;
  }
}
