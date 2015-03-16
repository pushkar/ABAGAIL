package opt.test;

import java.util.Arrays;
import java.util.Random;
import opt.ga.NQueensFitnessFunction;
import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.SwapNeighbor;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.SwapMutation;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * @author kmanda1
 * @version 1.0
 */
public class NQueensTest {
    /** The n value */
    private static final int N = 10;
    /** The t value */
    
    public static void main(String[] args) {
        int[] ranges = new int[N];
        Random random = new Random(N);
        for (int i = 0; i < N; i++) {
        	ranges[i] = random.nextInt();
        }
        NQueensFitnessFunction ef = new NQueensFitnessFunction();
        Distribution odd = new DiscretePermutationDistribution(N);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new SingleCrossOver();
        Distribution df = new DiscreteDependencyTree(.1); 
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 100);
        fit.train();
        long starttime = System.currentTimeMillis();
        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
        System.out.println("RHC: Board Position: ");
       // System.out.println(ef.boardPositions());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        SimulatedAnnealing sa = new SimulatedAnnealing(1E1, .1, hcp);
        fit = new FixedIterationTrainer(sa, 100);
        fit.train();
        
        starttime = System.currentTimeMillis();
        System.out.println("SA: " + ef.value(sa.getOptimal()));
        System.out.println("SA: Board Position: ");
       // System.out.println(ef.boardPositions());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        starttime = System.currentTimeMillis();
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 0, 10, gap);
        fit = new FixedIterationTrainer(ga, 100);
        fit.train();
        System.out.println("GA: " + ef.value(ga.getOptimal()));
        System.out.println("GA: Board Position: ");
        //System.out.println(ef.boardPositions());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        starttime = System.currentTimeMillis();
        MIMIC mimic = new MIMIC(200, 10, pop);
        fit = new FixedIterationTrainer(mimic, 5);
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
        System.out.println("MIMIC: Board Position: ");
        //System.out.println(ef.boardPositions());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
    }
}
