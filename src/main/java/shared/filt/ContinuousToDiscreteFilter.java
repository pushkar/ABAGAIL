package shared.filt;

import shared.AttributeType;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;

/**
 * A continuous to discrete filter
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ContinuousToDiscreteFilter implements DataSetFilter {
    /**
     * The number of bins
     */
    private int numberOfBins;
    
    /**
     * Make a new continuous to discrete filter
     * @param numberOfBins the number of bins
     */
    public ContinuousToDiscreteFilter(int numberOfBins) {
        this.numberOfBins = numberOfBins;
    }

    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        if (dataSet.getDescription() == null) {
            dataSet.setDescription(new DataSetDescription(dataSet));
        }
        DataSetDescription oldDescription = dataSet.getDescription();
        // for each instance
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            for (int j = 0; j < oldDescription.getAttributeCount(); j++) {
                if (oldDescription.getAttributeTypes()[j] == AttributeType.CONTINUOUS) {
                    double cv = instance.getContinuous(j);
                    int dv = (int) ((cv - oldDescription.getMin(j)) 
                        * numberOfBins / oldDescription.getRange(j));
                    instance.getData().set(j, dv);
                }
            }
        }
        
        // the description is no longer valid so generate a new one
        dataSet.setDescription(new DataSetDescription(dataSet));
        dataSet.getDescription().setLabelDescription(new DataSetDescription(dataSet.getLabelDataSet()));
    }
}
