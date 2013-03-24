package dist;

import shared.Copyable;
import shared.DataSet;
import shared.Instance;

/**
 * A distribution that acts on the label of the instance
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LabelDistribution extends AbstractDistribution implements Copyable {
    
    /**
     * The distribution
     */
    private Distribution dist;
    
    /**
     * Make a new label distribution
     * @param dist the distribution
     */
    public LabelDistribution(Distribution dist) {
        this.dist = dist;
    }

    /**
     * @see dist.Distribution#probabilityOf(shared.Instance)
     */
    public double p(Instance i) {
        return dist.p(i.getLabel());
    }

    /**
     * @see dist.Distribution#logLikelihood(shared.Instance)
     */
    public double logp(Instance i) {
        return dist.logp(i.getLabel());
    }

    /**
     * @see dist.Distribution#generateRandom(shared.Instance)
     */
    public Instance sample(Instance i) {
        return dist.sample(i.getLabel());
    }
   
    /**
     * @see dist.Distribution#mode(shared.Instance)
     */ 
    public Instance mode(Instance i) {
        return dist.mode(i.getLabel());
    }

    /**
     * @see dist.Distribution#estimate(shared.DataSet)
     */
    public void estimate(DataSet observations) {
        dist.estimate(observations.getLabelDataSet());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Label Distribution " + dist.toString();
    }

    /**
     * @see shared.Copyable#copy()
     */
    public Copyable copy() {
        return new LabelDistribution((Distribution) ((Copyable) dist).copy());
    }
    
    /**
     * Get the distribution
     * @return returns the distribution
     */
    public Distribution getDistribution() {
        return dist;
    }
    /**
     * Set the distribution
     * @param dist The distribution to set
     */
    public void setDistribution(Distribution dist) {
        this.dist = dist;
    }
}
