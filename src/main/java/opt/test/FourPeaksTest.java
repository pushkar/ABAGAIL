package opt.test;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.*;
import opt.example.FourPeaksEvaluationFunction;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

/**
 * Copied from ContinuousPeaksTest
 *
 * @version 1.0
 */
public class FourPeaksTest {
    /**
     * The n value
     */
    private static final int N = 200;
    /**
     * The t value
     */
    private static final int T = N / 5;

    public static String run(int[] iterations) {
        int rhcIterations = iterations[0];
        int saIterations = iterations[1];
        int gaIterations = iterations[2];
        int mimicIterations = iterations[3];

        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        EvaluationFunction ef = new FourPeaksEvaluationFunction(T);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new SingleCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        String rhcResult, saResult, gaResult, mimicResult;
        Instant end, start;
        start = Instant.now();
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, rhcIterations);
        fit.train();
        end = Instant.now();
        double rhcVal = ef.value(rhc.getOptimal());
        rhcResult = "RHC," + rhcIterations + "," + Duration.between(start, end).toMillis() + "," + rhcVal;
        System.out.println(rhcResult);

        start = Instant.now();
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
        fit = new FixedIterationTrainer(sa, saIterations);
        fit.train();
        end = Instant.now();
        double saVal = ef.value(sa.getOptimal());
        saResult = "SA," + saIterations + "," + Duration.between(start, end).toMillis() + "," + saVal;
        System.out.println(saResult);

        start = Instant.now();
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
        fit = new FixedIterationTrainer(ga, gaIterations);
        fit.train();
        end = Instant.now();
        double gaVal = ef.value(ga.getOptimal());
        gaResult = "GA," + gaIterations + "," + Duration.between(start, end).toMillis() + "," + gaVal;
        System.out.println(gaResult);

        start = Instant.now();
        MIMIC mimic = new MIMIC(200, 20, pop);
        // 1000
        fit = new FixedIterationTrainer(mimic, mimicIterations);
        fit.train();
        end = Instant.now();
        double mimicVal = ef.value(mimic.getOptimal());
        mimicResult = "MIMIC," + mimicIterations + "," + Duration.between(start, end).toMillis() + "," + mimicVal;
        System.out.println(mimicResult);
        return rhcResult + "\n" + saResult + "\n" + gaResult + "\n" + mimicResult + "\n";
    }
}
