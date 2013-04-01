package shared.filt;
import util.linalg.Matrix;
import util.linalg.Vector;

/**
 * A helper class used in PCA and other dimensionality reduction filters.
 * 
 * This class assumes that the eigenvalues represent the % of variation captured by each
 * component, and can therefore determine how many components to keep to capture
 * a specified % of variance
 * 
 * @author Jesse Rosalia
 *
 */
public class VarianceCounter {

    private Matrix eigenValues;
    private double sum = 0;

    public VarianceCounter(Matrix eigenValues) {
        if (eigenValues.m() != eigenValues.n() || ! isDiagonal(eigenValues)) {
            throw new IllegalStateException("Expected a square diagonal matrix");
        }
        this.eigenValues = eigenValues;
        for (int ii = 0; ii < eigenValues.m(); ii++) {
            sum  += eigenValues.get(ii, ii);
        }
    }
    
    /**
     * Count from the left...this captures the biggest components first. 
     * 
     * @param varianceToKeep
     * @return The number of components to keep (from the left)
     */
    public int countLeft(double varianceToKeep) {
        int toKeep  = -1;
        double kept = 0;
        for (int ii = 0; ii < eigenValues.m(); ii++) {
            double var = eigenValues.get(ii, ii) / sum;
            if (((kept + var) - varianceToKeep) > 1e-6) {
                break;
            }
            toKeep  = ii;
            kept   += var;
        }
        
        //if we couldn't find at least one attribute, throw an exception...otherwise, the various
        // algorithms do really weird things.
        if (toKeep < 0) {
            throw new IllegalStateException(String.format("No one attribute explains <= %.02f variance", varianceToKeep));
        }
    
        return toKeep + 1; //add one, to provide a count (not an index)
    }
    
    /**
     * Count from the right...this captures the smallest components first. 
     * 
     * @param varianceToKeep
     * @return The number of components to keep (from the right)
     */
    public int countRight(double varianceToKeep) {
        int toKeep  = -1;
        double kept = 0;
        for (int ii = eigenValues.m() - 1; ii >= 0; ii--) {
            double var = eigenValues.get(ii, ii) / sum;
            if (kept + var > varianceToKeep) {
                break;
            }
            toKeep  = ii;
            kept   += var;
        }
    
        //if we couldn't find at least one attribute, throw an exception...otherwise, the various
        // algorithms do really weird things.
        if (toKeep < 0) {
            throw new IllegalStateException(String.format("No one attribute explains <= %.02f variance", varianceToKeep));
        }
    
        return eigenValues.m() - toKeep;
    }

    private boolean isDiagonal(Matrix eigenValues) {
        boolean diagonal = true;
        for (int ii = 0; ii < eigenValues.m(); ii++) {
            Vector v = eigenValues.getColumn(ii);
            if (v.sum() != v.get(ii)) {
                diagonal = false;
                break;
            }
        }
        return diagonal;
    }
}
