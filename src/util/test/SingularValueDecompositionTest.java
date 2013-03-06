package util.test;

import util.linalg.DenseVector;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.SingularValueDecomposition;
import util.linalg.Vector;

/**
 * A test of the singular value decomposition
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SingularValueDecompositionTest {
    
    /**
     * Test main, creates a matrix and decomposes and reconstructs
     * @param args ignored
     */
    public static void main(String[] args) {
        double[][] a = {
            { 1, .79072 },
            { .79072, 1 }
        };
//      double[][] a = {
//          { 1, 2, 3, 1, 1, 2 },
//          { 3, 5, 2, 4, 4, 2 },
//          { 5, 4, 6, 2, 2, 2 },
//      };
//        double[][] a = {
//            { 1, 2, 3, 1 },
//            { 3, 5, 2, 4 },
//            { 1, 5, 2, 2 },
//            { 0, 5, 2, 3 },
//            { 1, 2, 1, 1 },
//            { 1, 2, 1, 1 },
//        };
//		double[][] a = {
//			{ 11, -13, 2 },
//			{ 12, -11, 3 },
//		};
        Matrix m = new RectangularMatrix(a);
        SingularValueDecomposition svd = new SingularValueDecomposition(m);
        System.out.println(m);
        System.out.println(svd.getD());
        System.out.println(svd.getU());
        System.out.println(svd.getV());
        System.out.println(
            svd.getU().times(svd.getD()).times(svd.getV().transpose()));
        System.out.println(
            svd.getU().times(svd.getU().transpose()));
        System.out.println(
            svd.getV().times(svd.getV().transpose()));
            
//		double[] b = {1, 1, 1};
//		double[] b = {1, 1, 1, 1, 1, 1};
        double[] b = {1, 1};
		Vector v = new DenseVector(b);
		Vector x = svd.solve(v);
		System.out.println(x);
		System.out.println(m.times(x));

    }
}
