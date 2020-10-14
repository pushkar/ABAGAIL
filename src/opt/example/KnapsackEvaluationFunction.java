package opt.example;

import util.linalg.Vector;
import opt.EvaluationFunction;
import shared.Instance;

/**
 * A checker board evaluation function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class KnapsackEvaluationFunction implements EvaluationFunction {

    /**
     * The values for the things that can be put in the knapsack
     */
    private double[] values;

    /**
     * The weights for the things that can be put in the knapsack
     */
    private double[] weights;

    /**
     * The maximum weight that the knapsack can take
     */
    private double maxWeight;

    /**
     * The weight of all the items
     */
    private double allItemsWeight;

    /**
     * Function Evaluation Count
     */
    private int fEvals;

    /**
     * Make a new knapsack evaluation function
     * @param values the set of values
     * @param weights the set of weights
     * @param maximumValue the maximum value
     * @param copiesPerElement the number of copies per element
     */
    public KnapsackEvaluationFunction(double[] values,
            double[] weights,
            double maximumValue,
            int[] copiesPerElement) {
        this.fEvals=0;
        this.values = values;
        this.weights = weights;
        maxWeight = maximumValue;
        for (int i = 0; i < weights.length; i++) {
            allItemsWeight += copiesPerElement[i] * weights[i];
        }
    }

    /**
     * Find the value of the knapsack with the given items.
     */
    public double value(Instance d) {
        this.fEvals++;
        Vector entriesInKnapsack = d.getData();
        double weight = 0;
        double value = 0;
        for (int i = 0; i < entriesInKnapsack.size(); i++) {
            weight += weights[i] * entriesInKnapsack.get(i);
            value += values[i] * entriesInKnapsack.get(i);
        }
        if (weight < maxWeight) {
            return value;
        } else {
            double smallNumber = 1E-10;
            return smallNumber*(allItemsWeight - weight);
        }
    }

    /**
     * Return function evaluation count
     * @return int fEvals
     */
    public int getFunctionEvaluations(){
      return this.fEvals;
    }

    /**
     * Reset function evaluation count
     */
    public void resetFunctionEvaluationCount(){
      this.fEvals=0;
    }
}
