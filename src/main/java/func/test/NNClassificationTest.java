package func.test;

import shared.ConvergenceTrainer;
import shared.DataSet;
import shared.Instance;
import shared.SumOfSquaresError;
import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import func.nn.backprop.BatchBackPropagationTrainer;
import func.nn.backprop.RPROPUpdateRule;

/**
 * A simple classification test
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class NNClassificationTest {

    /**
     * Tests out the perceptron with the classic xor test
     * @param args ignored
     */
    public static void main(String[] args) {
        BackPropagationNetworkFactory factory = 
            new BackPropagationNetworkFactory();
        double[][][] data = {
            { { 1, 1 }, { .1, .9 } },
            { { 0, 1 }, { 0, 1 } },
            { { 0, 0 }, { .9, .1 } }
        };
        Instance[] patterns = new Instance[data.length];
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = new Instance(data[i][0]);
            patterns[i].setLabel(new Instance(data[i][1]));
        }
        BackPropagationNetwork network = factory.createClassificationNetwork(
           new int[] { 2, 2, 2 });
        DataSet set = new DataSet(patterns);
        ConvergenceTrainer trainer = new ConvergenceTrainer(
               new BatchBackPropagationTrainer(set, network,
                   new SumOfSquaresError(), new RPROPUpdateRule()));
        trainer.train();
        System.out.println("Convergence in " 
            + trainer.getIterations() + " iterations");
        for (int i = 0; i < patterns.length; i++) {
            network.setInputValues(patterns[i].getData());
            network.run();
            System.out.println("~~");
            System.out.println(patterns[i].getLabel());
            System.out.println(network.getOutputValues());
        }
    }
}
