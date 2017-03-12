package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * An evaluation function that scores the number of ones if the first T bits are ones, and the number of zeros otherwise.
 * @author Joshua Wang joshuawang@gatech.edu
 * @version 1.0
 */
public class OnesBasinEvaluationFunction implements EvaluationFunction {

    /** The number of ones to match for the global optimum **/
    private int T;

    /**
     * Make a new function
     * @param T the number of ones to match
     */
    public OnesBasinEvaluationFunction(int T) {
        this.T = T;
    }

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        int numOnes = 0;
        int numZeros = 0;
        int maxOnes = 0;
        int maxZeros = 0;
        int leadingOnes = 0;
        boolean matchedOnes = true;

        Vector data = d.getData();
        int num = d.size();
        for (int i = 0; i < num; i++) {
            if (data.get(i) != 1) {
                if (i < T) {
                    matchedOnes = false;
                }
                maxOnes = Math.max(maxOnes, numOnes);
                numOnes = 0;
                numZeros += 1;
            } else {
                if (maxZeros == 0) {
                    leadingOnes += 1;
                }
                maxZeros = Math.max(maxZeros, numZeros);
                numZeros = 0;
                numOnes += 1;
            }
        }

        double localOptimum = num * 0.5;
        if (matchedOnes) {
            return localOptimum + maxOnes;
        }
        return (Math.max(T - leadingOnes, 0) / T) * localOptimum;
    }


}
