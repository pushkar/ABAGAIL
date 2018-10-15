package shared.filt;

import shared.AttributeType;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import util.linalg.DenseVector;

/**
 * A filter that changes attributes from discrete to binary
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteToBinaryFilter implements DataSetFilter {

    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        if (dataSet.getDescription() == null) {
            dataSet.setDescription(new DataSetDescription(dataSet));
        }
        // count how big the new data vector is
        int newAttributeCount = 0;
        DataSetDescription oldDescription = dataSet.getDescription();
        for (int i = 0; i < oldDescription.getAttributeTypes().length; i++) {
            if (oldDescription.getAttributeTypes()[i] == AttributeType.DISCRETE) {
                newAttributeCount += oldDescription.getDiscreteRange(i);
            } else {
                newAttributeCount++;
            }
        }
        if (newAttributeCount == oldDescription.getAttributeCount()) {
            return;
        }
        // for each instance
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            int k = 0;
            double[] data = new double[newAttributeCount];
            for (int j = 0; j < oldDescription.getAttributeTypes().length; j++) {
                if (oldDescription.getAttributeTypes()[j] == AttributeType.DISCRETE) {
                    data[k + instance.getDiscrete(j)] = 1;
                    k += oldDescription.getDiscreteRange(j);
                } else {
                    data[k] = instance.getContinuous(j);
                    k++;
                }
            }
            instance.setData(new DenseVector(data));
        }
        // the description is no longer valid
        dataSet.setDescription(null);
    }
    
    

}
