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
public class NormalizedFilter implements DataSetFilter {
    /**
     * Make a new normalized filter
     */
    public NormalizedFilter() {}

    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        if (dataSet.size() == 0) {
            return;
        }

        Vector firstData = dataSet.get(0).getData();
        int numAttrs = firstData.size();
        double[] attrMins = new double[numAttrs];
        double[] attrMaxs = new double[numAttrs];

        for (int n = 0; n < numAttrs; n ++) {
            attrMins[n] = firstData.get(n);
            attrMaxs[n] = firstData.get(n);
        }

        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            Vector data = instance.getData();

            // Find mins and maxes for each attribute
            for (int n = 0; n < numAttrs; n++) {
                double val = data.get(n);
                if (val < attrMins[n]) {
                    attrMins[n] = val;
                } else if (val > attrMaxs[n]) {
                    attrMaxs[n] = val;
                }
            }
        }

        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            Vector data = instance.getData();

            // Normalize each data point by attribute
            for (int n = 0; n < numAttrs; n++) {
                double normValue = (data.get(n) - attrMins[n]) / (attrMaxs[n] - attrMins[n]);
                data.set(n, normValue);
            }
        }

        for (int n = 0; n < numAttrs; n ++) {
            System.out.printf("[%d] MIN:%f MAX:%f\n",n, attrMins[n], attrMaxs[n]);
        }
        dataSet.setDescription(new DataSetDescription(dataSet));
    }

}
