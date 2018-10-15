package shared.filt;

import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import util.linalg.Vector;

/**
 * A filter that selects a specified value as the label.
 * This is useful for processing datasets with an extra
 * attribute appended to the end (such as files Weka
 * spits out with the cluster appended to each instance)
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 */
public class LabelSelectFilter implements DataSetFilter {
    /**
     * The size of the data
     */
    private int labelIndex;
    
    /**
     * Make a new label select filter
     * @param labelIndex the index of the value to use as the label
     */
    public LabelSelectFilter(int labelIndex) {
        this.labelIndex = labelIndex;
    }
    
    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        int dataCount = dataSet.get(0).size() - labelIndex;
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            Vector input = 
                instance.getData().get(0, instance.getData().size());
            double output = 
                    instance.getData().get(this.labelIndex);
            input = input.remove(this.labelIndex);
            instance.setData(input);
            instance.setLabel(new Instance(output));
        }
        dataSet.setDescription(new DataSetDescription(dataSet));
    }

}
