package shared.tester;

import shared.Instance;

/**
 * A test metric for recall.
 * Recall is defined as (true positives) /(true positives + false negatives).
 * Only the first value of the label is used to determine true/false.
 *
 * @author shashir
 * @date 2014-03-23
 *
 */
public class RecallTestMetric extends TestMetric {
   
    private int truePositives;
    private int falseNegatives;
    private int totalTargetPositives;

    @Override
    public void addResult(Instance target, Instance candidate) {
        // Sanity check.
        Comparison c = new Comparison(candidate, target);

        boolean trueCandidate = (0 == c.compare(candidate.getLabel().getContinuous(), 1.0));
        boolean trueTarget = (0 == c.compare(target.getLabel().getContinuous(), 1.0));
        if (!trueCandidate && trueTarget) {
            falseNegatives++;
        } else if (trueCandidate && trueTarget) {
            truePositives++;
        }
        totalTargetPositives = truePositives + falseNegatives;
    }

    public double getPctRecall() {
        return totalTargetPositives > 0 ? ((double) truePositives) / totalTargetPositives : 1; //if count is 0, we consider it all correct
    }

    public void printResults() {
        //only report results if there were any results to report.
        if (totalTargetPositives > 0) {
            double pctRecall = getPctRecall();
            System.out.printf(
                    "Recall (ratio of true positives to target positives): %.02f%%\n",
                    100 * pctRecall);
        } else {
            System.out.println("No results added.");
        }
    }
}
