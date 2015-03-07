package shared.normalizer;

import shared.DataSet;

/**
 * Created with IntelliJ IDEA.
 * User: chitradip
 * Date: 3/7/15
 * Time: 12:48 PM
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
