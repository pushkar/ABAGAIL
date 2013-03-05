package util.test;

import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.TridiagonalDecomposition;

/**
 * A test of the tridiagonal decomposition
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class TridiagonalDecompositionTest {


	/**
	 * Test main, creates a matrix and decomposes and reconstructs
	 * @param args ignored
	 */
	public static void main(String[] args) {
		double[][] a = {
			{ 4, 3, 2, 1},
			{ 3, 4, 3, 2},
			{ 2, 3, 4, 3},
			{ 1, 2, 3, 4}
		};
//		double[][] a = {
//			{ 1, 3, 4 },
//		 	{ 3, 2, 8 },
//		 	{ 4, 8, 3 }
//		};
		Matrix m = new RectangularMatrix(a);    
		TridiagonalDecomposition td = new TridiagonalDecomposition(m);
		System.out.println(m);
		System.out.println(td.getU());
		System.out.println(td.getT());
		System.out.println(
			td.getU().times(td.getT()).times(td.getU().transpose()));
		System.out.println(
			td.getU().times(td.getU().transpose()));
	}

}
