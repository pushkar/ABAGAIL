package util.test;

import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.SymmetricEigenvalueDecomposition;

/**
 * Test of the symmetric eigenvalue decomposition
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SymmetricEigenvalueDecompositionTest {

	/**
	 * The test main
	 * @param args ingored
	 */
	public static void main(String[] args) {
//		double[][] a = {
//			{ 1, .79072 },
//			{ .79072, 1 }
//        };
		double[][] a = {
			{ 4, 3, 2, 1},
			{ 3, 4, 3, 2},
			{ 2, 3, 4, 3},
			{ 1, 2, 3, 4}
		};
		Matrix m = new RectangularMatrix(a);    
		SymmetricEigenvalueDecomposition ed = 
		    new SymmetricEigenvalueDecomposition(m);
		System.out.println(m);
		System.out.println(ed.getU());
		System.out.println(ed.getD());
		System.out.println(ed.getU().transpose().times(ed.getU()));
		System.out.println(
			ed.getU().times(ed.getD()).times(ed.getU().transpose()));
	}

}
