package opt.test;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.*;
import opt.example.TravelingSalesmanCrossOver;
import opt.example.TravelingSalesmanEvaluationFunction;
import opt.example.TravelingSalesmanRouteEvaluationFunction;
import opt.example.TravelingSalesmanSortEvaluationFunction;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class TravelingSalesmanTest {
    /**
     * The n value
     */
    private static final int N = 50;

    /**
     * The test main
     *
     * @param iterations an int[] with 4 elements - RHC, SA, GA, Mimic
     */
    public static String run(int[] iterations, int runNum) {
        int rhcIterations = iterations[0];
        int saIterations = iterations[1];
        int gaIterations = iterations[2];
        int mimicIterations = iterations[3];

        Random random = new Random();
        // create the random points
        double[][] points = new double[N][2];
        for (int i = 0; i < points.length; i++) {
            points[i][0] = random.nextDouble();
            points[i][1] = random.nextDouble();
        }
        // for rhc, sa, and ga we use a permutation based encoding
        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
        Distribution odd = new DiscretePermutationDistribution(N);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new TravelingSalesmanCrossOver(ef);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);

        String rhcResult, saResult, gaResult, mimicResult;
        Instant end, start;

        System.out.println("Run:    "+runNum+"\n-------------------------------------------------------");
        start = Instant.now();
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, rhcIterations);
        fit.train();
        end = Instant.now();
        double rhcVal = ef.value(rhc.getOptimal());
        rhcResult = "RHC," + Duration.between(start, end).toMillis() + "," + rhcVal + "," +
                Math.ceil(1 / rhcVal);
        System.out.println(rhcResult);

        start = Instant.now();
        SimulatedAnnealing sa = new SimulatedAnnealing(1E12, .95, hcp);
        fit = new FixedIterationTrainer(sa, saIterations);
        fit.train();
        end = Instant.now();
        double saVal = ef.value(sa.getOptimal());
        saResult = "SA," + Duration.between(start, end).toMillis() + "," + saVal + "," +
                Math.ceil(1 / saVal);
        System.out.println(saResult);

        start = Instant.now();
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 20, gap);
        fit = new FixedIterationTrainer(ga, gaIterations);
        fit.train();
        end = Instant.now();
        double gaVal = ef.value(ga.getOptimal());
        gaResult = "GA," + Duration.between(start, end).toMillis() + "," + gaVal + "," +
                Math.ceil(1 / gaVal);
        System.out.println(gaResult);


        // for mimic we use a sort encoding
        ef = new TravelingSalesmanSortEvaluationFunction(points);
        int[] ranges = new int[N];
        Arrays.fill(ranges, N);
        odd = new DiscreteUniformDistribution(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        MIMIC mimic = new MIMIC(200, 100, pop);
        fit = new FixedIterationTrainer(mimic, mimicIterations);
        fit.train();
        end = Instant.now();
        double mimicVal = ef.value(mimic.getOptimal());
        mimicResult = "MIMIC," + Duration.between(start, end).toMillis() + "," + mimicVal + "," + Math.ceil(1 /
                mimicVal);
        System.out.println(mimicResult);
        System.out.println("-------------------------\n");
        return rhcResult + "\n" + saResult + "\n" + gaResult + "\n" + mimicResult + "\n";
    }
}
