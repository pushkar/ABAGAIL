package shared.normalizer;

import shared.DataSet;

/**
 * Created with IntelliJ IDEA.
 * User: chitradip
 * Date: 3/7/15
 * Time: 12:41 PM
 */

/**
 * This is the interface for normalizers
 *
 */
public interface Normalizer {
    /**
     * Transforms the given dataset by the "fitted" parameters
     * @param dataSet dataset to transform
     * @return the transformed dataset.
     */
    public DataSet transform(DataSet dataSet);

    /**
     *
     * @param dataSet dataset to set the parameters of the normalizer
     * @return a normalizer that is fitted to the dataset ( usually the same one )
     */
    public Normalizer fit(DataSet dataSet);
}
