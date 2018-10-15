package util.linalg;

/**
 * An implementation of a unsymmetric eigenvalue decomposition.
 * Decomposes a general matrix A into U*T*Ut where U
 * is a orthonormal eigenvector matrix and T is a quasi upper
 * triangular matrix whose diagonal entries are eigenvalues
 * and 2x2 blocks with complex eigenvalues.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RealSchurDecomposition {
	/** 
	 * The error threshold for the algorithm
	 */
	private static final double ERROR = 1E-10;
    /** 
     * The zero threshold for the algorithm
     */
    private static final double ZERO = 1E-6;
	
	/**
	 * The orthonormal matrix of eigenvectors.
	 */
	private RectangularMatrix u;
	
	/**
	 * The quasi upper triangular matrix
	 */
	private RectangularMatrix t;

	/**
	 * Create a new decomposition of the
	 * given matrix.
	 * @param a the matrix to decompose 
	 */
	public RealSchurDecomposition(Matrix a) {
		// decompose the matrix into upper hessenberg form
		HessenbergDecomposition hd = new HessenbergDecomposition(a);
		t = hd.getH();
		u = hd.getU();
		// decompose the matrix
		decompose();
	}
	
	/**
	 * Complete the decomposition by removing the
	 * lower diagonal entries in the upper hessenber matrix
	 * stored in t.  Uses householder reflections to
	 * perform a double shift qr step on unreduced portions
	 * of h.
	 */
	private void decompose() {
		// counters for figuring out sub matrix indices
		int q = t.n(), p = 0;
		// while there are still unreduced submatrices
		while (q > p + 1) {
			// set all of the sub diagonal elements that
			// are close to zero to zero
			for (int i = 1; i < t.n(); i++) {
				if (Math.abs(t.get(i, i-1)) < ERROR) {
					t.set(i, i-1, 0);
				}
			}
			// find the end of the last unreduced block
			// in t and set q to be the exclusive end index
			// of that block
			q = q - 1;
			// move back another index if the value
			// at i, i-1 is zero or if it is
			// part of the 2 by 2 block t(i-1:i,i-1:i)
			// with complex eigenvalues
			while (q > 0) {
				if (t.get(q, q-1) == 0) {
					q--;
				} else if ((q > 1 && t.get(q-1, q-2) == 0) || q == 1) {
					double b = t.get(q-1,q-1) + t.get(q, q);
					double aTimesC = t.get(q-1,q-1) * t.get(q,q)
					   - t.get(q-1,q) * t.get(q, q-1); 
					// if the roots of the characterestic
					// polynomial are complex keep moving
					// back else break
					if (b * b - 4 * aTimesC < 0) {
						q--;
					} else {
						break;
					}
				} else {
					break;
				}
			}
			q = q + 1;
			// set p to be the first index (inclusive) 
			// of the last unreduced block
			p = q - 1;
			while (p > 0 && t.get(p, p-1) != 0) {
				p--;
			}
			// the found block is then d(p:q, p:q)
			// if there's any unreduced blocks
			if (q > p + 1) {
				// perform a single QR shift step 
				// on the unreduced block
				if (q  >= p + 3) { 
					qrstep(p, q);
				} else {
					qrstepTwoByTwo(p);
				}
			}
		}        
	}

	/**
	 * Perform a single step of a single shift QR algorithm
	 * on a unreduced 2 by 2 matrix.  This 2 by 2 matrix
	 * should have real eigenvalues since otherwise
	 * it would be a quasi triangular matrix already.
	 * @param ia the inclusive start index of the 2 by 2 block
	 */
	private void qrstepTwoByTwo(int ia) {
		// calculate the shift value
		double d = (t.get(ia,ia) - t.get(ia+1, ia+1)) / 2;
		double signD = d < 0 ? -1 : 1;
		double mu = t.get(ia+1,ia+1)
			- t.get(ia+1, ia) * t.get(ia+1, ia)
			/ (d + signD * Math.sqrt(d * d 
				+ t.get(ia+1, ia) * t.get(ia+1, ia)));
		// the vector to cancel out
		double x = t.get(ia, ia) - mu;
		double y = t.get(ia+1, ia);
		// apply a givens rotation from the left and right
		GivensRotation gr = new GivensRotation(x, y);
		gr.applyLeft(t, ia, ia+1);
		gr.applyRight(t, ia, ia+1);
	    // accumulate the transformation
		gr.applyRight(u, ia, ia+1);
	}

	/**
	 * Perform a single step of the double shift QR algorithm.
	 * Note that this qr step assumes the matrix being reduced
	 * is at least 3 by 3.
	 * @param ia the inclusive start index of the unreduced block
	 * @param ib the exclusive end index of the unreduced block
	 */
	private void qrstep(int ia, int ib) {
		// first compute the first column of the double shifted
		// matrix (T - a_1*I)(T - a_2*I)
		// which is T^2 - a*T + b*T
		// where a and b are calculated below
		double a = t.get(ib-1, ib-1) + t.get(ib-2, ib-2);
		double b = t.get(ib-1, ib-1) * t.get(ib-2, ib-2)
		    + t.get(ib-1, ib-2) * t.get(ib-2, ib-1);
		// the first column is calculated below
		double x = t.get(ia,ia) * t.get(ia,ia)
		   + t.get(ia, ia+1) * t.get(ia+1, ia)
		   - a * t.get(ia,ia) + b;
		double y = t.get(ia+1, ia) 
			* (t.get(ia, ia) + t.get(ia+1, ia+1) - a);
		double z = t.get(ia+1, ia) * t.get(ia+2, ia+1);
		// perform a series of reflections on size 3 columns
		// chasing down the matrix
		for (int i = ia - 1; i < ib - 3; i++) {
			// the vector to be reflected and the housholder reflection
			// variables
			Vector v = new DenseVector(new double[] {x, y, z});
			HouseholderReflection hr = new HouseholderReflection(v);
			// reflect from left and right
			hr.applyLeft(t, i+1, i+4, Math.max(i, ia), t.n());
			hr.applyRight(t, 0, Math.min(i+5, ib), i+1, i+4);
			// accumulate the transformation
			hr.applyRight(u, 0, u.n(), i+1, i+4);
			// the new column to reflect
			x = t.get(i+2, i+1);
			y = t.get(i+3, i+1);
			if (i < ib - 4) {
				z = t.get(i+4, i+1);
			}
		}
		// apply the final reflection on the size 2 column
		Vector v = new DenseVector(new double[] {x, y});
		HouseholderReflection hr = new HouseholderReflection(v);
		hr.applyLeft(t, ib - 2, ib, ib - 3, t.n());
		hr.applyRight(t, 0, ib, ib - 2, ib);
		// accumulate the transformation
		hr.applyRight(u, 0, u.n(), ib - 2, ib);
	}

	/**
	 * Get the quasi upper triangular matrix t
	 * whose diagonal contains eigenvalues
	 * @return the matrix t
	 */
	public RectangularMatrix getT() {
		return t;
	}

	/**
	 * Get the orthonormal matrix of eigenvectors
	 * @return the orthonormal matrix
	 */
	public RectangularMatrix getU() {
		return u;
	}

}
