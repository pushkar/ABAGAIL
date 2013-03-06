package dist;

import shared.Copyable;
import shared.DataSet;
import shared.Instance;
import util.linalg.CholeskyFactorization;
import util.linalg.DenseVector;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.Vector;

/**
 * A multivariate gaussian distribution
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MultivariateGaussian extends AbstractDistribution  implements Copyable {
    /**
     * The default floor
     */
    private static final double FLOOR = .01;
    /**
     * The change in floor
     */
    private static final double FLOOR_CHANGE = 10;
    
    /**
     * The mean of the gaussian
     */
    private Vector mean;
    
    /**
     * The covariance matrix
     */
    private Matrix covarianceMatrix;
    
    /**
     * The decomposition of the covariance
     */
    private CholeskyFactorization decomposition;
    
    /**
     * The determinant
     */
    private double determinant;
    
    /**
     * The minimum allowed value in the covariance matrices
     */
    private double floor;
    
    /**
     * Whether to print lots of stuff out
     */
    private boolean debug;
    
    /**
     * Make a new multivariate gaussian
     * @param mean the mean
     * @param covariance the covariance
     */
    public MultivariateGaussian(Vector mean, Matrix covariance, double floor) {
        this.mean = mean;
        this.covarianceMatrix = covariance;
        this.floor = floor;
        decomposition = new CholeskyFactorization(covariance);
        determinant = decomposition.determinant();
    }
    
    /**
     * Make a new multivariate gaussian
     * @param mean the mean
     * @param covariance the covariance
     */
    public MultivariateGaussian(Vector mean, Matrix covariance) {
        this(mean, covariance, 0);
    }
    
    /**
     * Make a new multivariate gaussian
     * @param floor the floor value
     */
    public MultivariateGaussian(double floor) {
        this.floor = floor;
    }
    
    /**
     * Make a new multivariate gaussian
     */
    public MultivariateGaussian() {
    }

    /**
     * @see dist.Distribution#probabilityOf(shared.Instance)
     */
    public double p(Instance i) {
        Vector d = i.getData();
        Vector dMinusMean = d.minus(mean);
        double p = 1/Math.sqrt(Math.pow(2*Math.PI, mean.size())* determinant)
            * Math.exp(-.5 * dMinusMean.dotProduct(decomposition.solve(dMinusMean)));
        return p;
    }
    
    /**
     * Calculate the log likelihood
     * @param i the instance
     * @return the log likelihood
     */
    public double logp(Instance i) {
        Vector d = i.getData();
        Vector dMinusMean = d.minus(mean);
        double p = Math.log(1/Math.sqrt(Math.pow(2*Math.PI, mean.size())* determinant))
                - .5 * dMinusMean.dotProduct(decomposition.solve(dMinusMean));
        return p;
    }

    /**
     * @see dist.Distribution#generateRandom(shared.Instance)
     */
    public Instance sample(Instance ignored) {
        Vector r = new DenseVector(mean.size());
        for (int i = 0; i < r.size(); i++) {
            r.set(i, random.nextGaussian());
        }
        return new Instance(decomposition.getL().times(r).plus(mean));
    }

    /**
     * @see dist.Distribution#generateMostLikely(shared.Instance)
     */
    public Instance mode(Instance ignored) {
        return new Instance((Vector) mean.copy());
    }

    /**
     * @see dist.Distribution#estimate(shared.DataSet)
     */
    public void estimate(DataSet observations) {
        double weightSum = 0;
        // calculate mean
        mean = new DenseVector(observations.get(0).size());
        for (int t = 0; t < observations.size(); t++) {
            double weight = observations.get(t).getWeight();
            Vector d = observations.get(t).getData();
            for (int i = 0; i < mean.size(); i++) {
                mean.set(i, mean.get(i) + d.get(i) * weight);
            }
            weightSum += weight;
        }
        mean.timesEquals(1/weightSum);
        // and covariance
        covarianceMatrix = new RectangularMatrix(mean.size(), mean.size());
        for (int t = 0; t < observations.size(); t++) {
            Vector d = observations.get(t).getData();
            double weight = observations.get(t).getWeight();
            Vector dMinusMean = d.minus(mean);
            for (int i = 0; i < covarianceMatrix.m(); i++) {
                for (int j = 0; j < covarianceMatrix.n(); j++) {
                    covarianceMatrix.set(i,j,
                        covarianceMatrix.get(i,j) +
                        dMinusMean.get(i) * dMinusMean.get(j) * weight);
                }
            }
        }
        covarianceMatrix.timesEquals(1/weightSum);
        boolean scale = false;
        for (int i = 0; i < covarianceMatrix.m(); i++) {
            if (covarianceMatrix.get(i, i) < floor) {
                scale = true;
            }
        }
        if (scale) {
            for (int i = 0; i < covarianceMatrix.m(); i++) {
                covarianceMatrix.set(i, i, covarianceMatrix.get(i,i) + floor);
            }
        }
        // decompose the covariance matrix
        decomposition = new CholeskyFactorization(covarianceMatrix);
        determinant = decomposition.determinant();
        // the matrix isn't positive
        if (determinant == 0 || Double.isNaN(determinant)) {
            if (debug) {
                System.out.println("Covariance matrix not positive, applying ridge adjustment");
                System.out.println(covarianceMatrix);
            }
            if (floor == 0) {
                floor = FLOOR;
            } else {
                floor *= FLOOR_CHANGE;
            }
            // try again
            estimate(observations);
        }
    }
    
    public String toString() {
        return "mean =\n" + mean.toString()
            + "\ncovariance matrix =\n" + covarianceMatrix.toString();
    }

    /**
     * Get the covariance matrix
     * @return the covariance matrix
     */
    public Matrix getCovarianceMatrix() {
        return covarianceMatrix;
    }

    /**
     * Get the mean
     * @return the mean vector
     */
    public Vector getMean() {
        return mean;
    }

    /**
     * Set the covariance matrix
     * @param matrix the matrix
     */
    public void setCovarianceMatrix(Matrix matrix) {
        covarianceMatrix = matrix;
    }

    /**
     * Set the mean vector
     * @param vector the new mean
     */
    public void setMean(Vector vector) {
        mean = vector;
    }

    /**
     * Whether to print out lots of stuff
     * @return true if we should
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Set the debug mode
     * @param b true if you want to see stuff printe dout
     */
    public void setDebug(boolean b) {
        debug = b;
    }
    
    public Copyable copy() {
        return new MultivariateGaussian((Vector) mean.copy(), 
                    (Matrix) covarianceMatrix.copy(), floor);
    }

}
