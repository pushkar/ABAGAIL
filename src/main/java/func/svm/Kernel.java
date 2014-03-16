package func.svm;

import shared.DataSet;
import shared.Instance;

/**
 * A kernel function for a support
 * vector machine
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class Kernel {
   
    /**
     * The examples for the support vector machine
     */
    private DataSet examples;
    
    /**
     * Create a new support vector machine kernel
     * that uses the given examples
     * @param examples the example
     */
    public Kernel(DataSet examples) {
        this.examples = examples;
    }
    
    /**
     * Default constructor
     */
    public Kernel() {     
    }
    
    /**
     * Compute the result of applying the kernel to
     * the ith and jth examples
     * @param i the index of the first example
     * @param j the index of the second example
     * @return the result
     */
    public double value(int i, int j) {
        return value(examples.get(i), examples.get(j));
    }
    
    /**
     * Compute the kernel for a stored example
     * and the given data
     * @param i the index of the example
     * @param data the data
     * @return the result
     */
    public double value(int i, Instance data) {
        return value(examples.get(i), data);
    }
    
    /**
     * Compute the kernel for two data arrays
     * @param a the first data
     * @param b the second data
     * @return the value
     */
    public abstract double value(Instance a, 
        Instance b);
    
    /**
     * Get the examples (for precomputation and caching
     * purposes)
     * @return the examples
     */
    public DataSet getExamples() {
        return examples;
    }

    /**
     * Set the examples (for precomputation and caching
     * purposes)
     * @param examples the new examples
     */
    public void setExamples(DataSet examples) {
        this.examples = examples;
    }
    
    /**
     * Clear any cached values 
     * and the stored examples
     */
    public void clear() {
        examples = null;
    }

}
