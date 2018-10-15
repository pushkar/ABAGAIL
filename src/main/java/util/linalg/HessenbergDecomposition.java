package util.linalg;

/**
 * An implementation of the hessenberg decomposition
 * which decomposes a square matrix A into 
 * U * H * Ut where H is a upper hessenberg matrix
 * and U is a orthonormal matrix.  Upper hessenberg
 * matrices are upper triangular matrices with
 * nonzero values in the band directly below
 * the diagonal.  The implementation uses householder
 * matrices to zero elements.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class HessenbergDecomposition {
	
	/**
	 * The orthonormal matrix
	 */
	private RectangularMatrix u;

	/**
	 * The upper hessenberg matrix
	 */
	private RectangularMatrix h;

	/**
	 * Make a new hessenberg decomposition of
	 * the given matrix.
	 * @param a the matrix to decompose
	 */
	public HessenbergDecomposition(Matrix a) {
		h = new RectangularMatrix(a);
		u = RectangularMatrix.eye(a.m());
		decompose();
	}
	
	/**
	 * Perform the decomposition in place on
	 * h by zeroing out
	 * the sub sub diagonal elements in the first
	 * n - 2 columns of the matrix.
	 */
	private void decompose() {
		// the first n - 2 columns
		for (int i = 0; i < h.n() - 2; i++) {
			// extract the column
			Vector c  = h.getColumn(i);
			// the vector we want to reflect
			// the k+1:n elements of the column
			// into (r, 0, 0, ...) for some r
			Vector v = c.get(i+1, c.size());
			HouseholderReflection hr = new HouseholderReflection(v);
			// apply to the h matrix from the left and from the right
			hr.applyLeft(h, i+1, h.m(), i, h.n());
			hr.applyRight(h, 0, h.m(), i+1, h.n());
			// accumulate the u from the right
			hr.applyRight(u, 0, u.m(), i+1, u.n());
		}
	}

	/**
	 * Get the upper hessenberg matrix h
	 * @return the matrix h
	 */
	public RectangularMatrix getH() {
		return h;
	}

	/**
	 * Get the orthonormal matrix u
	 * @return the matrix u
	 */
	public RectangularMatrix getU() {
		return u;
	}

}
