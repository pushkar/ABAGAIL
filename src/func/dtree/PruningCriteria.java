package func.dtree;

/**
 * A class for deciding whether or not to prune a node
 */
public abstract class PruningCriteria {
    
    
    /**
     * Decide whether or not to prune based a node
     * @param stats the stats of the node
     * @return true if we should prune
     */
    public abstract boolean shouldPrune(DecisionTreeSplitStatistics stats);
}
