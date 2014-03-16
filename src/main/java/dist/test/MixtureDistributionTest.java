package dist.test;

import util.linalg.DenseVector;
import util.linalg.RectangularMatrix;
import dist.Distribution;
import dist.MixtureDistribution;
import dist.DiscreteDistribution;
import dist.MultivariateGaussian;
import shared.DataSet;
import shared.Instance;

/**
 * Testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MixtureDistributionTest {
    
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) throws Exception {
        Instance[] instances = new Instance[100];
        MultivariateGaussian mga = new MultivariateGaussian(new DenseVector(new double[] {100, 100, 100}), RectangularMatrix.eye(3).times(.01)); 
        MultivariateGaussian mgb = new MultivariateGaussian(new DenseVector(new double[] {-1, -1, -1}), RectangularMatrix.eye(3).times(10)); 
        for (int i = 0; i < instances.length; i++) {
            if (Distribution.random.nextBoolean()) {
                instances[i] = mga.sample();   
            } else {
                instances[i] = mgb.sample();
            }
            System.out.println(instances[i]);
        }
        DataSet set = new DataSet(instances);
        MixtureDistribution md = new MixtureDistribution(new Distribution[] {
            new MultivariateGaussian(new DenseVector(new double[] {120, 80, 100}), RectangularMatrix.eye(3).times(1)),
            new MultivariateGaussian(new DenseVector(new double[] {-1, -6, -5}), RectangularMatrix.eye(3).times(1))},
            DiscreteDistribution.random(2).getProbabilities());
        System.out.println(md);
        for (int i = 0; i < 20; i++) {
            md.estimate(set);
            System.out.println(md);
        }
        System.out.println(md);
        for (int i = 0; i < 30; i++) {
            System.out.println(md.sample(null));
        }
    }

}
