package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * An evaluation function that scores the number of components that match, wrapped by a polynomial "gradient".
 * The score is normalized and scaled by N. Therefore the max value is N.
 * @author Joshua Wang joshuawang@gatech.edu
 * @version 1.0
 */
public class ExactInstancesEvaluationFunction implements EvaluationFunction {
    /**
     * The exact instances to match
     */
    private Instance[] exactInstances;

    /**
     * The polynomial "incline" of the evaluation
     */
    private int polyIncline;

    /**
     * Make a new function
     * @param exactInstance the exact instance to match
     * @param polyIncline the polynomial "incline" of the function
     */
    public ExactInstancesEvaluationFunction(Instance[] exactInstances, int polyIncline) {
        this.exactInstances = exactInstances;
        this.polyIncline = polyIncline;
    }

    /**
     * Make a new function
     * @param exactInstance the exact instance to match
     */
    public ExactInstancesEvaluationFunction(Instance[] exactInstances) {
        this(exactInstances, 1);
    }

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        double maxScore = 0.0;
        for (Instance exactInstance : exactInstances) {
            Vector exactData = exactInstance.getData();
            Vector data = d.getData();

            int num = Math.min(exactData.size(), data.size());
            double score = 0.0;

            for (int i = 0; i < num; i++) {
                if (data.get(i) == exactData.get(i)) {
                    score += 1.0;
                }
            }
            maxScore = Math.max(maxScore, score);
        }

        return Math.pow(maxScore, polyIncline) / Math.pow(d.size(), polyIncline - 1);
    }


}
