package dist.test;

import dist.DiscreteDependencyTree;
import shared.DataSet;
import shared.Instance;

/**
 * A test of the discrete dependency tree distribution
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteDependencyTreeTest {
    /**
     * The test data
     */
    private static Instance[] data = new Instance[] {
        new Instance(new double[] { 0, 4, 4 , 4}),
        new Instance(new double[] { 4, 0, 1 , 0}),
        new Instance(new double[] { 4, 1, 0 , 1}),
        new Instance(new double[] { 4, 0, 0 , 0}),
    };
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        DataSet dataSet = new DataSet(data);
        DiscreteDependencyTree ddtd = 
            new DiscreteDependencyTree(.001);
        ddtd.estimate(dataSet);
        System.out.println(ddtd);
        for (int i = 0; i < 20; i++) {
            System.out.println(ddtd.sample(null));
        }
        System.out.println("Most likely");
        System.out.println(ddtd.mode(null));
        System.out.println("Probabilities of training data");
        for (int i = 0; i < data.length; i++) {
            System.out.println(ddtd.p(data[i]));
        }
    }

}
