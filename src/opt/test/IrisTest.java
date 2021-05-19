package opt.test;

import func.nn.BackpropNetworkBuilder;
import func.nn.LayeredNetwork;
import func.nn.OptNetworkBuilder;
import func.nn.backprop.BackPropagationNetwork;
import func.nn.feedfwd.FeedForwardNetwork;
import shared.*;
import shared.filt.*;
import shared.reader.CSVDataSetReader;
import shared.reader.DataSetReader;

import java.io.File;

public class IrisTest {

  private static int outputLayerSize;

  private static DataSet initializeData() throws Exception {
    //import data
    DataSetReader dsr = new CSVDataSetReader((new File("src/opt/test/iris.txt")).getAbsolutePath());
    DataSet ds = dsr.read();

    //split last attribute for label
    LabelSplitFilter lsf = new LabelSplitFilter();
    lsf.filter(ds);

    //convert label to binary for nn classification and get outputLayerSize
    DiscreteToBinaryFilter dbf = new DiscreteToBinaryFilter();
    dbf.filter(ds.getLabelDataSet());
    outputLayerSize=dbf.getNewAttributeCount();

    return ds;
  }

  private static LayeredNetwork runNetwork(DataSet set) {
    //create backprop network using builder
    BackPropagationNetwork network = new BackpropNetworkBuilder()
      .withLayers(new int[] {25,10,outputLayerSize})
      .withDataSet(set, 75)
      .withIterations(5000)
      .train();

    //create opt network using builder
    /**FeedForwardNetwork network = new OptNetworkBuilder()
      .withLayers(new int[] {25,10,outputLayerSize})
      .withDataSet(set, 75)
      .withSA(100000, .975)
      .withIterations(1000)
      .train();
     **/

    return network;
  }

  public static void main(String[] args) throws Exception {
    DataSet set = initializeData();
    //set = new DataSet(instances);
    System.out.println(new DataSetDescription(set));
    LayeredNetwork nn = runNetwork(set);
  }
}
