package shared.runner;
import shared.tester.AccuracyTestMetric;
import shared.tester.ConfusionMatrixTestMetric;
import shared.tester.RawOutputTestMetric;

/**
 * A runner for a given experiment or test.  The runner will be responsible
 * for constructing a classifier, loading the data into a DataSet, constructing
 * the necessary objects to train and test the classifier, and collecting the
 * results in a series of TestMetric objects.
 * 
 * NOTE: most of the metrics in this API refer to the last call to run, and are
 * replaced after each subsequent call.  You must call run at least once for them
 * to be valid.
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-07
 */
public interface Runner {

    /**
     * Get the accuracy metric test metric for the last run, for reporting % correct
     * and % incorrect.
     * 
     * @return
     */
    public AccuracyTestMetric getAccuracyMetric();
    
    /**
     * Get the confusion matrix for the last run.
     * 
     * @return
     */
    public ConfusionMatrixTestMetric getConfusionMatrix();
    
    /**
     * Get a name for this runner.  This is likely the name of the implementation
     * combined with the name of the data set.
     * 
     * @return
     */
    public String getName();
    
    /**
     * Get the raw output metric for the last run.
     * 
     * @return
     */
    public RawOutputTestMetric getRawOutput();
    
    /**
     * Get the training time for the last run.
     * 
     * @return
     */
    public long getTrainingTime();

    /**
     * Get the testing time for the last run.
     * 
     * @return
     */
    public long getTestTime();

    /**
     * Run the runner with the specified number of training iterations and
     * specified train/test split.
     * 
     * @param iterations The number of iterations used to train the classifier.
     * @param pctTrain The % of the data to use in a training set (the remainder is
     * in the testing set).  Note that values of 0 or 100 will result in the whole
     * set being used for training and testing.
     * 
     * @throws Exception
     */
    public void run(int iterations, int pctTrain) throws Exception;
}