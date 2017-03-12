package shared.filt;

import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import util.linalg.Vector;

/**
 * A filter that normalizes the desired attributes. Null or empty for all attributes.
 *
 * @author Joshua Wang joshuawang@gatech.edu
 */
public class StandardizedFilter implements DataSetFilter {

    /**
     * Make a new normalized filter
     */
    public StandardizedFilter() {}

    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        if (dataSet.size() == 0) {
            return;
        }

        Vector firstData = dataSet.get(0).getData();
        int numAttrs = firstData.size();
        double[] attrSum = new double[numAttrs];
        double[] attrMean = new double[numAttrs];
        double[] attrVarSum = new double[numAttrs];
        double[] attrVar = new double[numAttrs];

        // Sum the values for each attribute
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            Vector data = instance.getData();

            for (int n = 0; n < numAttrs; n++) {
                attrSum[n] += data.get(n);
            }
        }

        for (int n = 0; n < numAttrs; n ++) {
            attrMean[n] = attrSum[n] / dataSet.size();
            System.out.printf("[%d] MEAN:%f\n",n, attrMean[n]);
        }

        // Sum the mean difference for each attribute
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            Vector data = instance.getData();

            for (int n = 0; n < numAttrs; n++) {
                attrVarSum[n] += Math.pow(data.get(n) - attrMean[n], 2.0);
            }
        }

        for (int n = 0; n < numAttrs; n ++) {
            attrVar[n] = Math.sqrt(attrVarSum[n] / dataSet.size());
            System.out.printf("[%d] VARIANCE:%f\n",n, attrVar[n]);
        }

        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            Vector data = instance.getData();

            // Standardize each data point by attribute
            for (int n = 0; n < numAttrs; n++) {
                double standardValue = (data.get(n) - attrMean[n]) / attrVarSum[n];
                data.set(n, standardValue);
            }
        }

        dataSet.setDescription(new DataSetDescription(dataSet));
    }

}
