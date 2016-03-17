package opt;

import dist.Distribution;

import shared.Instance;

/**
 * A simulated annealing implementation, maximizing a 
 * {@link opt.HillClimbingProblem}.  This uses an exponential cooling scheme,
 * and so is parametrized over starting temperature and cooling constant.
 * Temperature is multiplied by that cooling constant at every iteration.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SimulatedAnnealing extends OptimizationAlgorithm {
    
    /**
     * The current optimization data
     */
    private Instance cur;
    
    /**
     * The current optimization value
     */
    private double curVal;
    
    /**
     * The current temperature
     */
    private double t;
    
    /**
     * The cooling parameter
     */
    private double cooling;
    
    /**
     * Make a new simulated annealing instance. Specify starting temperature,
     * cooling ratio, and a {@link opt.HillClimbingProblem}.
     * 
     * @param t The starting temperature (greater than 0)
     * @param cooling The cooling constant (between 0 and 1, exclusive)
     * @param hcp The optimization problem to maximize
     */
    public SimulatedAnnealing(double t, double cooling, HillClimbingProblem hcp) {
        super(hcp);
        this.t = t;
        this.cooling = cooling;
        this.cur = hcp.random();
        this.curVal = hcp.value(cur);
    }

    /**
     * Run simulated annealing for another iteration.  Temperature will be
     * multiplied by the cooling constant, and the optimal value may move to
     * a neighbor (depending on temperature and value).
     * @see shared.Trainer#train() 
     */
    public double train() {
        HillClimbingProblem p = (HillClimbingProblem) getOptimizationProblem();
        Instance neigh = p.neighbor(cur);
        double neighVal = p.value(neigh);
        if (neighVal > curVal || Distribution.random.nextDouble() < 
                Math.exp((neighVal - curVal) / t)) {
            curVal = neighVal;
            cur = neigh;
        }
        t *= cooling;
        return curVal;
    }

    /**
     * @return Current value of the given {@link opt.HillClimbingProblem}
     * @see opt.OptimizationAlgorithm#getOptimal() 
     */
    public Instance getOptimal() {
        return cur;
    }

}