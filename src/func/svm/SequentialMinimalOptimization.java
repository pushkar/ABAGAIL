package func.svm;

import util.linalg.DenseVector;
import util.linalg.Vector;
import shared.DataSet;
import shared.Instance;
import shared.Trainer;

/**
 * An implementation of the SMO algorithm.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SequentialMinimalOptimization implements Trainer {
    /**
     * The tolerance value
     */
    private static final double TOLERANCE = 1e-4;
    
    /**
     * The error value
     */
    private static final double EPS = 1e-4;
    
    /**
     * An about zero value
     */
    private static final double ZERO = 1e-8;
    
    
    /**
     * The number of iterations
     */
    private int iterations;
    
    
    /**
     * The instances
     */
    private DataSet examples;
    
    /**
     * The kernel function
     */
    private Kernel kernel;
    
    /**
     * The slack value, all alpha weights
     * must be between 0 and c inclusive
     */
    private double c;
    
    /**
     * The weights on the support vectors
     */
    private double[] a;
    
    /**
     * The threshold subtracted when
     * evaluating the support vector machine
     */
    private double b;
    
    /**
     * The error cache which is the real output
     * - the expected output for non bound examples
     * (examples whose a value is not 0 or c)
     */
    private double[] e;
    
    /**
     * The weight vector (for linear kernels)
     */
    private Vector w;
    
    /**
     * Make a new SMO trainer
     * @param examples the instances to train on
     * @param kernel the kernel to use
     * @param c the slack value
     */
    public SequentialMinimalOptimization(DataSet examples,
            Kernel kernel, double c) {
        this.c = c;
        this.kernel = kernel;
        this.examples = examples;
        // alpha values are initiallly zero
        a = new double[examples.size()];
        // as is the threshold
        b = 0;
        // all the instances are initially bound
        // so the error caches is all zero as well
        e = new double[examples.size()];
        // set up the kernel
        kernel.clear();
        kernel.setExamples(examples);
        // set up the weight vector (if linear)
        if (kernel instanceof LinearKernel) {
            w = new DenseVector(
                new double[examples.get(0).size()]);
        }
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        // number of alpha values changed this iteration
        int numChanged = 0;
        // whether or not to loop through all examples
        boolean examineAll = true;
        // the main training loop
        while (numChanged > 0 | examineAll) {
            iterations++;
            numChanged = 0;
            if (examineAll) {
                // loop through all the examples
                for (int i = 0; i < a.length; i++) {
                    if (examine(i)) {
                        numChanged++;
                    }
                }
            } else {
                // loop through all non bounded
                for (int i = 0; i < a.length; i++) {
                    if (!isBound(i) && examine(i)) {
                        numChanged++;
                    }
                }
            }
            // if we just examined all
            // we're either done or can
            // go back to only looking at non bounded examples
            // else if we didn't change anything
            // we should check everything
            if (examineAll) {
                examineAll = false;
            } else if (numChanged == 0) {
                examineAll = true;
            }
        }  
        return 0;
    }
    
    /**
     * Get the created support vector machine
     * @return the support vector machine
     */
    public SupportVectorMachine getSupportVectorMachine() {
        int supportVectorCount = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) {
                supportVectorCount++;
            }
        }
        Instance[] support = 
            new Instance[supportVectorCount];
        double[] supporta = new double[supportVectorCount];
        int j = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) {
                support[j] = examples.get(i);
                supporta[j] = a[i];
                j++;
            }
        }
        DataSet supportSet = new DataSet(support);
        supportSet.setDescription(examples.getDescription());
        return new SupportVectorMachine(supportSet, supporta, kernel, b);
    }
    
    /**
     * Get the number of iterations performed
     * @return the number of iterations
     */
    public int getNumberOfIterations() {
        return iterations;
    }
  
    /**
     * Examine an example
     * @param i the index of the example to examine
     * @return true if the example was changed
     */
    private final boolean examine(int j) {
         // we first check the loose KTT conditions for the example
        double ej = error(j);
        double rj =  ej * examples.get(j).getLabel().getPlusMinus();
        // if it doesn't violate the loose KTT conditions
        // just return
        if (!((rj < -TOLERANCE && a[j] < c) || (rj > TOLERANCE && a[j] > 0))) {
            return false;
        }
        // first we look for a second choice index, i, to take a step with
        // if ej is positive we look for the smallest error ei
        if (ej > 0) {
            int i = -1; double ei = ej;
            for (int k = 0; k < a.length; k++) {
                if (!isBound(k) && e[k] < ei) {
                    ei = e[k]; i = k;
                }
            }
            // and try and take a optimization step
            if (i != -1 && takeStep(i, j, ej)) {
                return true;
            }
        }
        // if ej is negative we look for the largest ei
        if (ej < 0) {
            int i = -1; double ei = ej;
            for (int k = 0; k < a.length; k++) {
                if (!isBound(k) && e[k] > ei) {
                    ei = e[k]; i = k;
                }
            }
            // and try and take a optimization step
            if (i != -1 && takeStep(i, j, ej)) {
                return true;
            }
        }
        // if the second choice hueristic fails we look
        // at all non bound indices, starting from a random point
        int startI = (int) Math.random() * a.length;
        int i = startI;
        do {
            if (!isBound(i) && takeStep(i, j, ej)) {
                return true;
            }
            i = (i + 1) % a.length;
        } while (i != startI);
        // if that fails we look at all of the indices, starting from
        // a random point
        startI = (int) Math.random() * a.length;
        i = startI;
        do {
            if (takeStep(i, j, ej)) {
                return true;
            }
            i = (i + 1) % a.length;
        } while (i != startI);
        // we have failed to make progress
        return false;
    }
    
    /**
     * Perform the joint optimization on
     * two indices
     * @param i the first indice
     * @param j the second
     * @param ei the error for the second indice
     * @return true if we make progress
     */
    private final boolean takeStep(int i, int j, double ej) {
        // the indices must be different
        if (i == j) {
            return false;
        }
        // the target values
        double yi = examples.get(i).getLabel().getPlusMinus(), 
            yj = examples.get(j).getLabel().getPlusMinus();
        // the new alpha values being computed
        double ai, aj;
        // the new threshold
        double bnew;
        // the upper and lower bounds of the line for aj
        double l, h;
        // the two target values multiplied together
        double s = yi * yj;
        // the error for the first index
        double ei = error(i);
        
        // depending on whether or not the target values are equal
        // compute the l and h with the appropriate formulas
        if (s < 0) {
            l = Math.max(0, a[j] - a[i]);
            h = Math.min(c, c + a[j] - a[i]);
        } else {
            l = Math.max(0, a[i] + a[j] - c);
            h = Math.min(c, a[i] + a[j]);
        }
        
        // no progress can be made
        if (l == h) {
            return false;
        }
        
        // compute the kernel values
        double kii = kernel.value(i, i);
        double kij = kernel.value(i, j);
        double kjj = kernel.value(j, j);
        // the second derivative of the objective function
        double eta = 2*kij - kii - kjj;
        
        // calculate the new aj
        // the normal case
        if (eta < 0) {
            // unconstrained max
            aj = a[j] - yj * (ei - ej) / eta;
            // clip it
            if (aj < l) {
                aj = l;
            } else if (aj > h) {
                aj = h;
            }
        } else {
            // the abnormal, case
            // actually calculate the objective function
            // at aj = l, and aj = h
            double fiold = ei + yi;
            double fjold = ej + yj;
            double vi = fiold + b - yi*a[i]*kii - yj*a[j]*kij;
            double vj = fjold + b - yi*a[i]*kij - yj*a[j]*kjj;
            double fl = a[i] + s*a[j] - s*l;
            double fh = a[i] + s*a[j] - s*h;
            double objl = fl + l - .5*kii*fl*fl - .5*kjj*l*l - s*kij*fl*l - yi*fl*vi - yj*l*vj;
            double objh = fh + h - .5*kii*fh*fh - .5*kjj*h*h - s*kij*fh*h - yi*fh*vi - yj*h*vj;
            
            if (objl > objh + EPS) {
                aj = l;
            } else if (objl < objh - EPS) {
                aj = h;
            } else {
                aj = a[j];
            }
        }
        
        // make aj zero or c if it is close to it
        if (aj < ZERO) {
            aj = 0;
        } else if (aj > c - ZERO) {
            aj = c;
        }
        
        // if there's no progress
        if (Math.abs(aj - a[j]) < EPS*(aj + a[j] + EPS)) {
            return false;
        }
        
        // set the ai value
        ai = a[i] + s*(a[j] - aj);
        
        // make ai zero or c if it is close to it
        if (ai < ZERO) {
            ai = 0;
        } else if (ai > c - ZERO) {
            ai = c;
        }
        
        // calculate the new threshold
        if (ai > 0 && ai < c) {
            // ai is not bounded
            bnew = ei + yi*(ai - a[i])*kii + yj*(aj - a[j])*kij + b;
        } else if (aj > 0 && aj < c) {
            // aj is not bounded
            bnew = ej + yi*(ai - a[i])*kij + yj*(aj - a[j])*kjj + b;
        } else {
            // all values in the range are valid, use the middle
            double bi = ei + yi*(ai - a[i])*kii + yj*(aj - a[j])*kij + b;
            double bj = ej + yi*(ai - a[i])*kij + yj*(aj - a[j])*kjj + b;
            bnew = (bi + bj) / 2;
        }
        
        // i and j are either bound now or 
        // should have their error set to zero in the cache
        if (ai > 0 && ai < c) {
            e[i] = 0;
        }
        if (aj > 0 && aj < c) {
            e[j] = 0;
        }
        
        // the deltas
        double ti = yi*(ai - a[i]);
        double tj = yj*(aj - a[j]);
        double tb = b - bnew;
        
        // update the linear vector if needed
        if (w != null) {
            w = examples.get(i).getData().times(ti).plus(w);
            w = examples.get(j).getData().times(tj).plus(w);          
        }
        
        // update the error cache
        // for non bound examples not in the cache
        for (int k = 0; k < e.length; k++) {
            if (k != i && k != j && !isBound(k)) {
                e[k] += ti*kernel.value(i,k) + tj*kernel.value(j,k) + tb;
            }
        }
       
        // finally, set the a values and the threshold
        b = bnew;
        a[i] = ai;
        a[j] = aj;
        return true; 
    }

    /**
     * Check if an index is bound
     * @param i the index to check
     * @return true if it is
     */
    private final boolean isBound(int i) {
        return a[i] <= 0 || a[i] >= c;
    }
    
    /**
     * Calculate (or look up in the cached)
     * the error for an example
     * @param i the example to look up the error for
     * @return the error
     */
    private final double error(int i) {
        // if it's not bound we use the error cache
        if (!isBound(i)) {
            return e[i];
        } else {
            return evaluate(i) - examples.get(i).getLabel().getPlusMinus();
        }
    }

    /**
     * Evaluate the support vector machine for an example
     * @param i the example to evaluate for
     * @return the evaulated value
     */
    private final double evaluate(int i) {
        // quick linear case
        if (w != null) {
            return examples.get(i).getData().dotProduct(w) - b;
        }
        // non linear slow case
        double result = 0;
        for (int j = 0; j < a.length; j++) {
            if (a[j] != 0) {
                result += examples.get(j).getLabel().getPlusMinus() 
                    * a[j] * kernel.value(i, j);
            }
        }
        result -= b;
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
       String ret = "b = " + b + "\n";
       ret += "kernel = " + kernel + "\n";
       ret += examples.toString();
       return ret;
    }

}
