package util.linalg;

import shared.Copyable;

/**
 * A class respresenting a upper triangular matrix
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class UpperTriangularMatrix extends Matrix {
	
	/**
	 * The row size
	 */
	private int m;
	
	/**
	 * The column size
	 */
	private int n;
	
	/**
	 * The internal data storage
	 */
	private double[][] data;
	
	/**
	 * Create a new square zero matrix that is
	 * restricted to be upper triangular
	 * @param m the row size of the matrix
	 */
	public UpperTriangularMatrix(int m) {
		this(m,m);
	}

	/**
	 * Create a new zeroed square matrix of size m
	 * capable of only representing upper triangular matrices
	 * @param m the row size
	 * @param n the column size
	 */
	public UpperTriangularMatrix(int m, int n) {
		this.m = m;
		this.n = n;
		data = new double[m][];
		for (int i = 0; i < data.length; i++) {
			data[i] = new double[n - i];
		}
	}
	
	/**
	 * Create a upper triangular matrix
	 * containing the upper triangular entries
	 * of the given matrix
	 * @param m the matrix to extract from
	 */
	public UpperTriangularMatrix(Matrix m) {
		this(m.m(), m.n());
		for (int i = 0; i < m(); i++) {
			for (int j = i; j < n(); j++) {
				set(i,j, m.get(i,j));
			}
		}
	}

	/**
	 * @see linalg.Matrix#m()
	 */
	public int m() {
		return m;
	}

	/**
	 * @see linalg.Matrix#n()
	 */
	public int n() {
		return n;
	}

	/**
	 * @see linalg.Matrix#get(int, int)
	 */
	public double get(int i, int j) {
		if (i < m() && j < n() && i > j) {
			return 0;
		} else {
			return data[i][j - i];
		}
	}

	/**
	 * @see linalg.Matrix#set(int, int, double)
	 */
	public void set(int i, int j, double d) {
		if (!(i < m() && j < n() && i > j && d == 0)) {
			data[i][j - i] = d;
		}
	}
	
	/**
	 * @see linalg.Matrix#transpose()
	 */
	public Matrix transpose() {
		LowerTriangularMatrix result = new LowerTriangularMatrix(n(), m());
		 for (int i = 0; i < data.length; i++) {
		 	for (int j = 0; j < data[i].length; j++) {
		 		result.set(j + i, i, data[i][j]);
		 	}
		 }
		 return result;
	}
	
	
	/**
	 * Calculate the determinant
	 * @return the determinant
	 */
	public double determinant() {
		int mnmin = Math.min(m(), n());
		double d = 1;
		for (int i = 0; i < mnmin; i++) {
			d *= get(i,i);
		}
		return d;
	}

	
	/**
	 * Solve this upper triangular system for the
	 * given vector.  Solves A*x = b for the given
	 * b.
	 * @param b the vector to solve for
	 * @return the solution vector
	 */
	public Vector solve(Vector b) {
		b = (Vector) b.copy();
		// solve with backward substitution
		// overwriting the copy of b with the solution x
		b.set(b.size() - 1, b.get(b.size() - 1) /
			get(b.size() - 1, b.size() - 1));
		for (int i = b.size() - 2; i >= 0; i--) {
			double sum = 0;
			for (int j = i+1; j < b.size(); j++) {
				sum += get(i,j)*b.get(j);	    
			}
			b.set(i, (b.get(i) - sum) / get(i,i));
		}
		return b;
	}
    
    /**
     * Find the inverse of this matrix
     * @return the inverse
     */
    public UpperTriangularMatrix inverse() {
        Vector[] columns = new Vector[n];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = solve(DenseVector.e(i, m));
        }
        return new UpperTriangularMatrix(
            RectangularMatrix.columns(columns));
    }

	/**
	 * @see linalg.Matrix#copy()
	 */
	public Copyable copy() {
		UpperTriangularMatrix result = new UpperTriangularMatrix(m(), n());
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				result.data[i][j] = data[i][j];		
			}
		}
		return result;
	}

}
