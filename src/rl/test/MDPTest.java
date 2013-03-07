package rl.test;

import rl.EpsilonGreedyStrategy;
import rl.Policy;
import rl.PolicyIteration;
import rl.QLambda;
import rl.SarsaLambda;
import rl.SimpleMarkovDecisionProcess;
import rl.ValueIteration;
import shared.FixedIterationTrainer;
import shared.ThresholdTrainer;

/**
 * A markov decision process test
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MDPTest {
    /**
     * The main method
     * @param args ignored
     */
    public static void main(String[] args) {
        // the andrew moore tutorial mdp
        SimpleMarkovDecisionProcess mdp = new SimpleMarkovDecisionProcess();
        mdp.setRewards(new double[] {0, 0, 10, 10});
        mdp.setTransitionMatrices(new double[][][] {
            {{ 1.0, 0, 0, 0}, {.5, .5, 0, 0 }},
            {{ .5,  0, 0, .5}, { 0,  1, 0, 0 }},
            {{ .5, 0, .5, 0}, {.5, .5, 0, 0 }},
            {{ 0, 0, .5, .5}, {0, 1, 0, 0 }}});
        mdp.setInitialState(0);
        // solve it
        ValueIteration vi = new ValueIteration(.9, mdp);
        ThresholdTrainer tt = new ThresholdTrainer(vi);
        long startTime = System.currentTimeMillis();
        tt.train();
        Policy p = vi.getPolicy();
        long finishTime = System.currentTimeMillis();
        System.out.println("Value iteration learned : " + p);
        System.out.println("in " + tt.getIterations() + " iterations");
        System.out.println("and " + (finishTime - startTime) + " ms");
        
        PolicyIteration pi = new PolicyIteration(.9, mdp);
        tt = new ThresholdTrainer(pi);
        startTime = System.currentTimeMillis();
        tt.train();
        p = pi.getPolicy();
        finishTime = System.currentTimeMillis();
        System.out.println("Policy iteration learned : " + p);
        System.out.println("in " + tt.getIterations() + " iterations");
        System.out.println("and " + (finishTime - startTime) + " ms");
        
        QLambda ql = new QLambda(.5, .9, .2, .995, new EpsilonGreedyStrategy(.3), mdp);
        FixedIterationTrainer fit = new FixedIterationTrainer(ql, 100);
        startTime = System.currentTimeMillis();
        fit.train();
        p = ql.getPolicy();
        finishTime = System.currentTimeMillis();
        System.out.println("Q lambda learned : " + p);
        System.out.println("in " + 100 + " iterations");
        System.out.println("and " + (finishTime - startTime) + " ms");
        System.out.println("Acquiring " + ql.getTotalReward() + " reward");
        
        SarsaLambda sl = new SarsaLambda(.5, .9, .2, .995, new EpsilonGreedyStrategy(.3), mdp);
        fit = new FixedIterationTrainer(sl, 100);
        startTime = System.currentTimeMillis();
        fit.train();
        p = sl.getPolicy();
        finishTime = System.currentTimeMillis();
        System.out.println("Sarsa lambda learned : " + p);
        System.out.println("in " + 100 + " iterations");
        System.out.println("and " + (finishTime - startTime) + " ms");
        System.out.println("Acquiring " + sl.getTotalReward() + " reward");
                
    }

}
