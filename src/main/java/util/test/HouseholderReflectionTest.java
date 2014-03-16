package util.test;

import util.linalg.HouseholderReflection;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.Vector;

/**
 * A test of householder reflections
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class HouseholderReflectionTest {
    
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        double[][] a = {
            { 1, 2, 3 },
            { 3, 5, 2 },
            { 1, 5, 2 },
            { 1, 2, 1 }
        };
        Matrix m = new RectangularMatrix(a); 
        Vector x = m.getRow(0);
        System.out.println(x);
        HouseholderReflection hr1 = new HouseholderReflection(x);
        hr1.applyRight(m, 0, m.m(), 0, m.n());
        System.out.println(m);
        x = m.getRow(1);
        x = x.get(1, x.size());
        System.out.println(x);
        HouseholderReflection hr2 = new HouseholderReflection(x);
        hr2.applyRight(m, 1, m.m(), 1, m.n());
        System.out.println(m);
        Matrix q = RectangularMatrix.eye(3);
        hr1.applyLeft(q, 0, q.m(), 0, q.n());
        hr2.applyLeft(q, 1, q.m(), 0, q.n());
        System.out.println(m.times(q));
    }

}
