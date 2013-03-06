package dist;

import shared.Copyable;
import shared.DataSet;
import shared.Instance;

/**
 * A distribution who's  probabilities
 * are precalculated and stored in the observation
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class PrecalculatedDistribution extends AbstractDistribution implements Copyable {
    /**
     * The index at which the precalculated probability is stored
     */
    private int i;
    
    /**
     * Make a new precalculated output distribution
     * @param i the index
     */
    public PrecalculatedDistribution(int i) {
        this.i = i;
    }

    /**
     * @see hmm.distribution.OutputDistribution#generateRandom(hmm.observation.Observation)
     */
    public Instance sample(Instance input) {
        return null;
    }

    /**
     * @see hmm.distribution.OutputDistribution#generateMostLikely(hmm.observation.Observation)
     */
    public Instance mode(Instance input) {
        return null;
    }

    /**
     * @see hmm.distribution.OutputDistribution#probabilityOfObservation(hmm.observation.Observation)
     */
    public double p(Instance inst) {
        return inst.getContinuous(i);
    }
    

    /**
     * @see dist.Distribution#logLikelihood(shared.Instance)
     */
    public double logp(Instance i) {
        return Math.log(p(i));
    }

    /**
     * @see dist.Distribution#estimate(shared.DataSet)
     */
    public void estimate(DataSet observations) { }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Precalculated " + i;
    }

    /**
     * @see shared.Copyable#copy()
     */
    public Copyable copy() {
        return this;
    }




}
