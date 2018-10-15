package shared.tester;

import shared.Instance;

/**
 * A test metric for precision.
 * Precision is defined as (true positives) /(true positives + false positives).
 * Only the first value of the label is used to determine true/false.
 *
 * @author shashir
 * @date 2014-03-23
 *
 */
public class PrecisionTestMetric extends TestMetric {
   
    private int truePositives;
    private int falsePositives;
    private int totalCandidatePositives;

    @Override
    public void addResult(Instance target, Instance candidate) {
        // Sanity check.
        Comparison c = new Comparison(candidate, target);

        boolean trueCandidate = (0 == c.compare(candidate.getLabel().getContinuous(), 1.0));
        boolean trueTarget = (0 == c.compare(target.getLabel().getContinuous(), 1.0));
        if (trueCandidate && !trueTarget) {
            falsePositives++;
        } else if (trueCandidate && trueTarget) {
            truePositives++;
        }
        totalCandidatePositives = truePositives + falsePositives;
    }

    public double getPctPrecision() {
        return totalCandidatePositives > 0 ? ((double) truePositives) / totalCandidatePositives : 1; //if count is 0, we consider it all correct
    }

    public void printResults() {
        //only report results if there were any results to report.
        if (totalCandidatePositives > 0) {
            double pctPrecision = getPctPrecision();
            System.out.printf(
                    "Precision (ratio of true positives to predicted positives): %.02f%%\n",
                    100 * pctPrecision);
        } else {
            System.out.println("No results added.");
        }
    }
}
