package util.test;

import util.linalg.HessenbergDecomposition;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;

/**
 * A test for the hessenberg decomposition class
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class HessenbergDecompositionTest {

	/**
	 * Test main, creates a matrix and decomposes and reconstructs
	 * @param args ignored
	 */
	public static void main(String[] args) {
//		double[][] a = {
//			{ 1, 5, 7, 8 },
//			{ 3, 0, 6, 8 },
//			{ 4, 3, 1, 8 },
//			{ 1, 2, 3, 4 }
//		};
		double[][] a = {
			{ 4, 3, 2, 1},
			{ 3, 4, 3, 2},
			{ 2, 3, 4, 3},
			{ 1, 2, 3, 4}
		};

		Matrix m = new RectangularMatrix(a);    
		HessenbergDecomposition hd = new HessenbergDecomposition(m);
		System.out.println(m);
		System.out.println(hd.getU());
		System.out.println(hd.getH());
		System.out.println(
			hd.getU().times(hd.getH()).times(hd.getU().transpose()));
		System.out.println(
			hd.getU().times(hd.getU().transpose()));
	}

}
