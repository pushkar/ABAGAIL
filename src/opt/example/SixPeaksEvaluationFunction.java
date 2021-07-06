package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;
import opt.example.FourPeaksEvaluationFunction;

/**
 * A six peaks evaluation function from (Bonet, Isbell, and Viola, 1997)
 * @author John Mansfield
 * @version 1.0
 */
public class SixPeaksEvaluationFunction implements EvaluationFunction {

    /**
     * Function Evaluation Count
     */
    private int fEvals;

    /**
     * The t value
     */
    private int t;

    /**
     * Make a new six peaks function
     *
     * @param t the t value
     */
    public SixPeaksEvaluationFunction(int t) {
        this.fEvals = 0;
        this.t = t;
    }

    /**
     * @see opt.EvaluationFunction#value(Instance)
     */
    public double value(Instance d) {
        this.fEvals++;
        Vector data = d.getData();

        //check four peaks value
        FourPeaksEvaluationFunction fpef = new FourPeaksEvaluationFunction(this.t);
        double fourPeaksMax=fpef.value(d);

        //XOR data with 1 and check again
        for (int i=0; i<data.size(); i++){
            data.set(i, (int) data.get(i) ^ 1);
        }
        double sixPeaksMax=fpef.value(d);

        //Convert back
        for (int i=0; i<data.size(); i++){
            data.set(i, (int) data.get(i) ^ 1);
        }
        return Math.max(fourPeaksMax, sixPeaksMax);
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