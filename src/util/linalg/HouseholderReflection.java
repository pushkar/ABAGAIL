package util.linalg;

/**
 * A class representing a householder reflection, a matrix
 * of the form P = I - beta * v * vt for some column vector
 * v and some scalar beta equal to 2/ (vt * v).
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class HouseholderReflection {
    
    /**
     * The householder vector
     */
    private Vector v;
    
    /**
     * The scalar factor beta
     */
    private double beta;
    
    /**
     * Create a new householder reflection
     * @param v the householder vector
     * @param beta the beta scalar
     */
    public HouseholderReflection(Vector v, double beta) {
        this.v = v;
        this.beta = beta;
    }
    
    /**
     * Create a new householder reflection that reflects
     * the given vector x into a column vector
     * [r, 0, 0, ...] = P*x for some value r.
     * @param x the vector to reflect into [r, 0, 0, ...]
     */
    public HouseholderReflection(Vector x) {
        v = new DenseVector(x.size());
        // take out the essential part of x, x(2:n)
        Vector ex = x.get(1, x.size());
        // the norm squared of the essential part of x
        double sigma = ex.dotProduct(ex);
        // set v to be [1, x(2:n)]
        v.set(0, 1);
        v.set(1, ex);
        // if x is a multiple of e_1 already, set beta to zero
        if (sigma == 0) {
        	beta = 0;
        } else {
        	// the norm of x
        	double mu = Math.sqrt(x.get(0) * x.get(0) + sigma);
        	// if x(1) is negative it's safe to subtract
        	// the norm of x, otherwise use a different formula
        	// to safe gaurd against x being close to a
        	// multiple of e_1
        	if (x.get(0) <= 0) {
        		v.set(0, x.get(0) - mu);
        	} else {
        		v.set(0, -sigma/ (x.get(0) + mu));
        	}
        	// calculate beta which is 2/(vt * v)
        	// and scale v such that v(1) is 1
        	beta = 2 * v.get(0) * v.get(0)
        	    / (sigma + v.get(0) * v.get(0));
        	v.timesEquals(1 / v.get(0));
        }
    }
    
    /**
     * Apply the householder reflection to the sub
     * matrix of the given matrix begining at row i, column j.
     * Sets A(i:m, j:n) = P * A(i:m, j:n) 
     * @param m the matrix to apply the reflection to
     * @param i the starting row
     * @param ib the ending row
     * @param ja the starting column
     * @param jb the ending column
     */
    public void applyLeft(Matrix m, int ia, int ib, int ja, int jb) {
        // use the fact that P*A = (I - beta*v*vt) * A
        //     = A - v*wt
        // with the vector w equal to beta * At * v
        Vector w = new DenseVector(jb - ja);
        // loop through the entries of w (the columns of the matrix)
        for (int column = ja; column < jb; column++) {
            // and the rows of the matrix (because we transposed)
            for (int row = ia; row < ib; row++) {
                w.set(column-ja, w.get(column-ja) + 
                    m.get(row, column) * v.get(row-ia)); 
            }
            w.set(column-ja, w.get(column-ja) * beta);
        }
        // apply the outer product update to the matrix
        // set A = A - v*wt
        for (int row = ia; row < ib; row++) {
            for (int column = ja; column < jb; column++) {
                m.set(row,column, m.get(row,column)
                    - v.get(row-ia) * w.get(column-ja));
            }
        }
    }

    /**
     * Apply the householder reflection to the sub
     * matrix of the given matrix begining at row i, column j.
     * Sets A(i:m, j:n) = A(i:m, j:n) * P 
     * @param m the matrix to apply the reflection to
     * @param ia the starting row
     * @parma ib the ending row
     * @param ja the starting column
     * @param jb the ending column
     */
    public void applyRight(Matrix m, int ia, int ib, int ja, int jb) {
        // use the fact that A*P = A * (I - beta*v*vt)
        //     = A - w*vt
        // with w equal to beta * A(i:m, j:n) * v
        Vector w = new DenseVector(ib - ia);
        // loop through the entries of w (the rows of the matrix)
        for (int row = ia; row < ib; row++) {
            // and the columns of the matrix 
            for (int column = ja; column < jb; column++) {
                w.set(row-ia, w.get(row-ia) +
                    m.get(row, column) * v.get(column-ja)); 
            }
            w.set(row-ia, w.get(row-ia) * beta);
        }
        // apply the outer product update to the matrix
        // set A = A - w*vt
        for (int row = ia; row < ib; row++) {
            for (int column = ja; column < jb; column++) {
                m.set(row,column, m.get(row,column)
                    - w.get(row-ia) * v.get(column-ja));
            }
        }
    }


    /**
     * Get the scale factor
     * @return the scale
     */
    public double getBeta() {
        return beta;
    }

    /**
     * Get the householder vector
     * @return the householder vector
     */
    public Vector getV() {
        return v;
    }

}
