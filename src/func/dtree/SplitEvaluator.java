package func.dtree;

/**
 * A criteria for splitting in a decision tree
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class SplitEvaluator {

    /**
     * Get the value of splitting a set of instances
     * along the given attribute
     * @param stats the statistics for splitting
     * @return the value
     */
    public abstract double splitValue(DecisionTreeSplitStatistics stats);

}
