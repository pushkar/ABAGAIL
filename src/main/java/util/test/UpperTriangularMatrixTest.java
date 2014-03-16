package util.test;

import util.linalg.RectangularMatrix;
import util.linalg.UpperTriangularMatrix;

/**
 * A test for lower triangular matrices
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class UpperTriangularMatrixTest {

    /**
     * Test main,
     * @param args ignored
     */
    public static void main(String[] args) {
      double[][] a = {
          { 1, 5, 3, 7 },
          { 0, 5, 1, 6 },
          { 0, 0, 6, 2 },
          { 0, 0, 0, 4 }
      };

      UpperTriangularMatrix um = new UpperTriangularMatrix(new RectangularMatrix(a));    
      System.out.println(um);
      System.out.println(um.inverse());
      System.out.println(um.inverse().times(um));
      System.out.println(um.times(um.inverse()));
      System.out.println(um.inverse().transpose().times(um.transpose()));
    }

}
