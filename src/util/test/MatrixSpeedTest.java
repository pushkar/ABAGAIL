package util.test;

import util.linalg.*;

/**
 * test to compare matrix operation speeds between implementations
 * @author Joshua Morton joshua.morton@gatech.edu
 * @version 1.0
 */
public class MatrixSpeedTest {

    public static void main(String[] args) {
        FastMatrix f = new FastMatrix(500,500);
        Matrix r = new RectangularMatrix(500,500);
        for (int i = 0; i < 500; i++) {
            for (int j = 0; j < 500; j++) {
                f.set(i, j, i+j+1);
                r.set(i, j, i+j+1);
            }
        }
        //Matrix r = new RectangularMatrix(100,100)
        long l = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            Matrix m = f.times(f);
        }
        System.out.println("fast: " + (System.currentTimeMillis() - l));

        l = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            Matrix m = r.times(r);
        }
        System.out.println("rect: " + (System.currentTimeMillis() - l));
    }
}
