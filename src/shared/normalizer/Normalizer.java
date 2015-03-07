package shared.normalizer;

import shared.DataSet;

/**
 * Created with IntelliJ IDEA.
 * User: chitradip
 * Date: 3/7/15
 * Time: 12:41 PM
 */
public interface Normalizer {
    public DataSet transform(DataSet dataSet);
    public Normalizer fit(DataSet dataSet);
}
