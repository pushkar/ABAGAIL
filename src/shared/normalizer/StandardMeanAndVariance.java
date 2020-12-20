package shared.normalizer;

import shared.DataSet;
import shared.Instance;
import util.linalg.Vector;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: chitradip
 * Date: 3/7/15
 * Time: 12:43 PM
 */

/**
 *
 */
public class StandardMeanAndVariance implements Normalizer {

    // private DataSet dataSet;
    private double[] means;
    private double[] stds;

    /**
     * This is there to create a normalizer in case the data is not present there
     * #fit() has to be called before use.
     */
    public StandardMeanAndVariance() {

    }

    /**
     * Calls fit() so that
     * @param dataSet used to fit the normalizer
     */
    public StandardMeanAndVariance(DataSet dataSet) {
        fit(dataSet);
    }

    @Override
    /**
     *  {@inheritDoc}
     *
     *  calculates x = (x - mean)/std across the features
     *
     */
    public DataSet transform(DataSet dataSet) {
        if ( means == null || stds == null) {
            throw new IllegalStateException("Call Normalizer.fit() before calling transform");
        }

        DataSet retSet = dataSet.copy();
        Instance[] instances = retSet.getInstances();
        final int numAttributes =instances[0].getData().size();

        for ( Instance instance : instances) {
            Vector data = instance.getData();
            for (int i = 0; i < numAttributes; i++) {
                data.set(i, (data.get(i)-means[i])/stds[i]);
            }
        }

        return retSet;
    }

    @Override
    /**
     *
     * Calculates the mean and standard deviation of the parameter
     * and stores them for future transformation
     *
     * @param dataSet
     */
    public Normalizer fit(DataSet dataSet) {

        if ( dataSet.size() == 0 ) {
            return new NoOpNormalizer();
        }

        final int numAttributes = dataSet.getInstances()[0].getData().size();
        Instance[] instances = dataSet.getInstances();
        means = new double[numAttributes];
        stds = new double[numAttributes];

        double[] tempXs = new double[numAttributes];
        Arrays.fill(tempXs, 0);

        for ( Instance instance : instances) {
            for (int i = 0; i < numAttributes; i++) {
                tempXs[i] += instance.getData().get(i);
            }
        }
        for (int i =0 ; i < numAttributes; i ++  ) {
            means[i] = tempXs[i]/instances.length;
        }

        //Reinitialze temp
        Arrays.fill(tempXs, 0);

        for ( Instance instance : instances) {
            for (int i = 0; i < numAttributes; i++) {
                double meanDiff = instance.getData().get(i) - means[i];
                tempXs[i] += meanDiff * meanDiff;
            }
        }

        for (int i =0 ; i < numAttributes; i ++  ) {
            stds[i] = Math.sqrt(tempXs[i]/instances.length);
            if ( Math.abs(stds[i]) < .0000001 ) {
                System.err.println("Warning: Changing std from 0 to 1 to prevent infinte numbers");
                stds[i] = 1;
            }
        }

        return this;

    }
}
