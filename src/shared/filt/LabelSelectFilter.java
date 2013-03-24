package shared.filt;

import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import util.linalg.DenseVector;
import util.linalg.Vector;

/**
 * A filter that allows you to choose which attribute of the DataSet to use as a label
 * @author Tim Swihart <https://github.com/chronoslynx>
 * @date 2013-03-24
 *
 */
public class LabelSelectFilter implements DataSetFilter {
	/**
	 * The attribute index of the label
	 */
	private int labelIndex;
	private int labelCount;
	
	public LabelSelectFilter(int i) {
		this.labelIndex = i;
		this.labelCount = 1;
	}
	
	public LabelSelectFilter() {
		this(0);
	}
	
	@Override
	public void filter(DataSet dataSet) {
		int dataCount = dataSet.get(0).size() - labelCount;
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            Vector input = new DenseVector(dataCount);
            int j = 0, k = 0;
            while(j < dataCount) {
            	if (k != labelIndex) {
            		input.set(j, instance.getData().get(k));
            		j++;
            	} 
            	k++;
            }
            instance.setData(input);
            instance.setLabel(new Instance(instance.getData().get(labelIndex)));
        }
        dataSet.setDescription(new DataSetDescription(dataSet));
	}

}
