package func.test;

import dist.Distribution;
import dist.MultivariateGaussian;
import shared.DataSet;
import shared.Instance;
import util.linalg.DenseVector;
import util.linalg.RectangularMatrix;
import func.svm.LinearKernel;
import func.svm.PolynomialKernel;
import func.svm.RBFKernel;
import func.svm.SigmoidKernel;
import func.svm.SingleClassSequentialMinimalOptimization;
import func.svm.SingleClassSupportVectorMachine;

/**
 * A test class
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SingleClassSequentialMinimalOptimizationTest {
    
    /**
     * Test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Instance[] instances = new Instance[100];
        boolean[] instancesRare = new boolean[100];
        MultivariateGaussian mga = new MultivariateGaussian(new DenseVector(new double[] {100, 100, 100}), RectangularMatrix.eye(3).times(.01)); 
        MultivariateGaussian mgb = new MultivariateGaussian(new DenseVector(new double[] {-1, -1, -1}), RectangularMatrix.eye(3).times(1)); 
        for (int i = 0; i < instances.length; i++) {
            if (Distribution.random.nextDouble() < .05) {
                instances[i] = mga.sample(null);
                instancesRare[i] = true;
            } else {
                instances[i] = mgb.sample(null);
            }
        }
        double avgP = 0;
        for (int i = 0; i < instances.length; i++) {
            avgP += mga.p(instances[i]);
        }
        System.out.println("Average p : " + avgP / instances.length);
        PolynomialKernel pk = new PolynomialKernel(2, true);
        LinearKernel lk = new LinearKernel();
        SigmoidKernel sk = new SigmoidKernel();
        RBFKernel rk = new RBFKernel(10);
        SingleClassSequentialMinimalOptimization smo =
            new SingleClassSequentialMinimalOptimization(new DataSet(instances), 
               rk, .1);
        smo.train();
        SingleClassSupportVectorMachine svm = smo.getSupportVectorMachine();
        System.out.println(svm);
        System.out.println("Num iterations " + smo.getNumberOfIterations());
        System.out.println("Num svs " + svm.getSupportVectors().size());
        instances = svm.getSupportVectors().getInstances();
        avgP = 0;
        for (int i = 0; i < instances.length; i++) {
            avgP += mga.p(instances[i]);
        }
        System.out.println("SV Average p: " + avgP / instances.length);
        return;
    }
}
