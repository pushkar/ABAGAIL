package shared.tester;

import shared.Instance;
import util.linalg.Vector;

/**
 * A test metric for accuracy.  This metric reports of % correct and % incorrect for a test run.
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-05
 */
public class AccuracyTestMetric implements TestMetric {

    private int count;    
    private int countCorrect;
    
    @Override
    public void addResult(Instance expected, Instance actual) {
        Comparison c = new Comparison(expected, actual);
        for (int ii = 0; ii < expected.size(); ii++) {
            //count up one for each instance
            count++;
            if (c.isCorrect(ii)) {
                //count up one for each correct instance
                countCorrect++;
            }
        }
    }
    
    public void printResults() {
        //only report results if there were any results to report.
        if (count > 0) {
            double pctCorrect   = ((double)countCorrect)/count;
            double pctIncorrect = (1 - pctCorrect);
            System.out.println(String.format("Correctly Classified Instances: %.02f%%",   100 * pctCorrect));
            System.out.println(String.format("Incorrectly Classified Instances: %.02f%%", 100 * pctIncorrect));
        } else {

            System.out.println("No results added.");
        }
    }
}
