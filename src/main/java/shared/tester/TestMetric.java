package shared.tester;

import shared.Instance;
import util.linalg.Vector;

/**
 * This interface defines an API for test metrics.  Test metrics are notified by the Tester
 * after an instance is tested by the classifier.  The test metrics are given a chance to compare
 * the results and accumulate statics or measurements of error.  These data can then be printed
 * in a human readable format.
 * 
 * @author Jesse Rosalia (https://www.github.com/theJenix)
 * @date 2013-03-05
 */
public interface TestMetric {

    /**
     * Add a test result to the metric.  The metric will compare the values and
     * accumulate what data it needs.
     * 
     * @param expected The expected value (from the training set)
     * @param actual The value produced by the classifier.
     */
    public void addResult(Instance expected, Instance actual);

    /**
     * Print the values collected by this test metric.
     * 
     */
    public void printResults();
}
