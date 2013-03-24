package func.dtree;

/**
 * A splitting criteria using GINI index
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class GINISplitEvaluator extends SplitEvaluator {

    /**
     * Calculate the GINI of an array of class probabilites
     * @param classProbabilities the probabilites
     * @return the GINI value
     */
    private double gini(double[] classProbabilities) {
        double gini = 1;
        for (int i = 0; i < classProbabilities.length; i++) {
            gini -= classProbabilities[i] * classProbabilities[i];
        }
        return gini;
    }

    /**
     * @see dtrees.SplitEvaluator#splitValue(dtrees.DecisionTreeSplitStatistics)
     */
    public double splitValue(DecisionTreeSplitStatistics stats) {
        double giniIndex = 0;
        for (int i = 0; i < stats.getBranchCount(); i++) {
            giniIndex += stats.getBranchProbability(i) *
               gini(stats.getConditionalClassProbabilities(i));
        }
        // we want to minimize the gini index
        return 1/giniIndex;
    }
}
