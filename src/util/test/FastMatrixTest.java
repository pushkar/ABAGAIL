package util.test;

import util.linalg.DenseVector;
import util.linalg.FastMatrix;
import util.linalg.Matrix;
import util.linalg.Vector;

/**
 * Created by Josh on 2/21/2015.
 */
public class FastMatrixTest {
    static int counter = 0;

    public static void main(String[] args) {
        int passed = 0;
        System.out.println("Square Matrix Transpose test:");
        if(squareTransposeTest()) {
            passed++;
            System.out.println("\tPassed!");
        } else {
            System.out.println("\tFailed");
        }

        System.out.println("Get Row Test:");
        if(getRowTest()) {
            passed++;
            System.out.println("\tPassed!");
        } else {
            System.out.println("\tFailed");
        }

        if (counter == passed){
            System.out.println("All tests passed!");
        }
    }

    private static boolean squareTransposeTest() {
        counter++;
        double[] arr = {1,2,3,4,5,6,7,8,9};
        FastMatrix myMat = new FastMatrix(3,3, arr);
        Matrix tp = myMat.transpose();

        double[] result = {1,4,7,2,5,8,3,6,9};
        for (int i = 0; i < tp.m(); i++) {
            for (int j = 0; j < tp.n(); j++) {
                if (tp.get(i, j) != result[i*tp.n()+j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean getRowTest() {
        counter++;
        double[] arr = {1,2,3,4,5,6,7,8,9};
        FastMatrix myMat = new FastMatrix(3,3, arr);
        Vector center = myMat.getRow(1);
        double[] test = {4,5,6};
        DenseVector comp = new DenseVector(test);
        return center.equals(comp);
    }
}
