package shared.filt;

import shared.DataSet;
import shared.Instance;

public class TestTrainSplitFilter implements DataSetFilter {

    private double pctTrain;
    private DataSet trainingSet;
    private DataSet testingSet;

    /**
     * 
     * 
     * @param pctTrain A percentage from 0 to 100
     */
    public TestTrainSplitFilter(int pctTrain) {
        this.pctTrain = 1.0 * pctTrain / 100; //
    }

    @Override
    public void filter(DataSet dataSet) {
        int totalInstances = dataSet.getInstances().length;
        int trainInstances = (int) (totalInstances * pctTrain);
        int testInstances  = totalInstances - trainInstances;
        Instance[] train = new Instance[trainInstances];
        Instance[] test  = new Instance[testInstances];
        for (int ii = 0; ii < trainInstances; ii++) {
            train[ii] = dataSet.get(ii);
        }
        for (int ii = trainInstances; ii < totalInstances; ii++) {
            test[ii - trainInstances] = dataSet.get(ii);
        }
        
        this.trainingSet = new DataSet(train);
        this.testingSet  = new DataSet(test);
    }

    public DataSet getTrainingSet() {
        return this.trainingSet;
    }

    public DataSet getTestingSet() {
        return this.testingSet;
    }    
}
