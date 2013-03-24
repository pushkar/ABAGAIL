package func.svm;

import shared.Instance;
import util.linalg.Vector;

/**
 * A radial basis function kernel
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RBFKernel extends Kernel {

    /**
     * The sigma parameter
     */
    private double sigma;
    
    /**
     * The gamma value
     */
    private double gamma;
    
    /**
     * Make a new radial basis function kernel
     * @param sigma the sigma value
     */
    public RBFKernel(double sigma) {
        this.sigma = sigma;
        gamma = -1/(2 * sigma * sigma);
    }


    /**
     * @see svm.Kernel#value(svm.SupportVectorMachineData, svm.SupportVectorMachineData)
     */
    public double value(Instance a, Instance b) {
        Vector va = a.getData();
        Vector vb = b.getData();
        double difference = va.dotProduct(va)
            + vb.dotProduct(vb)
            - 2*va.dotProduct(vb);
        return Math.exp(gamma * difference);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "RBF Kernel sigma = " + sigma;
    }


}
