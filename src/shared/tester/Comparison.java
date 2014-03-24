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

    private Instance expected;
    private Instance actual;
    private double epsilon = 1e-6;

    public Comparison(Instance expected, Instance actual) {
        this.expected = expected;
        this.actual   = actual;

        //sanity check...if this is not true, something is seriously wrong
        if (expected.size() != actual.size()) {
            throw new RuntimeException("Something is wrong.  Expected data and actual data sizes are not the same.");
        }
        if (null == expected.getLabel() && null == actual.getLabel()) {
            // Do nothing. This is OK.
        } else if (expected.getLabel().size() != actual.getLabel().size()) {
            throw new RuntimeException("Something is wrong. "
                + "Expected label and actual label sizes are not the same.");
        }
    }

    /**
     * Quick test to see if all parts of the instance are equal: features and labels.
     * See the comment above isCorrect for more information.
     * 
     * @return
     */
    public boolean isAllCorrect() {
        // Check feature values.
        for (int i = 0; i < size(); i++) {
            if (!isCorrect(i)) {
                return false;
            }
        }
        // Check label values.
        for (int i = 0; i < expected.getLabel().size(); i++) {
            if (!isLabelCorrect(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * A generic comparison function.  This should work for continuous, discrete and boolean
     * output values, but will not make inferences for boolean or discrete values represented
     * as continuous values (e.g. 0.8 will not be considered "1" or "true").
     *
     * @param index 
     * @return True if they are the same, false if they are different.
     */
    public boolean isCorrect(int index) {
        //compare the continuous values, down to 1e-6.  This accounts for any weird floating point issues, and some
        // issues when classifying discrete or boolean functions.
        return 0 == compare(expected.getContinuous(index), actual.getContinuous(index));
    }

    /**
     * A generic comparison function.  This should work for continuous, discrete and boolean
     * output values, but will not make inferences for boolean or discrete values represented
     * as continuous values (e.g. 0.8 will not be considered "1" or "true").
     *
     * @param index 
     * @return True if they are the same, false if they are different.
     */
    public boolean isLabelCorrect(int index) {
        //compare the continuous values, down to 1e-6.  This accounts for any weird floating point issues, and some
        // issues when classifying discrete or boolean functions.
        return 0 == compare(expected.getLabel().getContinuous(index),
                actual.getLabel().getContinuous(index));
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
     * Compares to double values for approximate equality.
     *
     * @param x first value.
     * @param y second value.
     * @return 0 if x is appriximately y; 1 if x > y; -1 if x < y. 
     */
    public int compare(double x, double y) {
        double difference = x - y;
        if (Math.abs(difference) < epsilon) {
            return 0;
        } else if (x > y) {
            return 1;
        } else {
            return -1;
        }
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
