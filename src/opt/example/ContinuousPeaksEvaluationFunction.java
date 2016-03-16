package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * The Continuous Peaks function, as described on page 10 of
 * <a href="https://www.ri.cmu.edu/pub_files/pub3/baluja_shumeet_1998_1/baluja_shumeet_1998_1.pdf">this paper</a>.
 * <p>
 * The function works as follows:<p><ul>
 * <li>Let <code>max0</code> = the longest consecutive run of 0s in input
 * <code>d</code>
 * <li>Let <code>max1</code> = the longest consecutive run of 1s in input
 * <code>d</code>
 * <li>If both <code>max0</code> and <code>max1</code> are greater than
 * <code>t</code> (the threshold value which one initializes this with), then
 * return <code>max(max0, max1) + N</code> where <code>n</code> is the number
 * of dimensions in <code>d</code>, otherwise, <code>max(max0, max1)</code>.
 * </ul>
 * @see <a href="https://www.ri.cmu.edu/pub_files/pub3/baluja_shumeet_1998_1/baluja_shumeet_1998_1.pdf">Pool-Wise Crossover In Genetic Algorithms: An Information-Theoretic View(Shumeet Baluja & Scott Davies)</a>
 * @see opt.test.ContinuousPeaksTest
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ContinuousPeaksEvaluationFunction implements EvaluationFunction {
    /**
     * The threshold value
     */
    private int t;
    
    /**
     * Make a new continuous peaks function.
     * @param t The threshold value (see
     * {@link ContinuousPeaksEvaluationFunction} description)
     */
    public ContinuousPeaksEvaluationFunction(int t) {
        this.t = t;
    }

    /**
     * Evaluate the continuous peaks function at value <code>d</code>.  
     * @see EvaluationFunction#value
     * @param d The input, which should be a binary vector (i.e. consist only
     * of 0s or 1s).  Label is ignored.
     */
    public double value(Instance d) {
        Vector data = d.getData();
        int max0 = 0;
        int count = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == 0) {
                count++;
            } else {
                if (count > max0) {
                    max0 = count;
                }
                count = 0;
            }
        }
        int max1 = 0;
        count = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == 1) {
                count++;
            } else {
                if (count > max1) {
                    max1 = count;
                }
                count = 0;
            }
        }
        int r = 0;
        if (max1 > t && max0 > t) {
            r = data.size();
        }
        return Math.max(max1, max0) + r;
    }
}
