package util.linalg;



/**
 * A symmetric eigenvalue decomposition decomposes
 * a symmetric matrix A = U*D*Ut where D is a diagonal
 * matrix of eigenvalues and U is a orthonormal
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SymmetricEigenvalueDecomposition {
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
	 * Construct the singular value decomposition of
	 * the given matrix.
	 * @param a the matrix to decompose
	 */
	public SymmetricEigenvalueDecomposition(Matrix a) {
		// decompose the matrix into a tridiagonal
		TridiagonalDecomposition td = new TridiagonalDecomposition(a);
        this.u = td.getU();
		// now decompose the tridiagonal
		decompose(td.getT());
	}

	/**
	 * Perform the singular vlaue decomposition onto
	 * the bidiagonal d, accumulating the orthogonal
	 * transformations in u and v.  Applies
	 * bidiagonal QR steps until the super diaognal
	 * elements are zero.
	 */
	private void decompose(RectangularMatrix d) {
		// counters for figuring out sub matrix indices
		int q = d.n(), p = 0;
		// while there are still nonzero super/sub diagonal entries
		while (q > p + 1) {
			// set all of the super and sub diagonal elements that
			// are close to zero to zero
			for (int i = 0; i < d.n() - 1; i++) {
				if (Math.abs(d.get(i, i+1)) < ERROR) {
					d.set(i, i+1, 0);
					d.set(i+1,i, 0);
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
				// perform a single step QR shift 
				// on the sub porition
				// of the diagonal
				qrstep(d, p, q);
			}
		}
        // transpose u so that we can quickly swap rows instead of
        // swapping columns
        u = (RectangularMatrix) u.transpose();
        // sort by eigenvalues
        // bubble sort is used because the algorithm
        // used is known to give partially sorted singular values
        boolean swapped = true;
        // we need to cast into a rectangle
        // so we can swap rows
        RectangularMatrix u = (RectangularMatrix) this.u;
        for (int i = 0; i < u.m() - 1 && swapped; i++) {
             swapped = false;
             for (int j = 0; j < u.m() - 1; j++) {
                 if (d.get(j, j) < d.get(j+1,j+1)) {
                     swapped = true;
                     // swap the eigen values
                     double t = d.get(j,j);
                     d.set(j,j,d.get(j+1,j+1));
                     d.set(j+1,j+1,t);
                     // and the eigen vectors, 
                     // which are currently rows
                     double[] ta = u.getData()[j];
                     u.getData()[j] = u.getData()[j+1];
                     u.getData()[j+1] = ta;
                 }
             }
         }        
         // retranspose u
         this.u = (RectangularMatrix) u.transpose();
         // make the diagonal
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
		 // be the last element of the diagonal
        // calculate the shift value
        double dd = (d.get(ib-2,ib-2) - d.get(ib-1, ib-1)) / 2;
        double signD = dd < 0 ? -1 : 1;
        double mu = d.get(ib-1,ib-1)
            - d.get(ib-1, ib-2) * d.get(ib-1, ib-2)
            / (dd + signD * Math.sqrt(dd * dd 
                + d.get(ib-1, ib-2) * d.get(ib-1, ib-2)));
		 // the initial rotation vector
		 double y = d.get(ia, ia) - mu;
		 double z = d.get(ia+1, ia);
		 for (int i = ia; i < ib - 1; i++) {
			 // create the givens rotation for canceling out
			 // the super and sub diagonal elements
			 GivensRotation g = new GivensRotation(y, z);
			 // update the diagonal and the u matrix
			 // only affecting columns/rows i and i+1
			 g.applyRight(d, i, i+1);
			 g.applyLeft(d, i, i+1);
			 g.applyRight(u, i, i+1);
			 // if we aren't done already
			 if (i + 1 < ib - 1) {
				 y = d.get(i+1, i);
				 z = d.get(i+2, i);
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
}
