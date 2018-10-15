package opt.ga;

import dist.Distribution;
import opt.EvaluationFunction;
import opt.GenericOptimizationProblem;
import shared.Instance;

/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class GenericGeneticAlgorithmProblem extends GenericOptimizationProblem implements
        GeneticAlgorithmProblem {

    /**
     * The cross over function
     */
    private CrossoverFunction crossover;
    /**
     * The mutation function
     */
    private MutationFunction mutation;

    /**
     * Make a new generic genetic algorithm problem
     * @param crossover the cross over operator
     * @param muation the mutation operator
     * @param eval the evaluation function
     * @param dist the initial distribution
     */
    public GenericGeneticAlgorithmProblem(EvaluationFunction eval, Distribution dist,
              MutationFunction mutation, CrossoverFunction crossover) {
        super(eval, dist);
        this.mutation = mutation;
        this.crossover = crossover;
    }
    /**
     * @see opt.ga.GeneticAlgorithmProblem#mate(opt.Instance, opt.Instance)
     */
    public Instance mate(Instance a, Instance b) {
        return crossover.mate(a, b);
    }
    /**
     * @see opt.ga.GeneticAlgorithmProblem#mutate(opt.Instance)
     */
    public void mutate(Instance d) {
        mutation.mutate(d);
    }

}
