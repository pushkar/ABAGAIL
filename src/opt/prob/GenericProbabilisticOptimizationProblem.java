package opt.prob;

import dist.Distribution;
import opt.EvaluationFunction;
import opt.GenericOptimizationProblem;

/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class GenericProbabilisticOptimizationProblem extends GenericOptimizationProblem
            implements ProbabilisticOptimizationProblem {
    /**
     * The distribution
     */
    private Distribution dist;

    /**
     * Make a new generic probabilisitic optimiziation problem
     * @param eval the evaluation function
     * @param dist the initial parameter distribution
     * @param fact the distribution factory
     */
    public GenericProbabilisticOptimizationProblem(EvaluationFunction eval, Distribution dist,
             Distribution d) {
        super(eval, dist);
        this.dist = d;
    }

    /**
     * @see opt.prob.ProbabilisticOptimizationProblem#getDistribution()
     */
    public Distribution getDistribution() {
        return dist;
    }



}
