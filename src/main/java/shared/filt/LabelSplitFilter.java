package shared.filt;

import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import util.linalg.Vector;

/**
 * A filter that splits a data set into
 * data and labels
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LabelSplitFilter implements DataSetFilter {
    /**
     * The size of the data
     */
    private int labelCount;
    
    /**
     * Make a new label data split filter
     * @param labelCount the number of label items
     */
    public LabelSplitFilter(int labelCount) {
        this.labelCount = labelCount;
    }
    
    /**
     * Make a new label split filter
     */
    public LabelSplitFilter() {
        this(1);
    }

    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        int dataCount = dataSet.get(0).size() - labelCount;
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            Vector input = 
                instance.getData().get(0, dataCount);
            Vector output = 
                instance.getData().get(dataCount, instance.getData().size());
            instance.setData(input);
            instance.setLabel(new Instance(output));
        }
        dataSet.setDescription(new DataSetDescription(dataSet));
    }

}
