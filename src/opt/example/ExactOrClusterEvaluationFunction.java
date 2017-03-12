package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * An evaluation function that scores the max of (the number of components that match, the largest run of 0's up to T)
 * @author Joshua Wang joshuawang@gatech.edu
 * @version 1.0
 */
public class ExactOrClusterEvaluationFunction extends ExactInstancesEvaluationFunction {

    /** The largest run of T's to score **/
    private int T;

    /**
     * Make a new function
     * @param exactInstance the exact instance to match
     * @param polyIncline the polynomial "incline" of the exact evaluation function
     */
    public ExactOrClusterEvaluationFunction(Instance[] exactInstances, int polyIncline, int T) {
        super(exactInstances, polyIncline);
        this.T = T;
    }

    /**
     * Make a new function
     * @param exactInstance the exact instance to match
     */
    public ExactOrClusterEvaluationFunction(Instance[] exactInstances, int T) {
        this(exactInstances, 1, T);
    }

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        Vector data = d.getData();
        double exactValue = super.value(d);

        double maxRun = 0.0;
        if (data.size() > 0) {
            double currentRun = maxRun;
            for (int i = 0; i < data.size(); i++) {
                double cur = data.get(i);
                if (cur == 1) {
                    currentRun += 1;
                } else {
                    maxRun = Math.max(currentRun, maxRun);
                    currentRun = 0;
                }
            }
        }

        return Math.max(exactValue, Math.min(maxRun, T));
    }


}
