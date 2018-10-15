package util.test;

import util.linalg.BidiagonalDecomposition;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;

/**
 * A test of the bidiagonal decomposition
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class BidiagonalDecompositionTest {
    
    /**
     * Test main, creates a matrix and decomposes and reconstructs
     * @param args ignored
     */
    public static void main(String[] args) {
//        double[][] a = {
//            { 1, 2, 3, 1 },
//            { 3, 5, 2, 4 },
//            { 1, 5, 2, 2 },
//            { 0, 5, 2, 3 },
//            { 1, 2, 1, 1 },
//        };
//        double[][] a = {
//            { 1, 2, 3, 1, 1, 2 },
//            { 3, 5, 2, 4, 4, 2 },
//            { 5, 4, 6, 2, 2, 2 },
//        };
        double[][] a = {
            { 1, 2, 3, 1},
            { 3, 5, 2, 1},
            { 2, 4, 6, 2},
        };
        Matrix m = new RectangularMatrix(a);    
        BidiagonalDecomposition bd = new BidiagonalDecomposition(m);
        System.out.println(m);
        System.out.println(bd.getU());
        System.out.println(bd.getV());
        System.out.println(
            bd.getU().times(bd.getB()).times(bd.getV().transpose()));
        System.out.println(
            bd.getU().times(bd.getU().transpose()));
        System.out.println(
            bd.getV().times(bd.getV().transpose()));     
        System.out.println(bd.getB());
        System.out.println(
            bd.getU().transpose().times(m).times(bd.getV()));
    }
    

}
