package dist;

import shared.Copyable;
import shared.DataSet;
import shared.Instance;

/**
 * A (single variable) gaussian distribution
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class UnivariateGaussian extends AbstractDistribution implements Copyable {
    /**
     * The sqrt to two pi
     */
    private static final double SQRTTWOPI = Math.sqrt(2*Math.PI);

    /**
     * The mean
     */
    private double mean;
    
    /**
     * The std deviation
     */
    private double sigma;
    
    /**
     * Make a new gaussian
     * @param mean the mean
     * @param sigma the sigma
     */
    public UnivariateGaussian(double mean, double sigma) {
        this.mean = mean;
        this.sigma = sigma;
    }
    
    /**
     * Make a new standard normal guassian
     */
    public UnivariateGaussian() {
        this(0, 1);
    }

    /**
     * @see dist.Distribution#p(shared.Instance)
     */
    public double p(Instance i) {
        double dMinusMean = i.getContinuous() - mean;
        return 1/(SQRTTWOPI*sigma) * Math.exp(
            -.5*dMinusMean*dMinusMean/(sigma*sigma));
    }

    /**
     * @see dist.Distribution#logp(shared.Instance)
     */
    public double logp(Instance i) {
        double dMinusMean = i.getContinuous() - mean;
        return Math.log(1/(SQRTTWOPI*sigma))
            -.5*dMinusMean*dMinusMean/(sigma*sigma);
    }

    /**
     * @see dist.Distribution#sample(shared.Instance)
     */
    public Instance sample(Instance i) {
        return new Instance(random.nextGaussian() * sigma + mean);
    }

    /**
     * @see dist.Distribution#mode(shared.Instance)
     */
    public Instance mode(Instance i) {
        return new Instance(mean);
    }

    /**
     * @see dist.Distribution#estimate(shared.DataSet)
     */
    public void estimate(DataSet set) {
        mean = 0;
        for (int i = 0; i < set.size(); i++) {
            mean += set.get(i).getContinuous();
        }
        mean /= set.size();
        sigma = 0;
        for (int i = 0; i < set.size(); i++) {
            double dMinusMean = set.get(i).getContinuous() - mean;
            sigma += dMinusMean * dMinusMean;
        }
        sigma /= set.size() - 1;
        sigma = Math.sqrt(sigma);
    }
    
    /**
     * Get the mean
     * @return returns the mean.
     */
    public double getMean() {
        return mean;
    }
    
    /**
     * Set the mean
     * @param mean the mean to set.
     */
    public void setMean(double mean) {
        this.mean = mean;
    }
    
    /**
     * Set the sigma
     * @param sigma the sigma to set.
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }
    /**
     * Get the sigma
     * @return returns the sigma.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * @see shared.Copyable#copy()
     */
    public Copyable copy() {
        return new UnivariateGaussian(mean, sigma);
    }
}
