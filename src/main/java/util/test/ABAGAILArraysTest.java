package util.test;

import util.ABAGAILArrays;

/**
 * A test main for the ABAGAIL utilities
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ABAGAILArraysTest {
    
    /**
     * Test main 
     * @param args ignored
     */
    public static void main(String[] args) {
        double[] numbers = new double[100];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }
        System.out.println(ABAGAILArrays.randomizedSelect(numbers, 11));
        System.out.println(ABAGAILArrays.search(numbers, 21));
        double[] test = new double[] {.1, 1};
        System.out.println(ABAGAILArrays.search(test, .2));
    }

}
