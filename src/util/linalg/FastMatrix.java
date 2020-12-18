package util.linalg;

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
    private int stride; //this is a value divisible by 32, for cache reasons
                        //and possibly because of Strassen's algorithm, but that's unlikely
                        //and also because of block multiplies
    private int vertStride; //same thing, but vertically
    
    /**
     * Initialize the array to zeroes
     * @param m the number of rows
     * @param n the number of columns
     */
    public FastMatrix(int m, int n) {
        this.height = m;
        this.width = n;
        //these two calculations are ugly, but appear often.  Translation:
        //if (m is divisible by 32) {
        //  return m
        //} else {
        //  return the smallest integer greater than m and divisible by 32
        //}
        this.vertStride = m % 32 == 0 ? m : ((m / 32) + 1) * 32;
        this.stride = n % 32 == 0 ? n : ((n / 32) + 1) * 32;
        this.data = new double[this.stride * this.vertStride];
    }

    /**
     * Constructor taking in a 2-d array to begin with
     * @param mat the 2d array to build from
     */
    public FastMatrix(double[][] mat) {
        this.height = mat.length;
        this.width = mat[0].length;
        this.vertStride = this.height % 32 == 0 ? this.height : ((this.height / 32) + 1) * 32;
        this.stride = this.width % 32 == 0 ? this.width : ((this.width / 32) + 1) * 32;
        this.data = new double[this.stride * this.vertStride];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.data[i * this.stride + j] = mat[i][j];
            }
        }
    }

    /**
     * private constructor for use when strides are known
     * @param m height
     * @param n width
     * @param s stride
     * @param vs vertical stride
     * @param mat backing array
     */
    private FastMatrix(int m, int n, int s, int vs, double[] mat) {
        this.width = n;
        this.height = m;
        this.stride = s;
        this.vertStride = vs;
        this.data = mat;
    }

    /**
     * a constructor for when you already have a fastmat backing array
     * @param m height
     * @param n width
     * @param mat the backing array to the old matrix
     */
    public FastMatrix(int m, int n, double[] mat) {
        this.height = m;
        this.width = n;
        this.vertStride = this.height % 32 == 0 ? this.height : ((this.height / 32) + 1) * 32;
        this.stride = this.width % 32 == 0 ? this.width : ((this.width / 32) + 1) * 32;
        if (mat.length == this.vertStride * this.stride) {
            this.data = mat;
        } else if (mat.length == (this.width * this.height)) {
            double[] backing = new double[this.stride * this.vertStride];
            for (int i = 0; i < this.height; i++) {
                System.arraycopy(mat, i * this.width, backing, i * this.stride, this.width);
                this.data = backing;
            }
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
        this.vertStride = this.height % 32 == 0 ? this.height : ((this.height / 32) + 1) * 32;
        this.stride = this.width % 32 == 0 ? this.width : ((this.width / 32) + 1) * 32;
        this.data = new double[this.stride * this.vertStride];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; i++) {
                this.data[i * this.stride + j] = m.get(i,j);
            }
        }
    }

    @Override
    public FastMatrix transpose() {
        double[] arr = new double[this.stride * this.vertStride];
        for (int i = 0; i < this.width * this.vertStride; i++) {
            int y = i / this.stride;
            int x = i - y * this.stride;
            arr[x*this.stride + y] = this.data[i];
        }
        return new FastMatrix(this.width, this.height, this.stride, this.vertStride, arr);
    }

    @Override
    public Vector getColumn(int index) {
        double[] result = new double[this.height];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.data[this.stride*i + index];
        }
        return new DenseVector(result);
    }

    @Override
    public Vector getRow(int index) {
        double[] result = Arrays.copyOfRange(this.data, index * this.stride, index * this.stride + this.width);
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
        final int vs = this.vertStride;
        final int s = this.stride;
        final int os = other.stride;
        
        final double[] A = this.data; // double[m * n]
        final double[] Bt = other.transpose().data;
        final double[] C = new double[vs * os];
        
        // make transposed copy of "other" matrix to properly align with cache
        // TODO: more efficient transpose algorithm using blocking

        
        // TODO: blocked matrix multiply based on expected L1D cache size (32KB)
        // TODO: vectorization hinting by SSE-aligning strides
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < k; j++) {
                double c = 0.0;
                for (int l = 0; l < n; l++) {
                    c += A[i * s + l] * Bt[j * s + l];
                }
                C[i * os + j] = c;
            }
        }
        
        return new FastMatrix(m, k, vs, os, C);
    }

    /**
     * set a single value in the matrix
     * @param i row
     * @param j column
     * @param d the new value
     */
    public void set(int i, int j, double d) {
        this.data[i * this.stride + j] = d;
    }

    public double get(int i, int j) {
        return this.data[i * this.stride + j];
    }

    public int m() {
        return height;
    }

    public int n() {
        return width;
    }

}
