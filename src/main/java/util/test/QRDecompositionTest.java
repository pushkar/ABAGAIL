package util.test;

import util.linalg.Matrix;
import util.linalg.QRDecomposition;
import util.linalg.RectangularMatrix;

/**
 * A test of the QR Decomposition
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class QRDecompositionTest {
    
    /**
     * Test main, creates a matrix and decomposes and reconstructs
     * @param args ignored
     */
    public static void main(String[] args) {
//        double[][] a = {
//            { 1, 2, 3, 4 },
//            { 3, 5, 2, 5 },
//            { 1, 5, 2, 6 },
//        };
//        double[][] a = {
//            { 1, 2, 3},
//            { 3, 5, 2},
//            { 1, 5, 2},
//            { 6, 3, 2}
//        };
        double[][] a = {
            { 1, 2, 3 },
            { 3, 5, 2 },
            { 1, 5, 2 },
        };
        Matrix m = new RectangularMatrix(a);    
        QRDecomposition qrd = new QRDecomposition(m);
        System.out.println(m);
        System.out.println(qrd.getQ());
        System.out.println(qrd.getR());
        System.out.println(qrd.getQ().times(qrd.getR()));
        System.out.println(qrd.getQ().times(qrd.getQ().transpose()));
    }

}
