package func.dtree;

/**
 * A node in a decision tree
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DecisionTreeNode {
    
    /**
     * The split used on this node for a non leaf node, or null for a leaf
     */
    private DecisionTreeSplit split;
    
    /**
     * The statistics for the split for a non leaf node, or null for a leaf
     */
    private DecisionTreeSplitStatistics stats;

    /**
     * The child nodes for a non leaf node, or null for a leaf
     */
    private DecisionTreeNode[] nodes;
    
    /**
     * Create a new non leaf node
     * @param split the split
     * @param stast the stats
     * @param nodes the children nodes
     */
    public DecisionTreeNode(DecisionTreeSplit split,
            DecisionTreeSplitStatistics stats, DecisionTreeNode[] nodes) {
        this.split = split;
        this.stats = stats;
        this.nodes = nodes;
    }
    
    /**
     * Whether all of this node's children are null
     * @return true if they are
     */
    public boolean isLeaf() {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the split for a non leaf node
     * @return the split
     */
    public DecisionTreeSplit getSplit() {
        return split;
    }

    /**
     * Get the splist statistics for a non leaf node
     * @return the split statistics
     */
    public DecisionTreeSplitStatistics getSplitStatistics() {
        return stats;
    }

    /**
     * Get the child nodes for the decision tree
     * @return the child nodes
     */
    public DecisionTreeNode[] getNodes() {
        return nodes;
    }

    /**
     * Get a node
     * @param branch the branch to get the node for
     */
    public DecisionTreeNode getNode(int branch) {
        return nodes[branch];
    }
    
    /**
     * Get a string representation
     * @param indentation the level of indentation
     * @return the string representation
     */
    public String toString(String indentation) {
        String ret = indentation + split.toString() + "\n";
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                ret += nodes[i].toString("\t" + indentation);
            } else {
                double[] probabilities;
                if (stats.getInstanceCount(i) == 0) {
                    probabilities = stats.getClassProbabilities();
                } else {
                    probabilities = stats.getConditionalClassProbabilities(i);
                }
                ret += indentation;
                for (int j = 0; j < probabilities.length; j++) {
                    ret += probabilities[j] + " ";
                }
                ret += "\n";
            }
        }
        return ret;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return toString("");
    }

}
