package opt.example;

import shared.Instance;
import util.ABAGAILArrays;

/**
 * A traveling salesman evaluation function that works with
 * routes that are encoded as sorts.  That is the route
 * is the permutaiton of indices found by sorting the data.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class TravelingSalesmanSortEvaluationFunction extends TravelingSalesmanEvaluationFunction {

    /**
     * Make a new traveling salesman evaluation function
     * @param points the points at which the cities are located
     */
    public TravelingSalesmanSortEvaluationFunction(double[][] points) {
        super(points);
    }

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        double[] ddata = new double[d.size()];
        for (int i = 0; i < ddata.length; i++) {
            ddata[i] = d.getContinuous(i);
        }
        int[] order = ABAGAILArrays.indices(d.size());
        ABAGAILArrays.quicksort(ddata, order);
        double distance = 0;
        for (int i = 0; i < order.length - 1; i++) {
            distance += getDistance(order[i], order[i+1]);
        }
        distance += getDistance(order[order.length - 1], order[0]);
        return 1/distance;
    }

}
