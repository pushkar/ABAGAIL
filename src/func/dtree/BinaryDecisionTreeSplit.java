package func.dtree;

import shared.Instance;

/**
 * A standard decision tree split
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BinaryDecisionTreeSplit extends DecisionTreeSplit {
    
    /**
     * The attribute being split on
     */
    private int attribute;
    
    /**
     * The splitting value
     */
    private int value;
    
    /**
     * Create a new binary decision tree split
     * @param attribute the attribute being split on
     * @param value the value split on
     */
    public BinaryDecisionTreeSplit(int attribute,int value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * @see dtrees.DecisionTreeSplit#getNumberOfBranches()
     */
    public int getNumberOfBranches() {
        return 2;
    }


    /**
     * @see dtree.DecisionTreeSplit#getBranchOf(shared.Instance)
     */
    public int getBranchOf(Instance i) {
        if (i.getDiscrete(attribute) == value) {
            return 0;
        } else {
            return 1; 
        }
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "attribute " + attribute + " == " + value;
    }

}
