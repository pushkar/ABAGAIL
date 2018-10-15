package opt.ga;

import opt.OptimizationProblem;
import shared.Instance;

/**
 * A genetic algorithm problem
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface GeneticAlgorithmProblem extends OptimizationProblem {
    
    /**
     * Mate two optimization datas
     * @param a the first one to mate
     * @param b the second one to mate
     * @return the result of mating them
     */
    public abstract Instance mate(Instance a, Instance b);
    
    /**
     * Mutate a observation data
     * @param d the data to mutate
     */
    public abstract void mutate(Instance d);
}
