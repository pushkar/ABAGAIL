package shared.tester;

import shared.Instance;
import util.linalg.Vector;

/**
 * An generic utility for comparing values in Instance objects
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-05
 */
public class Comparison {

    private final Instance expected;
    private final Instance actual;
    private double epsilon = 1e-6;

    public Comparison(Instance expected, Instance actual) {
        this.expected = expected;
        this.actual   = actual;

        //sanity check...if this is not true, something is seriously wrong
        if (expected.size() != actual.size()) {
            throw new RuntimeException("Something is wrong.  Expected data size and actual data sizes are not the same.");
        }
    }

    /**
     * Quick test to see if all parts of the instance are equal.  See the comment above isCorrect
     * for more information.
     * 
     * @return
     */
    public boolean isAllCorrect() {
        boolean equals = true;
        for (int ii = 0; ii < size(); ii++) {
            if (!isCorrect(ii)) {
                equals = false;
                break;
            }
        }
        return equals;
    }

    /**
     * A generic comparison function.  This should work for continuous, discrete and boolean
     * output values, but will not make inferences for boolean or discrete values represented
     * as continuous values (e.g. 0.8 will not be considered "1" or "true").
     * 
     * @param expected The expected label or classification, stored in an Instance object.
     * @param actual The actual result returned from the classifier
     * @param index 
     * @return True if they are the same, false if they are different.
     */
    public boolean isCorrect(int index) {
        //compare the continuous values, down to 1e-6.  This accounts for any weird floating point issues, and some
        // issues when classifying discrete or boolean functions.
        return Math.abs(expected.getContinuous(index) - actual.getContinuous(index)) < epsilon;
    }
    
    /**
     * Adjust the epsilon used when comparing correctness.  Values are considered "correct"
     * if their delta is less than this value.
     * 
     * @param e
     */
    public void setEpsilon(double e) {
    	this.epsilon = e;
    }

    /**
     * Return the size of the instances being compared.
     * 
     * @return
     */
    public int size() {
        return expected.size();
    }
}
