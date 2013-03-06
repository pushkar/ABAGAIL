package shared.tester;

import shared.Instance;
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

            //run this result past all of the available test metrics
            for (TestMetric metric : metrics) {
                metric.addResult(expected, actual);
            }
        }
    }
}
