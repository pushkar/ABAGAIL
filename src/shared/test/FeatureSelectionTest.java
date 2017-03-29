package shared.test;

import func.dtree.*;
import shared.DataSet;
import shared.filt.DataSetFilter;
import shared.filt.DecisionTreeSelector;
import shared.filt.LabelSelectFilter;
import shared.reader.ArffDataSetReader;

import java.io.File;

/**
 * @author Joshua Wang joshuawang@gatech.edu
 */
public class FeatureSelectionTest {
    public static void main(String[] args) throws Exception {
        String dataFile = new File("").getAbsolutePath() + "/src/shared/test/spambase.arff";

        // Preprocess the data
        ArffDataSetReader dataReader = new ArffDataSetReader(dataFile);
        DataSet dataSet = dataReader.read();
        int labelIndex = dataSet.getInstances()[0].getData().size() - 1;

        // Label Split
        LabelSelectFilter lsf = new LabelSelectFilter(labelIndex);
        lsf.filter(dataSet);

        // Define our feature selector
        SplitEvaluator splitEvaluator = new InformationGainSplitEvaluator();
        PruningCriteria pruningCriteria = new ChiSquarePruningCriteria(2);
        DecisionTreeSelector featureSelector = new DecisionTreeSelector(splitEvaluator, null);

        // Select the features!
        featureSelector.filter(dataSet);
        System.out.println(featureSelector.getRelevantAttributes());
    }
}
