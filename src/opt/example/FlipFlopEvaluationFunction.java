package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * A function that counts when ones flip to zeroes and vice versa in the data
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FlipFlopEvaluationFunction implements EvaluationFunction {

    /**
     * Function Evaluation Count
     */
    private int fEvals;

    /**
     * Make a new count ones function
     */
    public FlipFlopEvaluationFunction(){
      this.fEvals=0;
    }

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        this.fEvals++;
        Vector data = d.getData();
        double val = 0;
        for (int i = 0; i < data.size() - 1; i++) {
            if (data.get(i) != data.get(i + 1)) {
                val++;
            }
        }
        return val;
    }

    /**
     * Return function evaluation count
     * @return int fEvals
     */
    public int getFunctionEvaluations(){
      return this.fEvals;
    }

    /**
     * Reset function evaluation count
     */
    public void resetFunctionEvaluationCount(){
      this.fEvals=0;
    }
}
