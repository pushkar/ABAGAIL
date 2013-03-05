package func.inst;

import java.util.Arrays;

import shared.Copyable;
import shared.Instance;

import util.linalg.DenseVector;
import util.linalg.Vector;

/**
 * A HyperRectangle class for a KDTree implementation
 * @author Andrew Guillory
 * @version 1.0
 */
public class HyperRectangle implements Copyable {

    /**
     * The min values of the rectangle
     */
    private Vector min;

    /**
     * The max values of the rectangle
     */
    private Vector max;

    /**
     * Construct a new hyper rectangle
     * @param min the array of minimum values
     * @param max the array of maximum values
     */
    public HyperRectangle(Vector min, Vector max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Create a new infinite hyper rectangle
     * @param k the dimensionality
     */
    public HyperRectangle(int k) {
        double[] maxd = new double[k];
        Arrays.fill(maxd, Double.POSITIVE_INFINITY);
        max = new DenseVector(maxd);
        double[] mind = new double[k];
        Arrays.fill(mind, Double.NEGATIVE_INFINITY);
        min = new DenseVector(mind);
    }

    /**
     * Get the minimum values
     * @return the minimum values
     */
    public Vector getMinimumValues() {
        return min;
    }

    /**
     * Set the minimum values
     * @param min the min values
     */
    public void setMinimumValues(Vector min) {
        this.min = min;
    }

    /**
     * Get the minimum values
     * @return the minimum values 
     */
    public Vector getMaximumValues() {
        return max;
    }

    /**
     * Set the maximum values
     * @param max the maximum values
     */
    public void setMaximumValues(Vector max) {
        this.max = max;
    }

    /**
     * Get the hyper rectangle split to the left
     * @return the rectangle
     */
    public HyperRectangle splitLeft(double value, int dimension) {
        HyperRectangle clone = (HyperRectangle) copy();
        clone.getMaximumValues().set(dimension, value);
        return clone;
    }

    /**
     * Get a hyper rectangle split to the right
     * @return the rectangle
     */
    public HyperRectangle splitRight(double value, int dimension) {
        HyperRectangle clone = (HyperRectangle) copy();
        clone.getMinimumValues().set(dimension, value);
        return clone;
    }

    /**
     * Get the point in this hyper cube nearest to the target
     * @param target the point you are looking for
     * @return the point nearest to the target
     */
    public Instance pointNearestTo(Instance target) {
        double[] nearest = new double[target.size()];
        for (int i = 0; i < nearest.length; i++) {
            if (target.getContinuous(i) <= min.get(i)) {
                nearest[i] = min.get(i);
            } else if (target.getContinuous(i) >= max.get(i)) {
                nearest[i] = max.get(i);
            } else {
                nearest[i] = target.getContinuous(i);
            }
        }
        return new Instance(new DenseVector(nearest));
    }
    
    /**
     * Make a copy of this recangle
     * @return the copy
     */
    public Copyable copy() {
        return new HyperRectangle((Vector) min.copy(), (Vector) max.copy());
    }
}
