package func.dtree;

/**
 * A splitting criteria that uses information gain as a basis
 * for deciding the value of a split
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class InformationGainSplitEvaluator extends SplitEvaluator {
    
    /**
     * The log of 2
     */
    private static final double LOG2 = Math.log(2);

    /**
     * Calculate the entropy of an array of class probabilites
     * @param classProbabilities the probabilites
     * @return the entropy
     */
    private double entropy(double[] classProbabilities) {
        double entropy = 0;
        for (int i = 0; i < classProbabilities.length; i++) {
            if (classProbabilities[i] != 0)
                entropy -= classProbabilities[i] 
                   * Math.log(classProbabilities[i]) / LOG2;
        }
        return entropy;
    }

    /**
     * @see dtrees.SplitEvaluator#splitValue(dtrees.DecisionTreeSplitStatistics)
     */
    public double splitValue(DecisionTreeSplitStatistics stats) {
        // the entropy before splitting
        double initialEntropy = entropy(stats.getClassProbabilities());
        // and now after
        double conditionalEntropy = 0;
        for (int i = 0; i < stats.getBranchCount(); i++) {
            conditionalEntropy += stats.getBranchProbability(i) *
                entropy(stats.getConditionalClassProbabilities(i));
        }
        // the information gain is just initial minus conditional
        return initialEntropy - conditionalEntropy;
    }



}
