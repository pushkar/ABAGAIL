package util.test;

import util.linalg.CholeskyFactorization;
import util.linalg.DenseVector;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.Vector;

/**
 * A test of the cholesky factorization
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class CholeskyFactorizationTest {

	
	/**
	 * The test main
	 * @param args ingored
	 */
	public static void main(String[] args) {
		double[][] a = {
			{ 4, 3, 2, 1},
			{ 3, 4, 3, 2},
			{ 2, 3, 4, 3},
			{ 1, 2, 3, 4}
		};
		Matrix m = new RectangularMatrix(a);    
		CholeskyFactorization cf = new CholeskyFactorization(m);
		System.out.println(m);
		System.out.println(cf.getL());
		System.out.println(cf.getL().times(cf.getL().transpose()));
		System.out.println(cf.determinant());
		double[] b = {1, 0, 0, 0};
		Vector v = new DenseVector(b);
		Vector x = cf.solve(v);
		System.out.println(x);
		System.out.println(m.times(x));
	}
}
