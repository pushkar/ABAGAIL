package util.linalg;

/**
 * A class representing a givens rotations, which is a matrix
 * of the form G = [ ... ; cosine, ..., sine; ... ; -sine, ..., cosine; ... ],
 * a rotation of a certain angle that affects two rows of a matrix 
 * when applied to a matrix from the left A = Gt * A, and affects
 * two columns of a matrix when applied to a matrix from the right
 * A = A * G
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class GivensRotation {
    
    /**
     * The cosine of the angle
     */
    private double cosine;
    
    /**
     * The sine of the angle
     */
    private double sine;
    
    /**
     * Create a new givens rotation
     * @param theta the angle of the rotation
     */
    public GivensRotation(double theta) {
        cosine = Math.cos(theta);
        sine = Math.sin(theta);
    }

    /**
     * Create a new givens rotation which when applied
     * from the left to a column vector V = [..., a, ..., b]t
     * where a and b are the ith and jth entries in the vector
     * produces a column vector [..., r, ..., 0]t = Gt * V that has zero
     * in the jth position and some value r in the ith position.
     * @param a the entry in the ith dimension
     * @parma b the entry in the jth dimension
     */    
    public GivensRotation(double a, double b) {
        if (b == 0) {
            cosine = 1;
            sine = 0;
        } else {
            if (Math.abs(b) > Math.abs(a)) {
                double t = -a / b;
                sine = 1 / Math.sqrt(1 + t * t);
                cosine = sine * t;
            } else {
                double t = -b / a;
                cosine = 1 / Math.sqrt(1 + t * t);
                sine = cosine * t;
            }
        }
    }
    
    /**
     * Apply this rotation from the left to the given matrix.
     * Sets M = Gt * M.
     * @param m the matrix to apply the rotation to
     * @param i the first row to affect in the rotation
     * @param j the second row j > i to affect in the rotation
     */
    public void applyLeft(Matrix m, int i, int j) {
        // loop through all of the columns, only
        // affecting rows i and j
        for (int k = 0; k < m.n(); k++) {
            double vi = m.get(i, k);
            double vj = m.get(j, k);
            m.set(i,k, cosine * vi - sine*vj);
            m.set(j,k, sine * vi + cosine*vj);
        }
    }
    
    /**
     * Apply this rotation from the right to the given matrix.
     * Sets M = M * G.
     * @param m the matrix to apply the rotation to
     * @param i the first column to affect in the rotation
     * @param j the second column j > i to affect in the rotation
     */
    public void applyRight(Matrix m, int i, int j) {
        // loop through all of the rows, only
        // affecting columns i and j
        for (int k = 0; k < m.m(); k++) {
            double vi = m.get(k, i);
            double vj = m.get(k, j);
            m.set(k,i, cosine * vi - sine*vj);
            m.set(k,j, sine * vi + cosine*vj);
        }
        
    }
}
