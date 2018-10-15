package opt.test;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.*;
import opt.example.TwoColorsEvaluationFunction;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

/**
 * @author Daniel Cohen dcohen@gatech.edu
 * @version 1.0
 */
public class TwoColorsTest {
    /**
     * The number of colors
     */
    private static final int k = 2;
    /**
     * The N value
     */
    private static final int N = 100 * k;

    public static String run(int iterations) {
        int[] ranges = new int[N];
        Arrays.fill(ranges, k + 1);
        EvaluationFunction ef = new TwoColorsEvaluationFunction();
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new UniformCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        String rhcResult, saResult, gaResult, mimicResult;
        Instant end, start;
        start = Instant.now();
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, iterations);
        fit.train();
        end = Instant.now();
        rhcResult = "RHC," + Duration.between(start, end).toMillis() + "," + ef.value(rhc.getOptimal());
//        System.out.println(rhcResult);

        start = Instant.now();
        SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
        fit = new FixedIterationTrainer(sa, iterations);
        fit.train();
        end = Instant.now();
        saResult = "SA," + Duration.between(start, end).toMillis() + "," + ef.value(sa.getOptimal());
//        System.out.println(saResult);

        start = Instant.now();
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(20, 20, 0, gap);
        fit = new FixedIterationTrainer(ga, iterations);
        fit.train();
        end = Instant.now();
        gaResult = "GA," + Duration.between(start, end).toMillis() + "," + ef.value(ga.getOptimal());
//        System.out.println(gaResult);

        start = Instant.now();
        MIMIC mimic = new MIMIC(50, 10, pop);
        fit = new FixedIterationTrainer(mimic, iterations);
        fit.train();
        end = Instant.now();
        mimicResult = "MIMIC," + Duration.between(start, end).toMillis() + "," + ef.value(mimic.getOptimal());
//        System.out.println(mimicResult);
        return rhcResult + "\n" + saResult + "\n" + gaResult + "\n" + mimicResult + "\n";
    }
}
