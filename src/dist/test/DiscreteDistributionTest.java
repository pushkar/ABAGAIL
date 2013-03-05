package dist.test;

import shared.DataSet;
import shared.Instance;
import dist.DiscreteDistribution;

/**
 * A multinomial distribution test
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteDistributionTest {
    /**
     * Test main
     * @param args
     */
    public static void main(String[] args) {
        double[] ps = new double[] {
            .1, .3, .2, .4
        };
        DiscreteDistribution md = new DiscreteDistribution(ps);
        Instance[] samples = new Instance[10000];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = md.sample();
        }
        md.estimate(new DataSet(samples));
        System.out.println(md);
    }

}
