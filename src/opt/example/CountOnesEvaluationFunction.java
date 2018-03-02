package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * A function that counts the ones in the data
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class CountOnesEvaluationFunction implements EvaluationFunction {
    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */

    /**
     * Track number of times value() is called.
     * Expected to be incremented by all value() calls.
     */

    public long valueCallCount = 0;

    public double value(Instance d) {
        Vector data = d.getData();
        double val = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == 1) {
                val++;
            }
        }
        this.valueCallCount += 1;
        return val;
    }
}