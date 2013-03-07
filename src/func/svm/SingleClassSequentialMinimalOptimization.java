package func.svm;

import util.ABAGAILArrays;
import shared.DataSet;
import shared.Instance;
import shared.Trainer;

/**
 * An implementation of the SMO algorithm.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SingleClassSequentialMinimalOptimization implements Trainer {
    
    /**
     * The tolerance value
     */
    private static final double TOLERANCE = 1e-8;
    
    /**
     * The error value
     */
    private static final double EPS = 1e-8;
    
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
     * The slack value, all langrange multipliers are between
     * 0 and 1 / (v * l) where v is examples.size()
     */
    private double v;
    
    /**
     * For convience v * l
     */
    private double vl;
    
    /**
     * The weights on the support vectors
     */
    private double[] a;
    
    /**
     * The threshold subtracted when
     * evaluating the support vector machine
     */
    private double p;
    
    /**
     * Make a new SMO trainer
     * @param examples the instances to train on
     * @param kernel the kernel to use
     * @param v the slack value
     */
    public SingleClassSequentialMinimalOptimization(DataSet examples,
            Kernel kernel, double v) {
        // v can't be bigger than 1
        v = Math.min(v, 1);
        
        this.v = v;
        this.kernel = kernel;
        this.examples = examples;
        
        // set up the kernel
        kernel.clear();
        kernel.setExamples(examples);
        
        // initialize v * examples.size() of 
        // the multipliers to be 1 / (v*examples.size())
        a = new double[examples.size()];
        vl = v * examples.size();
        int ivl = (int) vl;
        int[] indices = ABAGAILArrays.indices(examples.size());
        ABAGAILArrays.permute(indices);
        for (int i = 0; i < ivl; i++) {
            a[indices[i]] = 1 / vl;
        }
        // if v * examples.size() isn't an integer, ensure
        // that the a values sum to 1
        if (ivl != vl) {
            double remainder = 1 - (1 / vl) * ivl;
            a[indices[ivl]] = remainder;
        }

        // initialize p to be the max of O_i where a_i > 0
        p = output(indices[0]);
        for (int i = 1; i < a.length && a[indices[i]] > 0; i++) {
            p = Math.max(p, output(indices[i]));
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
        // subtract the tolerance from p to make sure
        // things at the bound output 0
        p -= TOLERANCE;
        return 0;
    }
    
    /**
     * Get the created support vector machine
     * @return the support vector machine
     */
    public SingleClassSupportVectorMachine getSupportVectorMachine() {
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
        return new SingleClassSupportVectorMachine(supportSet, supporta, kernel, p);
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
        // compute the unthresholded output
        double oj = output(j);
        // if it doesn't violate the loose KTT conditions
        // just return
        if (!(((oj - p) * a[j] > TOLERANCE) 
                || ((p - oj) * (1/(vl) - a[j]) > TOLERANCE))) {
            return false;
        }
        // look for the max | oj - oi | : ai > 0 & ai < 1 / (vl)
        int i = -1; double max = 0;
        for (int k = 0; k < a.length; k++) {
            double oi = output(k);
            if (!isBound(k) && Math.abs(oj - oi) >= max) {
                max = Math.abs(oj - oi); i = k;
            }
        }
        // and try and take a optimization step
        if (i != -1 && takeStep(i, j, oj)) {
            return true;
        }
        // if the second choice hueristic fails we look
        // at all non bound indices, starting from a random point
        int startI = (int) Math.random() * a.length;
        i = startI;
        do {
            if (!isBound(i) && takeStep(i, j, oj)) {
                return true;
            }
            i = (i + 1) % a.length;
        } while (i != startI);
        // if that fails we look at all of the indices, starting from
        // a random point
        startI = (int) Math.random() * a.length;
        i = startI;
        do {
            if (takeStep(i, j, oj)) {
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
     * @param oj the unthresholded output for the second indice
     * @return true if we make progress
     */
    private final boolean takeStep(int i, int j, double oj) {
        // the indices must be different
        if (i == j) {
            return false;
        }
        // the new alpha values being computed
        double ai, aj;
        // the upper and lower bounds of the line for aj
        double l, h;
        
        // the unthresholded output for the first indice
        double oi = output(i);        
        
        // compute the kernel values
        double kii = kernel.value(i, i);
        double kij = kernel.value(i, j);
        double kjj = kernel.value(j, j);
        
        // calculate the c values, which are the outputs
        // minus the contributions from i and j
        double ci = oi - a[i] * kii - a[j] * kij;
        double cj = oj - a[i] * kij - a[j] * kjj;
        
        // calcualte delta which is 1 - the sum of all ak | k != i & k != j
        double d = a[i] + a[j];
        
        // compute the l and h
        l = Math.max(0, d - 1 / vl);
        h = Math.min(1 / vl, d);
        
        // no progress can be made
        if (l == h) {
            return false;
        }
        
        // calculate the new aj
        // unconstrained max
        aj = (d * (kii - kij) + ci - cj) / (kii + kjj - 2 * kij);
         // clip it
        if (aj < l) {
            aj = l;
        } else if (aj > h) {
            aj = h;
        }
        
        // make aj zero or 1 / vl if it is close to it
        if (aj < ZERO) {
            aj = 0;
        } else if (aj > 1 / vl - ZERO) {
            aj = 1 / vl;
        }
        
        // if there's no progress
        if (Math.abs(aj - a[j]) < EPS*(aj + a[j] + EPS)) {
            return false;
        }
        
        // set the ai value
        ai = d - aj;
        
        // make ai zero or c if it is close to it
        if (ai < ZERO) {
            ai = 0;
        } else if (ai >  1 / vl - ZERO) {
            ai = 1 / vl;
        }
        
        // set the a values
        a[i] = ai;
        a[j] = aj;
        
        // calculate the new threshold and return true
        if (!isBound(i)) {
            // ai is not bounded
            p = output(i);
            return true;
        }
        if (!isBound(j)) {
            // aj is not bounded
            p = output(j);
            return true;
        }
        // use any other non bound alpha
        int startK = (int) Math.random() * a.length;
        int k = startK;
        do {
            if (!isBound(k)) {
                p = output(j);
                return true;
            }
            k = (k + 1) % a.length;
        } while (k != startK);
        
        // everything is bound use the largest output for a non zero a
        p = Double.NEGATIVE_INFINITY;
        for  (k = 0; k < a.length; k++) {
            if (a[k] > 0) {
                p = Math.max(p, output(k));
            }
        }
        return true; 
    }

    /**
     * Check if an index is bound
     * @param i the index to check
     * @return true if it is
     */
    private final boolean isBound(int i) {
        return a[i] <= 0 || a[i] >= 1.0 / (vl);
    }

    /**
     * Evaluate the unthresholded output for an example
     * @param i the example to evaluate for
     * @return the unthresholded value
     */
    private final double output(int i) {
        double result = 0;
        for (int j = 0; j < a.length; j++) {
            if (a[j] != 0) {
                result += a[j] * kernel.value(i, j);
            }
        }
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
       String ret = "p = " + p + "\n";
       ret += "kernel = " + kernel + "\n";
       ret += examples.toString();
       return ret;
    }

}
