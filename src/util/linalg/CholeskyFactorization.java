package util.linalg;

/**
 * Calculates A = L*Lt where A is a symmetric
 * positive definite matrix and L is a lower triangular matrix.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class CholeskyFactorization {
	
	/**
	 * The lower triangular matrix
	 */
	private LowerTriangularMatrix l;
	
	/**
	 * The transpose of the matrix
	 */
	private UpperTriangularMatrix lt;
	
	/**
	 * Create a cholesky factorization of the given matrix
	 * @param a the matrix to factor
	 */
	public CholeskyFactorization(Matrix a) {
		l = new LowerTriangularMatrix(a);
		decompose();
	}
	
	/**
	 * Factors the matrix l inplace.
	 */
	private void decompose() {
		// loop through the diagonal of the matrix
		for (int j = 0; j < l.n(); j++) {
			// sqrt the diagonal
			l.set(j,j, Math.sqrt(l.get(j,j)));
			// divide the subdiagonal column by
			// the diagonal
			for (int i = j + 1; i < l.m(); i++) {
				l.set(i,j, l.get(i,j) / l.get(j,j));
			}
			// symmetric rank 1 update
			// subtract the crossproduct of the
			// subdiagonal column from the remaining
			// lower diagonal
			for (int jj = j + 1; jj < l.n(); jj++) {
				for (int ii = jj; ii < l.m(); ii++) {
					l.set(ii,jj, l.get(ii,jj) 
					    - l.get(ii,j) * l.get(jj,j));
				}
			}
		}
		// set the l transpose
		lt = (UpperTriangularMatrix) l.transpose();
	}

	/**
	 * Get the lower triangular matrix 
	 * @return the  matrix
	 */
	public LowerTriangularMatrix getL() {
		return l;
	}
	
	/**
	 * Get the transpose matrix
	 * @return the transpose
	 */
	public UpperTriangularMatrix getLt() {
		return lt;
	}
	
	/**
	 * Calculate the determinant
	 * @return the determinant
	 */
	public double determinant() {
		double d = l.determinant();
		return d * d;
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
		// now solves Lt*x = y
		return lt.solve(y);
	}

}
