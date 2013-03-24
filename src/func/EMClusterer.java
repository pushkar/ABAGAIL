package func;

import java.util.Arrays;

import shared.DataSet;
import shared.Instance;
import dist.*;
import dist.Distribution;
import dist.MixtureDistribution;
import dist.DiscreteDistribution;
import dist.MultivariateGaussian;

/**
 * An em clusterer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class EMClusterer extends AbstractConditionalDistribution implements FunctionApproximater {
    /** 
     * The tolerance
     */
    private static final double TOLERANCE = 1E-6;
    /** 
     * The tolerance
     */
    private static final int MAX_ITERATIONS = 1000;
    /**
     * The mixture distribution
     */
    private MixtureDistribution mixture;
    /**
     * The number of clusters
     */
    private int k;
    /**
     * The threshold
     */
    private double tolerance;
    
    /**
     * The max iterations
     */
    private int maxIterations;
    
    /**
     * How many iterations it took
     */
    private int iterations;
    
    /**
     * Whether to print stuff
     */
    private boolean debug = false;
    
    /**
     * Make a new em clusterer
     * @param k the number of clusters
     * @param tolerance the tolerance
     */
    public EMClusterer(int k, double tolerance, int maxIterations) {
        this.k = k;
        this.tolerance = tolerance;
        this.maxIterations = maxIterations;
    }
    
    /**
     * Make a new clusterer
     */
    public EMClusterer() {
        this(2, TOLERANCE, MAX_ITERATIONS);
    }

    /**
     * @see func.Classifier#classDistribution(shared.Instance)
     */
    public Distribution distributionFor(Instance instance) {
        // calculate the log probs
        double[] probs = new double[mixture.getComponents().length];
        double maxLog = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < probs.length; i++) {
            probs[i] = mixture.getComponents()[i].logp(instance);
            maxLog = Math.max(maxLog, probs[i]);
        }
        // turn into real probs
        double sum = 0;
        for (int i = 0; i < probs.length; i++) {
            probs[i] = Math.exp(probs[i] - maxLog);
            sum += probs[i];
        }
        // normalize
        for (int i = 0; i < probs.length; i++) {
            probs[i] /= sum;
        }
        return new DiscreteDistribution(probs);
    }

    /**
     * @see func.FunctionApproximater#estimate(shared.DataSet)
     */
    public void estimate(DataSet set) {
        // kmeans initialization
        KMeansClusterer kmeans = new KMeansClusterer(k);
        kmeans.estimate(set);
        double[] prior = new double[k];
        double weightSum = 0;
        int[] counts = new int[k];
        int[] classifications = new int[set.size()];
        for (int i = 0; i < set.size(); i++) {
            classifications[i] = kmeans.value(set.get(i)).getDiscrete();
            counts[classifications[i]]++;
            prior[classifications[i]] += set.get(i).getWeight();
            weightSum += set.get(i).getWeight();
        }
        // create data sets for each of the classes
        Instance[][] instances = new Instance[k][];
        for (int i = 0; i < instances.length; i++) {
            instances[i] = new Instance[counts[i]];
        }
        Arrays.fill(counts, 0);
        for (int i = 0; i < set.size(); i++) {
            instances[classifications[i]][counts[classifications[i]]] = set.get(i);
            counts[classifications[i]]++;
        }
        MultivariateGaussian[] initial = new MultivariateGaussian[k];
        for (int i = 0; i < initial.length; i++) {
            initial[i] = new MultivariateGaussian();
            initial[i].setDebug(debug);
            initial[i].estimate(new DataSet(instances[i]));
            prior[i] /= weightSum;
        }
        mixture = new MixtureDistribution(initial, prior);
        // reestimate
        boolean done = false;
        double lastLogLikelihood = 0;
        iterations = 0;
        while (!done) {
            if (debug) {
                System.out.println("On iteration " + iterations);
                System.out.println(mixture);
            }
            mixture.estimate(set);
            double logLikelihood = 0;
            for (int j = 0; j < set.size(); j++) {
                logLikelihood += mixture.logp(set.get(j));
            }
            logLikelihood /= set.size();
            done = (iterations > 0 && Math.abs(logLikelihood - lastLogLikelihood) < tolerance)
                || (iterations + 1 >= maxIterations);
            lastLogLikelihood = logLikelihood;
            iterations++;
        }
    }

    /**
     * @see func.FunctionApproximater#value(shared.Instance)
     */
    public Instance value(Instance i) {
        return distributionFor(i).mode();
    }

    /**
     * Get the number of iterations it took
     * @return the number
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Is debug mode on
     * @return true if it is
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Set debug mode on or off
     * @param b the debug mode
     */
    public void setDebug(boolean b) {
        debug = b;
    }

    /**
     * Get the mixture
     * @return the mixture
     */
    public MixtureDistribution getMixture() {
        return mixture;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return mixture.toString();
    }


}
