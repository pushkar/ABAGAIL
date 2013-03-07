package shared.tester;

import shared.Instance;
import shared.reader.DataSetLabelBinarySeperator;
import func.nn.NeuralNetwork;

/**
 * A tester for neural networks.  This will run each instance
 * through the network and report the results to any test metrics
 * specified at instantiation.
 * 
 * @author Jesse Rosalia (https://www.github.com/theJenix)
 * @date 2013-03-05
 */
public class NeuralNetworkTester implements Tester {

    private NeuralNetwork network;
    private TestMetric[] metrics;

    public NeuralNetworkTester(NeuralNetwork network, TestMetric ... metrics) {
        this.network = network;
        this.metrics = metrics;
    }

    @Override
    public void test(Instance[] instances) {
        for (int i = 0; i < instances.length; i++) {
            //run the instance data through the network
            network.setInputValues(instances[i].getData());
            network.run();

            Instance expected = instances[i].getLabel();
            Instance actual   = new Instance(network.getOutputValues());

            //collapse the values, for statistics reporting
            //NOTE: assumes discrete labels, with n output nodes for n
            // potential labels, and an activation function that outputs
            // values between 0 and 1.
            Instance expectedOne = DataSetLabelBinarySeperator.combineLabels(expected);
            Instance actualOne   = DataSetLabelBinarySeperator.combineLabels(actual);

            //run this result past all of the available test metrics
            for (TestMetric metric : metrics) {
                metric.addResult(expectedOne, actualOne);
            }
        }
    }
}
