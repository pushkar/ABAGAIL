package opt.example;

import opt.EvaluationFunction;

/**
 * An evaluation function for the traveling salesman problem
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class TravelingSalesmanEvaluationFunction implements EvaluationFunction {
    /**
     * The distance between city i and j
     */
    private double[][] distances;
    /**
     * Make a new traveling salesman  evaluation function
     * @param points the points at which the cities are located
     */
    public TravelingSalesmanEvaluationFunction(double[][] points) {
        distances = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            distances[i] = new double[i];
            for (int j = 0; j < i; j++) {
                double[] a = points[i];
                double[] b = points[j];
                distances[i][j] = Math.sqrt(Math.pow(a[0] - b[0], 2)
                    + Math.pow(a[1] - b[1], 2));
            }
        }
    }
    
    /**
     * Get the distance between two points
     * @param i the first point
     * @param j the second
     * @return the distance
     */
    public double getDistance(int i, int j) {
        if (i==j) {
            return 0;
        } else {
            int a = Math.max(i,j);
            int b = Math.min(i,j);
            return distances[a][b];
        }
    }
}
