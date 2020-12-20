package shared.normalizer;

import shared.DataSet;

/**
 * Created with IntelliJ IDEA.
 * User: chitradip
 * Date: 3/7/15
 * Time: 12:48 PM
 */

/**
 * This class does nothing but implement the interface.
 * This is usually returned when no data is present.
 */
public class NoOpNormalizer implements Normalizer {

    @Override
    public DataSet transform(DataSet dataSet) {
        return dataSet;
    }

    @Override
    public Normalizer fit(DataSet dataSet) {
        return this;
    }
}
