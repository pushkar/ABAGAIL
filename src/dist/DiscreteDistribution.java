package dist;

import java.io.Serializable;
import java.util.Arrays;

import shared.Copyable;
import shared.DataSet;
import shared.Instance;

import util.ABAGAILArrays;
import util.linalg.Vector;


/**
 * A probabilitiy function for representing single discrete
 * output values
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteDistribution extends AbstractDistribution implements Serializable, Copyable {
	
    /**
     * The array of probabilities
     */
    private double[] probabilities;
    
    /**
     * The prior probabilities
     */
    private double[] prior;
    
    /**
     * The cummulatives
     */
    private double[] cummulatives;
    
    /**
     * The continuity parameter
     */
    private double m;
    
    /**
     * Make a new single discrete output
     * that models the given probabilities
     * @param probabilities array of probabilitites
     * such that the probabilities of output i
     * is probabilitites[i]
     * @param m the continuity parameter
     */
     public DiscreteDistribution(Vector vector) {
         this.m = vector.size();
         this.prior = new double[vector.size()];       
         Arrays.fill(prior, 1.0/vector.size());
         probabilities = new double[vector.size()];
         for (int i = 0; i < vector.size(); i++) {
             probabilities[i] = vector.get(i);
         }
         cummulatives = null;
     }
    
    /**
     * Make a new single discrete output
     * that models the given probabilities
     * @param probabilities array of probabilitites
     * such that the probabilities of output i
     * is probabilitites[i]
     * @param m the continuity parameter
     */
    public DiscreteDistribution(double[] probabilities) {
        this.probabilities = probabilities;
        this.m = probabilities.length;
        this.prior = new double[probabilities.length];       
        Arrays.fill(prior, 1.0/probabilities.length);
        cummulatives = null;
    }
     
    /**
     * Set the probabilities
     * @param probabilities the probabilities
     */
    public void setProbabilities(double[] probabilities) {
        this.probabilities = probabilities;
        cummulatives = null;
    }
    
    /**
     * Get the probabilities
     * @return the probailities
     */
    public double[] getProbabilities() {
        return probabilities;
    }

    /**
     * Get the probability of i
     * @param i the discrete value to get the probability of
     * @return the probability of i
     */
    public double p(Instance i) {
        return probabilities[i.getDiscrete()];
    }

    /**
     * Generate a discrete value consistent with the distribution
     * @return the discrete value
     */
    public Instance sample(Instance ignored) {
        if (cummulatives == null) {
            calculateCummulatives();
        }
        double rand = random.nextDouble();
        return new Instance(ABAGAILArrays.search(cummulatives, rand));  
    }
    
    /**
     * Recalculate the cummulativies
     */
    private void calculateCummulatives() {
        cummulatives = new double[probabilities.length];
        cummulatives[0] = probabilities[0];
        for (int i = 1; i < cummulatives.length; i++) {
            cummulatives[i] = cummulatives[i-1] + probabilities[i];
        }
    }
    
    /**
     * Generate the most likely value
     * @return the value
     */
    public Instance mode(Instance ignored) {
        int argMax = 0;
        for (int i = 1; i < probabilities.length; i++) {
            if (probabilities[i] > probabilities[argMax]) {
                argMax = i;
            }
        }
        return new Instance(argMax);
    }
    
    /**
     * Reestimage based on the given data set
     * @param observations the observations
     */
    public void estimate(DataSet observations) {
        double weightSum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = 0;
        }
        for (int i = 0; i < observations.size(); i++) {
            Instance cur = observations.get(i);
            weightSum += cur.getWeight();
            probabilities[cur.getDiscrete()] += cur.getWeight();
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = (probabilities[i] + m * prior[i])
                / (weightSum + m);
        }
        cummulatives = null;
    }
    
    /**
     * Get the range of values represented
     * @return the range
     */
    public int getRange() {
        return probabilities.length;
    }
    
    /**
     * Get the m value
     * @return the m value
     */
    public double getM() {
        return m;
    }

    /**
     * Set the m value
     * @param d the new m value
     */
    public void setM(double d) {
        m = d;
    }
    
    /**
     * Set the prior probability
     * @param priors the prior
     */
    public void setPrior(double[] priors) {
    		this.prior = priors;
    }
    
    /**
     * Get the prior probability
     * @return the prior probability
     */
    public double[] getPrior() {
    		return prior;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ABAGAILArrays.toString(probabilities);
    }
    

    /**
     * @see shared.Copyable#copy()
     */
    public Copyable copy() {
        double[] copyProbabilities = new double[probabilities.length];
        for (int i = 0; i < copyProbabilities.length; i++) {
            copyProbabilities[i] = probabilities[i];
        }
        DiscreteDistribution copy = new DiscreteDistribution(copyProbabilities);
        copy.setM(m);
        copy.setPrior(prior);
        return copy;
    }
    
    /**
     * Make a new single discrete output
     * that models values [0, 1, 2, ..., range - 1]
     * @param range the upper range of the output
     */
    public static DiscreteDistribution random(int range) {
        double[] probabilities = new double[range];
        // random intial values
        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = random.nextDouble();
            sum += probabilities[i];
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        return new DiscreteDistribution(probabilities);
    }
    
    /**
     * Make a new single discrete output
     * that models values [0, 1, 2, ..., range - 1]
     * @param range the upper range of the output
     */
    public static DiscreteDistribution uniform(int range) {
        double[] probabilities = new double[range];
        Arrays.fill(probabilities, 1.0/probabilities.length);
        return new DiscreteDistribution(probabilities);
    }

    



}
