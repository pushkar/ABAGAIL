package func.dtree;

import shared.Instance;

/**
 * A split in a decision tree
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class DecisionTreeSplit {

    /**
     * Get the number of branches in this split
     * @return the number of branches
     */
    public abstract int getNumberOfBranches();

    /**
     * Get the branch of the given data
     * @param d the data
     * @return the branch
     */
    public abstract int getBranchOf(Instance i);
}
