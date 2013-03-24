package func.svm;

import shared.Instance;

/**
 * A polynomial kernel
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SigmoidKernel extends Kernel {
    /**
     * The weight of the dot product
     */
    private double dotProductWeight;
    
    /**
     * The constant added on
     */
    private double additiveConstant;
    
    /**
     * Make a new sigmoid kernel
     * @param dotProductWeight the weight to give to the dot product term
     * @param additiveConstant the additive constant
     */
    public SigmoidKernel(double dotProductWeight, double additiveConstant) {
        this.dotProductWeight = dotProductWeight;
        this.additiveConstant = additiveConstant;
    }
    
    /**
     * Make a new sigmoid kernel
     * @param addOne whether to add one to the sigmoid
     */
    public SigmoidKernel(boolean addOne) {
        this(1,0);
        if (addOne) {
            additiveConstant = 1;
        }
    }
    
    /**
     * Make a new sigmoid kernel
     */
    public SigmoidKernel() {
        this(false);
    }


    /**
     * @see svm.Kernel#value(svm.SupportVectorMachineData, svm.SupportVectorMachineData)
     */
    public double value(Instance a, Instance b) {
        return tanh(dotProductWeight * a.getData().dotProduct(b.getData()) + additiveConstant);
    }
    
    /**
     * Compute the tanh of a value
     * @param value the value
     * @return the tanh
     */
    public double tanh(double value) {
        double e2x = Math.exp(2 * value);
        if (e2x == Double.POSITIVE_INFINITY) {
            return 1;
        } else {
            return (e2x - 1) / (e2x + 1);
        }
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Polynomial Kernel tanh(" + dotProductWeight + "*K(xi,xj) + " + additiveConstant
            + ")";
    }

}