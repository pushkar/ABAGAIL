package util.test;

import util.linalg.RealSchurDecomposition;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;

/**
 * A test of the eigenvalue decomposition
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class EigenvalueDecompositionTest {
	/**
	 * The test main
	 * @param args ingored
	 */
	public static void main(String[] args) {
//		double[][] a = {
//			{ 1, 2, 3, 1 },
//			{ 3, 5, 2, 4 },
//			{ 1, 5, 2, 2 },
//			{ 0, 5, 2, 3 },
//		};
        double[][] a = {
            { 1, .79072 },
            { .79072, 1 }
        };
//		double[][] a = {
//			{ 1, 2, 3 },
//			{ 4, 5, 6 },
//			{ 7, 8, 0 }            
//		};
//		double[][] a = {
//			{ 4, 3, 2, 1},
//			{ 3, 4, 3, 2},
//			{ 2, 3, 4, 3},
//			{ 1, 2, 3, 4}
//		};
		Matrix m = new RectangularMatrix(a);    
		RealSchurDecomposition ed = new RealSchurDecomposition(m);
		System.out.println(m);
		System.out.println(ed.getU());
		System.out.println(ed.getT());
		System.out.println(ed.getU().transpose().times(ed.getU()));
		System.out.println(
			ed.getU().times(ed.getT()).times(ed.getU().transpose()));
	}
}
