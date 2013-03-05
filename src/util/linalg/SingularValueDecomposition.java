package util.linalg;

/**
 * A class for performing singular value decompositions
 * of matrices.  For a matrix A calculates
 * U*D*Vt = A where U and V are orthonormal and
 * D is diagonal.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SingularValueDecomposition {
    /** 
     * The error threshold for the algorithm
     */
    private static final double ERROR = 1E-10;
    
    /**
     * The diagonal matrix
     */
    private DiagonalMatrix d;
    
    /**
     * The u matrix
     */
    private RectangularMatrix u;

    /**
     * The v matrix
     */
    private RectangularMatrix v;
    
    /**
     * Construct the singular value decomposition of
     * the given matrix.
     * @param a the matrix to decompose
     */
    public SingularValueDecomposition(Matrix a) {
        // decompose the matrix into a bidiagonal
        BidiagonalDecomposition bd = new BidiagonalDecomposition(a);
        v = bd.getV();
        u = (RectangularMatrix) bd.getU().transpose();
        // now decompose the bidiagonal
        decompose(bd.getB());
    }

    /**
     * Perform the singular vlaue decomposition onto
     * the bidiagonal d, accumulating the orthogonal
     * transformations in u and v.  Applies
     * bidiagonal QR steps until the super diaognal
     * elements are zero.
     */
    private void decompose(RectangularMatrix d) {
    	int mnmin = Math.min(d.m(), d.n());
        // counters for figuring out sub matrix indices
        int q = mnmin, p = 0;
        // while there are still nonzero superdiagonal entries
        while (q > p + 1) {
            // set all of the super diagonal elements that
            // are close to zero to zero
            for (int i = 0; i < mnmin - 1; i++) {
                if (Math.abs(d.get(i, i+1)) < ERROR) {
                    d.set(i, i+1, 0);
                }
            }
            // find the last non zero super diagonal
            // and set q to be i+2 where the first
            // non zero super diagonal lives at i,i+1
            q = q - 2;
            while (q >= 0 && d.get(q, q+1) == 0) {
                q--;
            }
            q = q + 2;
            // find the first non zero super diagonal
            // in the last block of non zero super diagonals
            // and set p to be i
            p = q - 2;
            while (p >= 0 && d.get(p, p+1) != 0) {
                p--;
            }
            p++;
            // the found block is then d(p:q, p:q)
            // if there's any non zero super diagonals
            if (q > p + 1) {
                // if any of the diagonal entries in the block
                // we found are zero then we set the super diagonal
                // on that row to zero as well
                boolean zeroed = false;
                for (int i = p; i < q - 1; i++) {
                    if (Math.abs(d.get(i,i)) < ERROR) {
                        d.set(i, i+1, 0);
                        zeroed = true;
                    }
                }
                // if we zeroed go back and find another block
                if (zeroed) {
                    continue;
                }
                // perform a single step QR shift 
                // on the sub porition
                // of the diagonal
                qrstep(d, p, q);
            }
        }
        // transpose v so that we can quickly swap rows instead of
        // swapping columns
        v = (RectangularMatrix) v.transpose();
        // sort by singular values
        // bubble sort is used because the algorithm
        // used is known to give partially sorted singular values
        boolean swapped = true;
        for (int i = 0; i < mnmin - 1 && swapped; i++) {
            swapped = false;
            for (int j = 0; j < mnmin - 1; j++) {
                if (d.get(j, j) < d.get(j+1,j+1)) {
                    swapped = true;
                    // swap the singular values
                    double t = d.get(j,j);
                    d.set(j,j,d.get(j+1,j+1));
                    d.set(j+1,j+1,t);
                    // and the singular vectors, 
                    // which are currently rows
                    double[] ta = v.getData()[j];
                    v.getData()[j] = v.getData()[j+1];
                    v.getData()[j+1] = ta;
                    ta = u.getData()[j];
                    u.getData()[j] = u.getData()[j+1];
                    u.getData()[j+1] = ta;
                }
            }
        }        
        // we have been accumulating u transpose
        this.u = (RectangularMatrix) u.transpose();
        // transpose v again
        this.v = (RectangularMatrix) v.transpose();
        // make the diagonal matrix
        this.d = new DiagonalMatrix(d);
    }
    
    /**
     * Perform a single step qr decomposition
     * on a sub portion of the d matrix.  Performs
     * a qr shift step on d(ia:(ib-1), ia:(ib-1), updating
     * d, u, and v in place.
     * @param ia the starting index
     * @param ib the exclusive ending index
     */
    private void qrstep(RectangularMatrix d, int ia, int ib) {
		 // choose the QR shift value
		 // which is chose here to
		 // be the last element of the implicitly
		 // calculated tridiagonal T = Bt*B
		 double mu = d.get(ib-1, ib-1)  * d.get(ib-1, ib-1)
			+ d.get(ib-1, ib-2) * d.get(ib-1, ib-2);
		 // the initial rotation seeks to
		 // zero out the first super diagonal
		 // of the matrix T = Bt*B 
		 double y = d.get(ia, ia) * d.get(ia, ia) - mu;
		 double z = d.get(ia, ia) * d.get(ia, ia + 1);
		 for (int i = ia; i < ib - 1; i++) {
			 // create the givens rotation for canceling out
			 // the super diagonal element
			 GivensRotation g = new GivensRotation(y, z);
			 // update the diagonal and the v matrix
			 // from the right, only affecting columns i and i+1
			 g.applyRight(d, i, i+1);
			 g.applyRight(v, i, i+1);
			 // now cancel out the sub diagonal
			 y = d.get(i, i);
			 z = d.get(i + 1, i);
			 g = new GivensRotation(y, z);
			 // update the diagonal from the left
			 // with the transpose of the rotation, affecting rows i and i+1
			 g.applyLeft(d, i, i+1);
			 g.applyLeft(u, i, i+1);
			 // the next value to cancel out is above
			 // the diagonal, if we aren't done already
			 if (i + 1 < ib - 1) {
				 y = d.get(i, i + 1);
				 z = d.get(i, i + 2);
			 }
		 }
    	
    }
    
    /**
     * Get the diagonal matrix
     * @return the diagonal
     */
    public DiagonalMatrix getD() {
        return d;
    }

    /**
     * Get the orthonormal u matrix
     * @return the u matrix
     */
    public RectangularMatrix getU() {
        return u;
    }

    /**
     * Get the orthonormal v matrix
     * @return the v matrix
     */
    public RectangularMatrix getV() {
        return v;
    }
    
    /**
     * Solve, in the least squares sense,
     * for the given vector b.  Finds
     * x such that the 2 norm
     * ||A*x - b|| is minimized.
     * @param b the vector to solve for
     * @return the solution
     */
    public Vector solve(Vector b) {
    	// the solution
    	double[] x = new double[v.m()];
    	// loop through i < r where r is the rank of the matrix
    	// looping through the columns of u and v,
    	// and the singular values of d
    	int i = 0;
    	int mnmin = Math.min(d.m(), d.n());
    	while (i < mnmin && Math.abs(d.get(i,i)) > ERROR) {
    		// calculate the scale factor
    		// which is the dot product of the ith column
    		// of u with b, divided by the ith singular value
    		// u_it * b / sigma_i
    		double scale = 0;
    		for (int j = 0; j < u.m(); j++) {
    			scale += u.get(j, i) * b.get(j);
    		}
    		scale /= d.get(i,i);
    		// add scale * v_i to the result vector
    		for (int j = 0; j < x.length; j++) {
    			x[j] += v.get(j,i) * scale;
    		}
    		// go on to the next column
    		i++;
    	}
    	return new DenseVector(x);
    }

}
