package util.linalg;

/**
 * A tridiagonal decomposition that decomposes
 * a symmetric matrix A into U*T*Ut where T
 * is tridiagonal and U is a orthonormal matrix.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class TridiagonalDecomposition {
	
	/**
	 * The tridiagonal matrix t
	 */
	private RectangularMatrix t;
	
	/**
	 * The orthonormal matrix
	 */
	private RectangularMatrix u;

	/**
	 * Create a new tridiagonal decomposition
	 * @param a the matrix to decompose
	 */
	public TridiagonalDecomposition(Matrix a) {
		t = new RectangularMatrix((Matrix) a.copy());
		u = RectangularMatrix.eye(a.n());
		decompose();
	}

	/**
	 * Decomposes the matrix through a series of
	 * householder reflections from the left and right.
	 * This is exactly the same as the hessenber decomposition
	 * except that symmetry is exploited to save a few
	 * flops.
	 */
	private void decompose() {
		// the first n - 2 columns
		for (int i = 0; i < t.n() - 2; i++) {
			// extract the column
			Vector c  = t.getColumn(i);
			// the vector we want to reflect
			// the k+1:n elements of the column
			// into (r, 0, 0, ...) for some r
			Vector x = c.get(i+1, c.size());
			// create the householder reflection, beta and v
			HouseholderReflection hr = new HouseholderReflection(x);
			Vector v = hr.getV();
			double beta = hr.getBeta();
			// apply to the t matrix from the left and from the right
			// applying from the left to A(i+1:n,i)
			// from the right to A(i,i+1:n)
			// and from both sides at the same time to A(i+1:n,i+1:n)
			// create w = beta*A(i,i+1:n)*v for the one sided updates
			Vector w = new DenseVector(v.size() + 1);
			for (int row = i; row < t.n(); row++) {
				for (int column = i+1; column < t.n(); column++) {
					w.set(row-i, w.get(row-i) +
						t.get(row, column) * v.get(column-(i+1))); 
				}
				w.set(row-i, w.get(row-i) * beta);
			}
			// z vector for the simulatenous updates
			// equal to p-(beta*pt*v/2)*v 
			// where p = beta*A(i+1:n,i+1:n)*v
			Vector z = w.get(1, w.size());
			z = z.minus(v.times(beta*z.dotProduct(v)/2));
			// apply the outer product update to the matrix
			// set 
			// A(i+1:n,i+1:n) = A(i+1:n,i+1:n) - v*zt - z*vt
			// A(i+1:n,i) = A(i+1,n) - v*wt
			// A(i,i+1:n) = A(i,i+1:n) - w*vt
			for (int row = i; row < t.n(); row++) {
				for (int column = i; column < t.n(); column++) {
					if (row > i && column > i) {
						t.set(row,column, t.get(row,column)
							- v.get(row-(i+1)) * z.get(column-(i+1))
							- v.get(column-(i+1)) * z.get(row-(i+1)));
					} else if (row > i) {
						t.set(row, column, t.get(row,column)
		 				  - v.get(row-(i+1)) * w.get(column-i));
					} else if (column > i) {
						t.set(row, column, t.get(row,column)
   						  - v.get(column-(i+1)) * w.get(row-i));
					}
				}
			}
			// accumulate the u from the right
			hr.applyRight(u, 0, u.m(), i+1, u.n());
		}
	}

	/**
	 * Get the tridiagonal matrix t
	 * @return the tridiagonal matrix
	 */
	public RectangularMatrix getT() {
		return t;
	}

	/**
	 * Get the orthonormal matrix u
	 * @return the orthonormal matrix
	 */
	public RectangularMatrix getU() {
		return u;
	}

}
