package shared.reader;

import java.util.ArrayList;
import java.util.Arrays;

import shared.DataSet;
import shared.Instance;
import shared.tester.Comparison;

/**
 * Separates Discrete Labels into Binary representation for better use in Neural Networks
 * @author Alex Linton <https://github.com/lexlinton>
 * @date 2013-03-05
 */

public class DataSetLabelBinarySeperator {
    
	public static void seperateLabels(DataSet set) {
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

	/**
	 * Combine separated labels into a single valued instance representing
	 *  the output label, based on the max value found in the instance.
	 *  
	 *  NOTE: This assumes labels that were split using separateLabels, and
	 *  a function that maps output values to 
	 * 
	 * @param instance
	 * @return
	 */
    public static Instance combineLabels(Instance instance) {
        //if it's already a size 1 instance, we assume it's already collapsed...otherwise
        // the code below will adversely affect the value
        if (instance.size() == 1) {
            return instance;
        }
        
        //we have values to collapse into a discrete measurement based
        // on the instance datapoint with the biggest value.  This is meant
        // to be a reversal of separateLabels
        int maxInx = -1;
        double max = 0;
        for (int ii = 0; ii < instance.size(); ii++) {
            double inst = instance.getContinuous(ii);
            if (inst > max) {
                maxInx = ii;
                max    = inst;
            }
        }
        //max will be the max value (between 0 and 1), and maxInx will be 
        return new Instance(max + maxInx - 1);
    }
}
