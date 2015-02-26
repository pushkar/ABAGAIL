package util.linalg;

import dist.Distribution;

import java.util.Arrays;

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

    @Override
    public Matrix transpose() {
        double[] arr = new double[this.width * this.height];
        for (int i = 0; i < this.data.length; i++) {
            int y = i / this.width;
            int x = i - y * this.width;
            arr[x*this.width + y] = this.data[i];
        }
        return new FastMatrix(this.width, this.height, arr);
    }

    @Override
    public Vector getColumn(int index) {
        double[] result = new double[this.height];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.data[this.width*i + index];
        }
        return new DenseVector(result);
    }

    @Override
    public Vector getRow(int index) {
        double[] result = Arrays.copyOfRange(this.data, index * this.width, (index + 1) * this.width);
        return new DenseVector(result);
    }


    public Matrix times(FastMatrix other) {
        if (this.width != other.height) {
            throw new IllegalArgumentException("Matrix sizes do not match");
        }
        
        // this (A) is mxn, other (B) is nxk, result (C) is mxk
        
        final int m = this.height;
        final int n = this.width; // = other.height
        final int k = other.width;
        
        final double[] A = this.data; // double[m * n]
        final double[] B = other.data; // double[n * k]
        final double[] Bt = new double[k * n];
        final double[] C = new double[m * k];
        
        // make transposed copy of "other" matrix to properly align with cache
        // TODO: more efficient transpose algorithm using blocking
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                Bt[i * n + j] = B[j * k + i];
            }
        }
        
        // TODO: blocked matrix multiply based on expected L1D cache size (32KB)
        // TODO: vectorization hinting by SSE-aligning strides
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < k; j++) {
                double c = 0.0;
                for (int l = 0; l < n; l++) {
                    c += A[i * n + l] * Bt[j * n + l];
                }
                C[i * k + j] = c;
            }
        }
        
        return new FastMatrix(m, k, C);
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
