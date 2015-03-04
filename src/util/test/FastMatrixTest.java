package util.test;

import util.linalg.*;

/**
 * Created by Josh Morton on 2/21/2015.
 *
 * why we aren't using a testing package I don't know, but whatever
 */
public class FastMatrixTest {
    static int counter = 0;

    public static void main(String[] args) {
        int passed = 0;
        System.out.println("Square Matrix Transpose :");
        if(squareTransposeTest()) {
            passed++;
            System.out.println("\tPassed!");
        } else {
            System.out.println("\tFailed");
        }

        System.out.println("Get Row:");
        if(getRowTest()) {
            passed++;
            System.out.println("\tPassed!");
        } else {
            System.out.println("\tFailed");
        }

        System.out.println("Multiplication by Rectangular Matrix:");
        if(fastRectMultiply()) {
            passed++;
            System.out.println("\tPassed!");
        } else {
            System.out.println("\tFailed");
        }

        System.out.println("Multiplication by Fast Matrix:");
        if(fastFastMultiply()) {
            passed++;
            System.out.println("\tPassed!");
        } else {
            System.out.println("\tFailed");
        }

        System.out.println("Fast Matrix Multiplies into odd shapes:");
        if(fastFastUnusualMultiply()) {
            passed++;
            System.out.println("\tPassed!");
        } else {
            System.out.println("\tFailed");
        }

        if (counter == passed){
            System.out.println("All tests passed!");
        }
    }

    /**
     * checks that a m.transpose() correctly returns a transposed matrix
     * @return whether or not the test passes
     */
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

    /**
     * checks m.getRow()
     * @return
     */
    private static boolean getRowTest() {
        counter++;
        double[] arr = {1,2,3,4,5,6,7,8,9};
        FastMatrix myMat = new FastMatrix(3,3, arr);
        Vector center = myMat.getRow(1);
        double[] test = {4,5,6};
        DenseVector comp = new DenseVector(test);
        return center.equals(comp);
    }

    /**
     * makes sure that a matrix matrix multiply works correctly on a fastMatrix by a rectangular Matrix
     * @return
     */
    private static boolean fastRectMultiply() {
        counter++;
        double[] arr = {1,2,3,4,5,6,7,8};
        double[][] arr2 = {{1,2},{3,4},{5,6},{7,8}};
        Matrix first = new FastMatrix(2, 4, arr);
        Matrix second = new RectangularMatrix(arr2);
        Matrix t = first.times(second);
        if (t.get(0,0) == 50 && t.get(0,1) == 60 && t.get(1,0) == 114 && t.get(1,1) == 140) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * checks that the override fastMat by fastMat multiply also works
     * @return
     */
    private static boolean fastFastMultiply() {
        counter++;
        double[] arr = {1,2,3,4,5,6,7,8};
        Matrix first = new FastMatrix(2, 4, arr);
        Matrix second = new FastMatrix(4, 2, arr);
        Matrix t = first.times(second);
        if (t.get(0,0) == 50 && t.get(0,1) == 60 && t.get(1,0) == 114 && t.get(1,1) == 140) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * multiplies two matrices that result in a different shape, to make sure that all sizing works
     * @return
     */
    private static boolean fastFastUnusualMultiply() {
        counter++;
        double[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        double[] arr2 = {1, 2, 3, 4, 5, 6};
        Matrix first = new FastMatrix(3, 3, arr);
        Matrix second = new FastMatrix(3, 2, arr2);
        Matrix t = first.times(second);
        if (t.get(0, 0) == 22 && t.get(0, 1) == 28 && t.get(1, 0) == 49 && t.get(1, 1) == 64 && t.get(2, 0) == 76 &&
                t.get(2, 1) == 100) {
            return true;
        } else {
            return false;
        }
    }
}
