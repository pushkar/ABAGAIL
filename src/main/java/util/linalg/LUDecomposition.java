package util.linalg;

/**
 * A class for performing a LU decomposition.
 * Decomposes a matrix A into L*U where L is lower
 * triangular and U is upper triangular.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LUDecomposition {
    
    /**
     * The lower triangular matrix
     */
    private LowerTriangularMatrix l;
    
    /**
     * The upper triangular matrix
     */
    private UpperTriangularMatrix u;
    
    /**
     * Comput the LU Decomposition of the given matrix
     * @param a the matrix to decompose
     */
    public LUDecomposition(Matrix a) {
        decompose((Matrix) a.copy());
    }
    
    /**
     * Performs the decomposition in place
     * using gaussian elimination.
     * At the end of this decomposition copies over
     * the lower triangular elements to l and reshapes
     * u if needed.
     * @param a the matrix to decompose
     */
    private void decompose(Matrix a) {
        int mnmin = Math.min(a.m(), a.n());
        // loop through each column to be elimnated
        for (int k = 0; k < mnmin; k++) {
            // divide the column by the pivot
            double pivot = a.get(k,k);
            for (int i = k + 1; i < a.m(); i++) {
                a.set(i,k, a.get(i,k) / pivot);
            }
            // subtract out the outer product update for this step
            for (int i = k + 1; i < a.m(); i++) {
                for (int j = k + 1; j < a.n(); j++) {
                    a.set(i,j, a.get(i,j) - a.get(i,k) * a.get(k,j));
                }
            }
        }
        // create the l and u matrices
        l = new LowerTriangularMatrix(a.m(), mnmin);
        u = new UpperTriangularMatrix(mnmin, a.n());
        // copy over the elements of the l matrix
        for (int i = 0; i < l.m(); i++) {
            for (int j = Math.min(i, l.n() - 1); j >= 0; j--) {
                if (i==j) {
                    l.set(i,j, 1);
                } else {
                    l.set(i,j, a.get(i,j));
                }
            }
        }
        // and the elements of the u matrix
        for (int i = 0; i < u.m(); i++) {
            for (int j = i; j < u.n(); j++) {
                u.set(i,j, a.get(i,j));
            }
        }
    }

    /**
     * Get the lower triangular matrix
     * @return the matrix
     */
    public LowerTriangularMatrix getL() {
        return l;
    }

    /**
     * Get the upper triangular matrix
     * @return the matrix
     */
    public UpperTriangularMatrix getU() {
        return u;
    }
    
    /**
     * Calculate the determinant
     * @return the determinant
     */
    public double determinant() {
    	return u.determinant();
    }
    
    /**
     * Solve the system of linear equations
     * for the given vector.  Find the column
     * vector x such that A*x = b.
     * @param b the column vector to solve for
     * @return the solution
     */
    public Vector solve(Vector b) {
    	// first solves L*y = b
    	Vector y = l.solve(b);
    	// now solves U*x = y
		return u.solve(y);
    }

}
