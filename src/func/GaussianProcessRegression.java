package func;

import dist.AbstractConditionalDistribution;
import dist.Distribution;
import dist.UnivariateGaussian;
import shared.DataSet;
import shared.Instance;
import util.linalg.CholeskyFactorization;
import util.linalg.DenseVector;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.Vector;
import func.svm.Kernel;
import func.svm.LinearKernel;

/**
 * A gaussian process regression
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class GaussianProcessRegression extends AbstractConditionalDistribution implements FunctionApproximater {
    /**
     * The kernel
     */
    private Kernel kernel;
    /**
     * The noise sigma
     */
    private double noiseSigma;
    /**
     * The kernel matrix
     */
    private Matrix c;
    /**
     * The cholesky factorization of the kernel matrix
     */
    private CholeskyFactorization cf;
    /**
     * The a values
     */
    private Vector a;
    
    /**
     * Make a new gaussian process regression
     * @param k the kernel to use
     * @param noise the noise sigma value
     */
    public GaussianProcessRegression(Kernel k, double noise) {
        this.kernel = k;
        this.noiseSigma = noise;
    }
    /**
     * Make a new default gaussian process regression
     */
    public GaussianProcessRegression() {
        this(new LinearKernel(), 1);
    }
    /**
     * @see func.FunctionApproximater#estimate(shared.DataSet)
     */
    public void estimate(DataSet set) {
        // make the kernel matrix
        c = new RectangularMatrix(set.size(), set.size());
        kernel.setExamples(set);
        for (int i = 0; i < c.m(); i++) {
            for (int j = 0; j < c.n(); j++) {
                c.set(i,j, kernel.value(i,j));
            }
        }
        // add in the noise
        c.plusEquals(RectangularMatrix.eye(set.size()).times(noiseSigma * noiseSigma));
        // make the target vector
        Vector t = new DenseVector(set.size());
        for (int i = 0; i < t.size(); i++) {
            t.set(i, set.get(i).getLabel().getContinuous());
        }
        // solve for the weights
        cf = new CholeskyFactorization(c);
        a = cf.solve(t);
    }
    
    /**
     * @see func.FunctionApproximater#value(shared.Instance)
     */
    public Instance value(Instance i) {
        return distributionFor(i).mode();
    }
    
    /**
     * @see dist.ConditionalDistribution#distributionFor(shared.Instance)
     */
    public Distribution distributionFor(Instance instance) {
        Vector k = new DenseVector(c.m());
        for (int i = 0; i < k.size(); i++) {
            k.set(i, kernel.value(i, instance));
        }        
        double mean = a.dotProduct(k);
        double sigma = Math.sqrt(
            kernel.value(instance, instance) - k.dotProduct(cf.solve(k)));
        return new UnivariateGaussian(mean, sigma);
    }
    

}
