package func.test;

import shared.DataSet;
import shared.Instance;
import func.svm.LinearKernel;
import func.svm.PolynomialKernel;
import func.svm.RBFKernel;
import func.svm.SequentialMinimalOptimization;
import func.svm.SigmoidKernel;
import func.svm.SupportVectorMachine;

/**
 * A test class
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SequentialMinimalOptimizationTest {
    
    /**
     * Test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Instance[] instances =  {
            new Instance(new double[] {0, 0, 0, 1}, true),
            new Instance(new double[] {0, 1, 0, 1}, true),
            new Instance(new double[] {1, 0, 0, 1}, true),
            new Instance(new double[] {1, 0, 0, 1}, false),
            new Instance(new double[] {1, 0, 1, 1}, false),
            new Instance(new double[] {1, 0, 0, 1}, false),
        };
        double[][] tests =  {
            {0, 1, 1, 1},
            {0, 0, 0, 0},
            {1, 0, 0, 1},
            {1, 1, 1, 1}
        };
        PolynomialKernel pk = new PolynomialKernel(2, true);
        LinearKernel lk = new LinearKernel();
        SigmoidKernel sk = new SigmoidKernel();
        RBFKernel rk = new RBFKernel(.05);
        SequentialMinimalOptimization smo =
        new SequentialMinimalOptimization(new DataSet(instances), 
                sk, 55);
        smo.train();
        SupportVectorMachine svm = smo.getSupportVectorMachine();
        System.out.println(svm.getSupportVectors().size());
        System.out.println("examples");
        for (int i = 0; i < instances.length; i++) {
            System.out.println(svm.margin(instances[i]));
        }
        System.out.println("tests");
        for (int i = 0; i < tests.length; i++) {
            System.out.println(svm.margin(new Instance(tests[i])));
        }
    }
}
