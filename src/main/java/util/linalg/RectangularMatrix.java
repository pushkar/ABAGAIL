package util.linalg;

import dist.Distribution;

/**
 * A general rectangular matrix class
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RectangularMatrix extends Matrix {
	
	/**
	 * The data for the matrix
	 */
	private double[][] data;
	
	/**
	 * Create a new zeroed rectangular matrix
	 * @param m the number of rows
	 * @param n the number of columns
	 */
	public RectangularMatrix(int m, int n) {
		data = new double[m][n];
	}
	
	/**
	 * Create a new rectangular matrix with the given data
	 * @param data the data for the matrix
	 */
	public RectangularMatrix(double[][] data) {
		this.data = data;
	}
    /**
     * Make a copy of the given matrix
     * @param m the matrix
     */
    public RectangularMatrix(Matrix m) {
        data = new double[m.m()][m.n()];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = m.get(i,j);
            }
        }
    }

	/**
	 * @see linalg.Matrix#m()
	 */
	public int m() {
		return data.length;
	}

	/**
	 * @see linalg.Matrix#n()
	 */
	public int n() {
		return data[0].length;
	}

	/**
	 * @see linalg.Matrix#get(int, int)
	 */
	public double get(int i, int j) {
		return data[i][j];
	}

	/**
	 * @see linalg.Matrix#set(int, int, double)
	 */
	public void set(int i, int j, double d) {
		data[i][j] = d;
	}
	
    
	/**
	 * Get the internal representation of the data
	 * @return the data
	 */
	public double[][] getData() {
		return data;
	}

    
	/**
	 * Make a m by m identity matrix
	 * @param m the size of the matrix
	 * @return the matrix
	 */
	public static RectangularMatrix eye(int m) {
		return eye(m, m);
	}

    
	/**
	 * Make a m by n identity like matrix
	 * @param m the row size of the matrix
	 * @param n the column size of the matrix
	 * @return the matrix
	 */
	public static RectangularMatrix eye(int m, int n) {
		double[][] result = new double[m][n];
		for (int i = 0, j = 0; i < m && j < n; i++, j++) {
			result[i][j] = 1;
		}
		return new RectangularMatrix(result);
	}

    
	/**
	 * Form a block diagonal matrix whose diagonals are the given matrices
	 * @param matrices the matrices to make the diagonals of
	 * @return the matrix
	 */
	public static RectangularMatrix diag(Matrix[] matrices) {
		int m = 0, n = 0;
		for (int i = 0; i < matrices.length; i++) {
			m += matrices[i].m();
			n += matrices[i].n();
		}
		RectangularMatrix result = new RectangularMatrix(m, n);
		int row = 0, column = 0;
		for (int i = 0; i < matrices.length; i++) {
			result.set(row, column, matrices[i]);
			row += matrices[i].m();
			column += matrices[i].n();
		}
		return result;
	}
    
    
    /**
     * Make a matrix of ones
     * @param m the square size
     * @return the matrix
     */
    public static RectangularMatrix ones(int m) {
        return ones(m, m);
    }
    
    /**
     * Make a new matrix of ones
     * @param m the number of rows
     * @param n the number of columns
     * @return the matrix
     */
    public static RectangularMatrix ones(int m, int n) {
        double[][] result = new double[m][n];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = 1;
            }
        }
        return new RectangularMatrix(result);
    }
    
    /**
     * Make a random matrix
     * @param m the m and n value
     * @return the random matrix
     */
    public static RectangularMatrix random(int m) {
        return random(m, m);
    }
    
    /**
     * Make a random matrix
     * @param m the m value
     * @param n the n value
     * @return the random matrix
     */
    public static RectangularMatrix random(int m, int n) {
        double[][] data = new double[m][n];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = Distribution.random.nextDouble();                
            }
        }
        return new RectangularMatrix(data);
    }

    /**
     * Make a new column matrix
     * @param columns the columns
     * @return the column matrix
     */
    public static Matrix columns(Vector[] columns) {
        RectangularMatrix rm = new RectangularMatrix(columns[0].size(), columns.length);
        for (int i = 0; i < columns.length; i++) {
            rm.setColumn(i, columns[i]);
        }
        return rm;
    }
    
    /**
     * Make a new row matrix
     * @param rows the rows
     * @return the row matrix
     */
    public static Matrix rows(Vector[] rows) {
        RectangularMatrix rm = new RectangularMatrix(rows.length, rows[0].size());
        for (int i = 0; i < rows.length; i++) {
            rm.setRow(i, rows[i]);
        }
        return rm;
    }
    
    

}
