package opt.test;

import shared.DataSet;
import shared.Instance;
import shared.filt.kFoldSplitFilter;
import shared.tester.CrossValidationTestMetric;

public class CrossValidationTest {
    public static void main(String[] args) {
        /*
        // Load in a dataset using a DataSetReader or by generating it by hand

        // Split the data into k-folds (assuming 10 here)
        kFoldSplitFilter split = new kFoldSplitFilter(10);

        // Create a metric to evaluate the results of the cross validation
        CrossValidationTestMetric metric = new CrossValidationTestMetric(dataSet.size(), 10);

        // Loop through each fold
        for (DataSet set: split.getFolds()) {

            //Run algorithm on set

            // Perform cross validation
            for (DataSet fold: split.getValidationFolds(set)) {
                for (Instance inst: fold) {
                    // Clean results (maybe thresold them to 0 or 1)
                    // outputLabel is the label that the algorithm produced 
                    metric.addResult(inst.getLabel(), new Instance(outputLabel));
                }
                // Tell the metric we're moving onto the next validation fold
                metric.nextValidationFold();
            }
            // Tell the metric we're moving onto the next training fold (i.e. training on the next fold)
            metric.nextFold();
        }

        // Output the results to screen
        metric.printResults();
        */
    }
}
