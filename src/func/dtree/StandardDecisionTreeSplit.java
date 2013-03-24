package func.dtree;

import shared.Instance;

/**
 * A standard decision tree split
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class StandardDecisionTreeSplit extends DecisionTreeSplit {
    
    /**
     * The attribute being split on
     */
    private int attribute;
    
    /**
     * The range of attributes for the split
     */
    private int attributeRange;
    
    /**
     * Create a new standard decision tree split
     * @param attribute the attribute being split on
     * @param attributeRange the range of attributs
     */
    public StandardDecisionTreeSplit(int attribute, int attributeRange) {
        this.attribute = attribute;
        this.attributeRange = attributeRange;
    }

    /**
     * @see dtrees.DecisionTreeSplit#getNumberOfBranches()
     */
    public int getNumberOfBranches() {
        return attributeRange;
    }

    /**
     * @see dtree.DecisionTreeSplit#getBranchOf(shared.Instance)
     */
    public int getBranchOf(Instance data) {
        return data.getDiscrete(attribute);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "attribute " + attribute;
    }

}
