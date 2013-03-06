package util.linalg;

/**
 * A class implementing a bidiagonal decomposition.
 * Decomposes a general matrix A into U*B*Vt
 * where B is bidiagonal and U and Vt are
 * orthonormal.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BidiagonalDecomposition {
    
    /**
     * The resulting u matrix
     */
    private RectangularMatrix u;
    
    /**
     * The resulting v matrix
     */
    private RectangularMatrix v;

    /**
     * The resulting bidiagonal matrix b's diagonal
     */
    private RectangularMatrix b;

    /** 
     * Create a bidiagonal decomposition of the given matrix
     * @param matrix the matrix to decompose
     */
    public BidiagonalDecomposition(Matrix matrix) {
        b = new RectangularMatrix(matrix);
        u = RectangularMatrix.eye(b.m());
        v = RectangularMatrix.eye(b.n());
        decompose();
    }
    
    /**
     * Perform the decomposition.
     * Uses householder reflections to zero out all of
     * subdiagonal and entries and entries to the right
     * of the superdiagonal
     */
    private void decompose() {
        int mnmin = Math.min(b.n(), b.m());
        // loop through columns of b
        for (int i = 0; i < mnmin; i++) {
            if  (i < b.m() - 1) {
                // extract the column
                Vector column = b.getColumn(i);
                // extract out the porition we want to reflect
                // into e1
                Vector x = column.get(i, column.size());
                // calculate the householder reflection for
                // this vector
                HouseholderReflection h = new HouseholderReflection(x);
                // update the b from the left
                h.applyLeft(b, i, b.m(), i, b.n());
                // and u from the right
                h.applyRight(u, 0, u.m(), i, u.n());
            }
            // zero out the row
            if (i < b.n() - 2) {
                // extract out the row we want to reflect
                Vector row = b.getRow(i);
                // extract out the porition we are going to be reflecting
                Vector x = row.get(i+1, row.size());
                // construct the householder reflection
                HouseholderReflection h = new HouseholderReflection(x);
                // update the b from the right
                h.applyRight(b, i, b.m(), i+1, b.n());
                // and v from the right
                h.applyRight(v, 0, v.m(), i+1, v.n());
            }
        } 
        // chase the extra value up the matrix (for n > m)
        if (b.n() > b.m()) {
            // for each row
            for (int i = b.m()-1; i >= 0; i--) {
                // the values we want to cancel out
                double x = b.get(i,i);
                double y = b.get(i,b.m());
                // construct the givens rotation
                // and cancel out from the right
                GivensRotation g = new GivensRotation(x, y);
                // affects only columns i and m
                g.applyRight(b, i, b.m());
                g.applyRight(v, i, b.m());
            }
        }
    }   
    
    /**
     * Get the bidiagonal matrix b
     * @return the bidiagonal
     */
    public RectangularMatrix getB() {
        return b;
    }

    /**
     * Get the orthonormal matrix  u
     * @return the orthornomal
     */
    public RectangularMatrix getU() {
        return u;
    }

    /**
     * Get the orthonormal matrix v
     * @return the orthonormal
     */
    public RectangularMatrix getV() {
        return v;
    }

}
