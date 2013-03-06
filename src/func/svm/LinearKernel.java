package func.svm;

import shared.Instance;

/**
 * A linear support vector machine kernel
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LinearKernel extends Kernel {

    /**
     * @see svm.Kernel#value(shared.Instance, shared.Instance)
     */
    public double value(Instance a, Instance b) {
        return a.getData().dotProduct(b.getData());
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Linear Kernel";
    }

}
