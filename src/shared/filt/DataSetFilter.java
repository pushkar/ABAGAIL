package shared.filt;

import shared.DataSet;

/**
 * A filter for a data set
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface DataSetFilter {
    /**
     * Perform the operation on the given data set
     * @param dataSet the data set to operate on
     */
    public abstract void filter(DataSet dataSet);

}
