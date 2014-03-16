package util.linalg;

import shared.Copyable;

/**
 * A class representing a lower triangular matrix
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LowerTriangularMatrix extends Matrix {
	
	/**
	 * The number of rows in the matrix
	 */
	private int m;
	
	/**
	 * The number of columns in the matrix
	 */
	private int n;

	/**
	 * The data of the matrix
	 */
	private double[][] data;

	/**
	 * Create a new square zero matrix that is
	 * restricted to be lower triangular
	 * @param m the row size of the matrix
	 */
	public LowerTriangularMatrix(int m) {
		this(m,m);
	}

	/**
	 * Create a new zero matrix that is
	 * restricted to be lower triangular
	 * @param m the row size of the matrix
	 * @param n the column size of the matrix
	 */
	public LowerTriangularMatrix(int m, int n) {
		this.m = m;
		this.n = n;
		data = new double[m][];
		for (int i = 0; i < data.length; i++) {
			data[i] = new double[Math.min(i + 1, n)];
		}
	}
	
	/**
	 * Create a lower triangular matrix
	 * containing the lower triangular entries
	 * of the given matrix
	 * @param m the matrix to extract from
	 */
	public LowerTriangularMatrix(Matrix m) {
		this(m.m(), m.n());
		for (int i = 0; i < m(); i++) {
			for (int j = 0; j <= i; j++) {
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
		if (i < m() && j < n() && j > i) {
			return 0;
		} else {
			return data[i][j];
		}
	}

	/**
	 * @see linalg.Matrix#set(int, int, double)
	 */
	public void set(int i, int j, double d) {
		if (!(i < m() && j < n() && j > i && d == 0)) {
			data[i][j] = d;
		}
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
	 * @see linalg.Matrix#transpose()
	 */
	public Matrix transpose() {
		UpperTriangularMatrix result = new UpperTriangularMatrix(n(), m());
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				result.set(j, i, data[i][j]);
			}
		}
		return result;
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
		// solve by forward substiution 
		// overwriting the copy of b with the solution x
		b.set(0, b.get(0) / get(0,0));
		for (int i = 1; i < b.size(); i++) {
			double sum = 0;
			for (int j = 0; j < i; j++) {
				sum += get(i, j) * b.get(j);
			}
			b.set(i, (b.get(i) - sum) / get(i,i));
		}
		return b;
	}
    
    /**
     * Find the inverse of this matrix
     * @return the inverse
     */
    public LowerTriangularMatrix inverse() {
        Vector[] columns = new Vector[n];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = solve(DenseVector.e(i, m));
        }
        return new LowerTriangularMatrix(
            RectangularMatrix.columns(columns));
    }
	
	/**
	 * @see linalg.Matrix#copy()
	 */
	public Copyable copy() {
		LowerTriangularMatrix result = new LowerTriangularMatrix(m(), n());
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				result.data[i][j] = data[i][j];		
			}
		}
		return result;
	}

}
