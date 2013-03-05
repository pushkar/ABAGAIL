package util.linalg;

/**
 * A class for performing the QR decomposition with
 * householder reflections.  Decomposes a square
 * matrix A into Q*R where R is upper triangular
 * and Q is a matrix composed of a series
 * of householder reflections.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class QRDecomposition {
    /**
     * The matrix of reflections, q
     */
    private RectangularMatrix q;
    
    /**
     * The upper triangular r
     */
    private UpperTriangularMatrix r;
    
    /**
     * Construct a new QRDecomposition
     * @param matrix the matrix being decomposed
     */
    public QRDecomposition(Matrix matrix) {
        q = RectangularMatrix.eye(matrix.m());
        decompose((Matrix) matrix.copy());
    }
     
    /**
     * Run the decomposition.
     * Zeros out the elements below the diagonal with
     * householder reflections, accumulating these
     * reflections as it goes.
     * @param a the matrix to decompose
     */
    private void decompose(Matrix a) {
        int mnmin = Math.min(a.n(), a.m());
        // we have to zero out the last column if 
        // there are more rows than columns
        if (a.m() > a.n()) {
            mnmin++;
        }
        // loop through each of the columns of r
        for (int i = 0; i < mnmin - 1; i++) {
            // extract the column
            Vector column = a.getColumn(i);
            // extract out the porition we want to reflect
            // into e1
            Vector x = column.get(i, column.size());
            // calculate the householder reflection for
            // this vector
            HouseholderReflection h = new HouseholderReflection(x);
            // update the r by multiplying by it
            h.applyLeft(a, i, a.m(), i, a.n());
            // update the q as well
            h.applyRight(q, 0, q.m(), i, q.n());
        }
        r = new UpperTriangularMatrix(a);
    }

    /**
     * Get the matrix of reflections, Q
     * @return the Q matrix
     */
    public RectangularMatrix getQ() {
        return q;
    }

    /**
     * Get the decomposed, upper triangular matrix, R
     * @return the R matrix
     */
    public UpperTriangularMatrix getR() {
        return r;
    }
    
}
