package util.linalg;

/**
 * A diagonal matrix is a matrix that only has a diagonal
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiagonalMatrix extends Matrix {
    /**
     * The m value
     */
    private int m;
    /**
     * The n value
     */
    private int n;
    /**
     * The diagonal values
     */
    private double[] diagonal;
    
    /**
     * Make a new diagonal matrix
     * @param m the matrix
     */
    public DiagonalMatrix(Matrix m) {
        this.m = m.m();
        this.n = m.n();
        this.diagonal = new double[Math.min(m.m(), m.n())];
        for (int i = 0; i < diagonal.length; i++) {
            diagonal[i] = m.get(i,i);
        }
    }
    
    /**
     * Make a new diagonal matrix
     * @param m the number of rows
     * @param n the number of columns
     * @param diagonal the values
     */
    public DiagonalMatrix(int m, int n, double[] diagonal) {
        this.m = m;
        this.n = n;
        this.diagonal = diagonal;
    }

    /**
     * @see util.linalg.Matrix#m()
     */
    public int m() {
        return m;
    }

    /**
     * @see util.linalg.Matrix#n()
     */
    public int n() {
        return n;
    }

    /**
     * @see util.linalg.Matrix#get(int, int)
     */
    public double get(int i, int j) {
        if (i == j && i < diagonal.length) {
            return diagonal[i];
        } else if (i > m || j > n) {
            throw new  UnsupportedOperationException();
        }  else {
            return 0;
        } 
    }

    /**
     * @see util.linalg.Matrix#set(int, int, double)
     */
    public void set(int i, int j, double d) {
        if (i == j && i < diagonal.length) {
            diagonal[i] = d;
        } else if (d != 0 || i > m || j > n){
            throw new UnsupportedOperationException();
        }
    }
    
    /**
     * Get the inverse
     * @return the inverse
     */
    public DiagonalMatrix inverse() {
        double[] newDiagonal = new double[diagonal.length];
        for (int i = 0; i < diagonal.length; i++) {
            newDiagonal[i] = 1/diagonal[i];
        }
        return new DiagonalMatrix(m, n, newDiagonal);
    }
    
    /**
     * Get the square root
     * @return the square root
     */
    public DiagonalMatrix squareRoot() {
        double[] newDiagonal = new double[diagonal.length];
        for (int i = 0; i < diagonal.length; i++) {
            newDiagonal[i] = Math.sqrt(diagonal[i]);
        }
        return new DiagonalMatrix(m, n, newDiagonal);
    }

}
