package func.svm;

import shared.Instance;

/**
 * A polynomial kernel
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class PolynomialKernel extends Kernel {
    /**
     * The weight of the dot product
     */
    private double dotProductWeight;
    
    /**
     * The constant added on
     */
    private double additiveConstant;

    /**
     * The exponent in the polynomial
     */
    private int exponent;
    
    /**
     * Make a new polynomial kernel
     * @param dotProductWeight the weight to give to the dot product term
     * @param additiveConstant the additive constant
     * @param exponent the exponent
     */
    public PolynomialKernel(double dotProductWeight, double additiveConstant,
           int exponent) {
        this.dotProductWeight = dotProductWeight;
        this.additiveConstant = additiveConstant;
        this.exponent = exponent;
    }

    /**
     * Make a new polynomial kernel
     * @param exponent the exponent
     */
    public PolynomialKernel(int exponent) {
        this(exponent, false);
    }
    
    /**
     * Make a new polynomial kernel
     * @param exponent the exponent
     * @param addOne whether to add one to the polynomial
     */
    public PolynomialKernel(int exponent, boolean addOne) {
        this(1,0,exponent);
        if (addOne) {
            additiveConstant = 1;
        }
    }

    /**
     * @see svm.Kernel#value(svm.SupportVectorMachineData, svm.SupportVectorMachineData)
     */
    public double value(Instance a, Instance b) {
        return Math.pow(dotProductWeight * a.getData().dotProduct(b.getData()) 
            + additiveConstant, exponent);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Polynomial Kernel (" + dotProductWeight + "*K(xi,xj) + " + additiveConstant
            + ")^" + exponent;
    }


}
