package shared.reader;

import java.util.ArrayList;
import java.util.Arrays;

import shared.DataSet;
import shared.Instance;

/**
 * Separates Labels into Binary representation for better use in Neural Networks
 * @author Alex Linton <https://github.com/lexlinton>
 * @date 2013-03-05
 */

public class DataSetLabelBinarySeperator {
	public static void seperateLabels(DataSet set){
		int numberOfLabels = 0;
		ArrayList<Integer> labels = new ArrayList<Integer>();
		//count up the number of distinct labels
		for(int i = 0; i < set.size(); i++){
			if(!labels.contains(new Integer(set.getInstances()[i].getLabel().getDiscrete()))){
				numberOfLabels++;
				labels.add(new Integer(set.getInstances()[i].getLabel().getDiscrete()));
			}
		}
		double[] values = new double[numberOfLabels];
		for(int i = 0; i < values.length; i++){
			values[i] = 0;
		}
		for(int i = 0; i < set.size(); i++){
			int labelValue = set.getInstances()[i].getLabel().getDiscrete()%values.length;
			values[labelValue] = 1;
			Instance instance = new Instance(Arrays.copyOf(values, values.length));
			set.get(i).setLabel(instance);
			values[labelValue] = 0;
		}
	}
}
