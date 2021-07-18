package opt.test;

import func.nn.BackpropNetworkBuilder;
import func.nn.LayeredNetwork;
import func.nn.OptNetworkBuilder;
import func.nn.backprop.BackPropagationNetwork;
import func.nn.feedfwd.FeedForwardNetwork;
import shared.*;
import shared.filt.*;
import shared.normalizer.StandardMeanAndVariance;
import shared.reader.CSVDataSetReader;
import shared.reader.DataSetReader;

import java.io.File;

/**
 * Iris nn example using network builder
 *
 * https://archive.ics.uci.edu/ml/datasets/Iris
 *
 * @author John Mansfield
 * @version 1.0
 */

public class IrisTest {

  private static int outputLayerSize;
  private static DataSet train;
  private static DataSet test;

  private static void initializeData() throws Exception {
    //import data
    DataSetReader dsr = new CSVDataSetReader((new File("src/opt/test/iris.txt")).getAbsolutePath());
    DataSet ds = dsr.read();
    System.out.println(new DataSetDescription(ds));

    //split last attribute for label
    LabelSplitFilter lsf = new LabelSplitFilter();
    lsf.filter(ds);

    //encode label as one-hot array and get outputLayerSize
    DiscreteToBinaryFilter dbf = new DiscreteToBinaryFilter();
    dbf.filter(ds.getLabelDataSet());
    outputLayerSize=dbf.getNewAttributeCount();

    //test-train split
    int percentTrain=75;
    RandomOrderFilter randomOrderFilter = new RandomOrderFilter();
    randomOrderFilter.filter(ds);
    TestTrainSplitFilter testTrainSplit = new TestTrainSplitFilter(percentTrain);
    testTrainSplit.filter(ds);
    train=testTrainSplit.getTrainingSet();
    test=testTrainSplit.getTestingSet();

    //standardize data
    StandardMeanAndVariance smv = new StandardMeanAndVariance();
    smv.fit(train);
    smv.transform(train);
    smv.transform(test);
  }

  private static void runNetwork() {

    //create backprop network using builder
    BackPropagationNetwork network = new BackpropNetworkBuilder()
      .withLayers(new int[] {25,10,outputLayerSize})
      .withDataSet(train, test)
      .withIterations(5000)
      .train();

    //create opt network using builder
    FeedForwardNetwork optNetwork = new OptNetworkBuilder()
      .withLayers(new int[] {25,10,outputLayerSize})
      .withDataSet(train, test)
      .withSA(100000, .975)
      .withIterations(1000)
      .train();
  }

  public static void main(String[] args) throws Exception {
    initializeData();
    runNetwork();
  }
}
