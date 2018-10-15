package opt.prob;


import dist.Distribution;
import opt.OptimizationAlgorithm;
import opt.OptimizationProblem;
import shared.DataSet;
import shared.Instance;
import util.ABAGAILArrays;

/**
 * Based on the MIMIC algorithm
 * J. S. De Bonet, C. L. Isbell, and P. Viola (1997). 
 * MIMIC: Finding Optima by Estimating Probability Densities, 
 * Advances in Neural Information Processing Systems, Vol. 9 .
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MIMIC extends OptimizationAlgorithm {
    /**
     * The current distribution
     */
    private Distribution distribution;
    
    /**
     * The number of samples to generate
     */
    private int samples;
    
    /**
     * The number of samples to keep
     */
    private int tokeep;

    /**
     * Make a new mimic
     * @param samples the number of samples to take each iteration
     * @param random how many of those samples should be completely random
     * @param theta the starting theta
     * @param increment the increment
     * @param stoppingCount the minimum number of good samples needed to continue
     * @param op the problem
     */
    public MIMIC(int samples, int tokeep, ProbabilisticOptimizationProblem op) {
        super(op);
        this.tokeep = tokeep;
        this.samples = samples;
        Instance[] data = new Instance[samples];
        for (int i = 0; i < data.length; i++) {
            data[i] = op.random();
        }
        distribution = op.getDistribution();
        distribution.estimate(new DataSet(data));
    }

    /**
     * @see opt.OptimizationAlgorithm#getOptimal()
     */
    public Instance getOptimal() {
        OptimizationProblem op = getOptimizationProblem();
        Instance[] data = new Instance[samples];
        for (int i = 0; i < data.length; i++) {
            data[i] = distribution.sample(null);
        } 
        double bestVal = op.value(data[0]);
        Instance best = data[0];
        for (int i = 1; i < data.length; i++) {
            double value = op.value(data[i]);
            if (value > bestVal) {
                bestVal = value;
                best = data[i];
            }
        }
        return best;
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        ProbabilisticOptimizationProblem op = (ProbabilisticOptimizationProblem) getOptimizationProblem();
        Instance[] data = new Instance[samples];
        for (int i = 0; i < data.length; i++) {
            data[i] = distribution.sample(null);
        } 
        double[] values = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = op.value(data[i]);
        }
        double[] temp = new double[values.length];
        System.arraycopy(values, 0, temp, 0, temp.length);
        double cutoff = ABAGAILArrays.randomizedSelect(temp, temp.length - tokeep);
        int j = 0;
        Instance[] kept = new Instance[tokeep];
        for (int i = 0; i < data.length && j < kept.length; i++) {
            if (values[i] >= cutoff) {
                kept[j] = data[i];
                j++;
            }
        }
        distribution.estimate(new DataSet(kept));
        return cutoff;
    }

}
