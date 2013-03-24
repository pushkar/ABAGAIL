package dist;

import shared.DataSet;
import shared.Instance;

/**
 * A distribution of all of the permutations
 * of a set size.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteUniformDistribution extends AbstractDistribution {
    /**
     * The ranges of the data
     */
    private int[] n;
    
    /**
     * The probability
     */
    private double p;
    
    /**
     * Make a new discrete permutation distribution
     * @param n the size of the data
     */
    public DiscreteUniformDistribution(int[] n) {
        this.n = n;
        p = n[0];
        for (int i = 1; i < n.length; i++) {
            p *= n[i];
        }
        p = 1 / p;
    }

    /**
     * @see dist.Distribution#probabilityOf(shared.Instance)
     */
    public double p(Instance i) {
        return p;
    }

    /**
     * @see dist.Distribution#generateRandom(shared.Instance)
     */
    public Instance sample(Instance ignored) {
        double[] d  = new double[n.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = random.nextInt(n[i]);
        }
        return new Instance(d);
    }

    /**
     * @see dist.Distribution#generateMostLikely(shared.Instance)
     */
    public Instance mode(Instance ignored) {
        return sample(ignored);
    }

    /**
     * @see dist.Distribution#estimate(shared.DataSet)
     */
    public void estimate(DataSet observations) {
        return;
    }
}
