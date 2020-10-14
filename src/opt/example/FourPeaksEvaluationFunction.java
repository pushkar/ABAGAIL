package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * A four peaks evaluation function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FourPeaksEvaluationFunction implements EvaluationFunction {

    /**
     * Function Evaluation Count
     */
    private int fEvals;

    /**
     * The t value
     */
    private int t;

    /**
     * Make a new four peaks function
     * @param t the t value
     */
    public FourPeaksEvaluationFunction(int t) {
      this.fEvals=0;
      this.t = t;
    }

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        this.fEvals++;
        Vector data = d.getData();
        int i = 0;
        while (i < data.size() && data.get(i) == 1) {
            i++;
        }
        int head = i;
        i = data.size() - 1;
        while (i >= 0 && data.get(i) == 0) {
            i--;
        }
        int tail = data.size() - 1 - i;
        int r = 0;
        if (head > t && tail > t) {
            r = data.size();
        }
        return Math.max(tail, head) + r;
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
