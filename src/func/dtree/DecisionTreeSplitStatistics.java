package func.dtree;

import shared.DataSet;

/**
 * A class representing the distribution of nodes along
 * a decision tree split
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DecisionTreeSplitStatistics {
    
    /**
     * The instance counts for each of the branches
     */
    private int[] instanceCounts;
    
    /**
     * The class probabilities
     * that is classProbabilities[i] is 
     * the P(class = i)
     */
    private double[] classProbabilities;
    
    /**
     * The conditional probabilities
     * that is conditionalClassProbabilities[i][j] is
     * the P(class = j | instance is in branch i)
     */
    private double[][] conditionalClassProbabilities;
    
    /**
     * The branch probabilities
     * that is branchProbabilities[i] is
     * the P(instance is in branch i)
     */
    private double[] branchProbabilities;
    
    /**
     * Calculate statistics from the given split and instances
     * @param split the split
     * @param instances the instances split on
     * @param classRange the range of class values
     */
    public DecisionTreeSplitStatistics(DecisionTreeSplit split, 
            DataSet instances) {
        int classRange = instances.getDescription().getLabelDescription().getDiscreteRange();
        instanceCounts = new int[split.getNumberOfBranches()];
        classProbabilities = new double[classRange];
        conditionalClassProbabilities = new double[split.getNumberOfBranches()][classRange];
        branchProbabilities = new double[split.getNumberOfBranches()];
        // the sum of all of the weights
        double weightSum = 0;
        for (int i = 0; i < instances.size(); i++) {
            double weight = instances.get(i).getWeight();
            int branch = split.getBranchOf(instances.get(i));
            instanceCounts[branch]++;
            classProbabilities[instances.get(i).getLabel().getDiscrete()] += weight;
            branchProbabilities[branch] += weight;
            // we first just calculate the joint probabilities
            conditionalClassProbabilities[branch]
                [instances.get(i).getLabel().getDiscrete()] += weight;
            weightSum += weight;
        }
        // turn the unnormalized joint prob's into normalized conditional probs
        for (int i = 0; i < conditionalClassProbabilities.length; i++) {
            if (branchProbabilities[i] == 0) {
                continue;
            }
            for (int j = 0; j < conditionalClassProbabilities[i].length; j++) {
                conditionalClassProbabilities[i][j] /= branchProbabilities[i];
            }
        }
        // normalize the attribute and class arrays
        for (int i = 0; i < classProbabilities.length; i++) {
            classProbabilities[i] /= weightSum;
        }
        for (int i = 0; i < branchProbabilities.length; i++) {
            branchProbabilities[i] /= weightSum;
        }
    }
        
    /**
     * Get the branch probabilties
     * @return the branch probabilites
     */
    public double[] getBranchProbabilities() {
        return branchProbabilities;
    }
    
    /**
     * Get the number of branches
     * @return the number of branches
     */
    public int getBranchCount() {
        return branchProbabilities.length;
    }
    
    /**
     * Get a branch probabilty
     * @param branch the branch
     * @return the probability
     */
    public double getBranchProbability(int branch) {
        return branchProbabilities[branch];
    }

    /**
     * Get the class probabilties
     * @return the probabilites
     */
    public double[] getClassProbabilities() {
        return classProbabilities;
    }
    
    /**
     * Get the number of classes
     * @return the number of classes
     */
    public int getClassCount() {
        return classProbabilities.length;
    }
    
    /**
     * Get a class probabilty
     * @param c the class
     * @return the probability
     */
    public double getClassProbability(int c) {
        return classProbabilities[c];
    }

    /**
     * Get the conditional class probabilites
     * @return the probabilties
     */
    public double[][] getConditionalClassProbabilities() {
        return conditionalClassProbabilities;
    }
    
    /**
     * Get the conditional class probabilites
     * for a given branch
     * @param branc the branch
     * @return the probabilties
     */
    public double[] getConditionalClassProbabilities(int branch) {
        return conditionalClassProbabilities[branch];
    }

    /**
     * Get the instance counts
     * @return the counts
     */
    public int[] getInstanceCounts() {
        return instanceCounts;
    }
    
    /**
     * Get an instance counts for a given branch
     * @param branch the branch
     * @return the count
     */
    public int getInstanceCount(int branch) {
        return instanceCounts[branch];
    }
    
    /**
     * Get the most likely class
     * @return the most likely class
     */
    public int getMostLikelyClass() {
        int mostLikely = 0;
        for (int i = 1; i < classProbabilities.length; i++) {
            if (classProbabilities[i] > classProbabilities[mostLikely]) {
                mostLikely = i;
            }
        }
        return mostLikely;
    }

}
