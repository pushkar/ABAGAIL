package rl.test;

import rl.EpsilonGreedyStrategy;
import rl.MazeMarkovDecisionProcessVisualization;
import rl.NonDeterministicMazeMDP;
import rl.Policy;
import rl.PolicyIteration;
import rl.QLambda;
import rl.SarsaLambda;
import rl.ValueIteration;
import shared.FixedIterationTrainer;
import shared.ThresholdTrainer;

/**
 * Tests out the non-deterministic maze markov decision process classes
 * @author Yiqi (Victor) Chen
 * @version 1.0
 */
public class NonDeterministicMazeMDPTest {
    /**
     * Tests out things
     * @param args ignored
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	NonDeterministicMazeMDP maze = NonDeterministicMazeMDP.load("testmaze.txt");
        System.out.println(maze);
        
        ValueIteration vi = new ValueIteration(.95, maze);
        ThresholdTrainer tt = new ThresholdTrainer(vi);
        long startTime = System.currentTimeMillis();
        tt.train();
        Policy p = vi.getPolicy();
        long finishTime = System.currentTimeMillis();
        System.out.println("Value iteration learned : " + p);
        System.out.println("in " + tt.getIterations() + " iterations");
        System.out.println("and " + (finishTime - startTime) + " ms");
        MazeMarkovDecisionProcessVisualization mazeVis =
            new MazeMarkovDecisionProcessVisualization(maze);
        System.out.println(mazeVis.toString(p));

        PolicyIteration pi = new PolicyIteration(.95, maze);
        tt = new ThresholdTrainer(pi);
        startTime = System.currentTimeMillis();
        tt.train();
        p = pi.getPolicy();
        finishTime = System.currentTimeMillis();
        System.out.println("Policy iteration learned : " + p);
        System.out.println("in " + tt.getIterations() + " iterations");
        System.out.println("and " + (finishTime - startTime) + " ms");
        System.out.println(mazeVis.toString(p));
        
        int iterations = 50000;
        QLambda ql = new QLambda(.5, .95, .2, 1, new EpsilonGreedyStrategy(.3), maze);
        FixedIterationTrainer fit = new FixedIterationTrainer(ql, iterations);
        startTime = System.currentTimeMillis();
        fit.train();
        p = ql.getPolicy();
        finishTime = System.currentTimeMillis();
        System.out.println("Q lambda learned : " + p);
        System.out.println("in " + iterations + " iterations");
        System.out.println("and " + (finishTime - startTime) + " ms");
        System.out.println("Acquiring " + ql.getTotalReward() + " reward");
        System.out.println(mazeVis.toString(p));
        
        SarsaLambda sl = new SarsaLambda(.5, .95, .2, 1, new EpsilonGreedyStrategy(.3), maze);
        fit = new FixedIterationTrainer(sl, iterations);
        startTime = System.currentTimeMillis();
        fit.train();
        p = sl.getPolicy();
        finishTime = System.currentTimeMillis();
        System.out.println("Sarsa lambda learned : " + p);
        System.out.println("in " + iterations + " iterations");
        System.out.println("and " + (finishTime - startTime) + " ms");
        System.out.println("Acquiring " + sl.getTotalReward() + " reward");
        System.out.println(mazeVis.toString(p));

    }

}
