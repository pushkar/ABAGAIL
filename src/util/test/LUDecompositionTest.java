package util.test;

import util.linalg.DenseVector;
import util.linalg.LUDecomposition;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.Vector;

/**
 * A test of the LU decomposition
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LUDecompositionTest {

    /**
     * Test main, creates a matrix and decomposes and reconstructs
     * @param args ignored
     */
    public static void main(String[] args) {
//        double[][] a = {
//            {1, 2},
//            {3, 4},
//            {5, 6}
//        };
//        double[][] a = {
//            { 1, 2, 3},
//            { 4, 5, 6}
//        };
        double[][] a = {
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 0 }            
        };
        Matrix m = new RectangularMatrix(a);    
        LUDecomposition lu = new LUDecomposition(m);
        System.out.println(m);
        System.out.println(lu.getL());
        System.out.println(lu.getU());
        System.out.println(lu.getL().times(lu.getU()));
        double[] b = {2, 4, 3};
        Vector v = new DenseVector(b);
        Vector x = lu.solve(v);
        System.out.println(x);
        System.out.println(m.times(x));
        System.out.println(lu.determinant());
    }

}
