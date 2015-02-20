package util.linalg;

import dist.Distribution;

/**
 * A general optimized rectangular matrix class
 * @author Joshua Morton joshua.morton@gatech.edu
 * @version 1.0
 */
public class FastMatrix extends Matrix {
    
    /**
     * The data for the matrix
     */
    private double[] data;
    private int width; //n
    private int height; //m
    
    /**
     * Initialize the array to zeroes
     * @param m the number of rows
     * @param n the number of columns
     */
    public FastMatrix(int m, int n) {
        this.height = m;
        this.width = n;
        this.data = new double[m * n];
    }

    /**
     * Constructor taking in a 2-d array to begin with
     * @param mat the 2d array to build from
     */
    public FastMatrix(double[][] mat) {
        this.height = mat.length;
        this.width = mat[0].length;
        this.data = new double[this.width * this.height];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.data[i * this.width + j] = mat[i][j];
            }
        }
    }

    public FastMatrix(int m, int n, double[] mat) {
        this.height = m;
        this.width = n;
        if (mat.length == m*n) {
            this.data = mat;
        } else {
            throw new IllegalArgumentException("Array does not match size");
        }
    }

    /**
     * Constructor taking in another matrix
     * @param m - the other matrix
     */
    public FastMatrix(Matrix m) {
        this.height = m.m();
        this.width = m.n();
        this.data = new double[this.width * this.height];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; i++) {
                this.data[i * this.width + j] = m.get(i,j);
            }
        }
    }

    /**
     * set a single value in the matrix
     * @param i row
     * @param j column
     * @param d the new value
     */
    public void set(int i, int j, double d) {
        this.data[i * this.width + j] = d;
    }

    public double get(int i, int j) {
        return this.data[i * this.width + j];
    }

    public int m() {
        return height;
    }

    public int n() {
        return width;
    }

}
