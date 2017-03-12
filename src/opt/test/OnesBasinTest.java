package opt.test;

import java.util.Arrays;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.*;
import opt.example.*;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * An optimization problem of finding a bit string with all ones. If the first T bits are not one, the secondary optimum
 * is returned. Otherwise, the second optimum + the largest run of ones is returned.
 * @author Joshua Wang joshuawang@gatech.edu
 * @version 1.0
 */
public class OnesBasinTest {

    /** The length of the vectors **/
    private static final int N = 50;

    /** The max run length **/
    private static final int T = 5;

    private static final int RUNS = 10;

    public static void main(String[] args) {

        double[] avgRHC = new double[2];
        double[] avgSA = new double[2];
        double[] avgGA = new double[2];;
        double[] avgMIMIC = new double[2];

        // FixedIterationTrainer.printDebug = true;

        for (int t = 0; t < RUNS; t ++) {

            int[] ranges = new int[N];
            Arrays.fill(ranges, 2);
            EvaluationFunction ef = new OnesBasinEvaluationFunction(T);
            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new SingleCrossOver();
            Distribution df = new DiscreteDependencyTree(.1, ranges);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

            double toSeconds = 1E9;
            double opt = 0.0;
            double timeSeconds = 0.0;

            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
            FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 500000);
            fit.train();

            timeSeconds = fit.getTimeToTrain() / toSeconds;
            opt = ef.value(rhc.getOptimal());
            avgRHC[0] += opt;
            avgRHC[1] += timeSeconds;

            System.out.printf("Finished in %fs\n", timeSeconds);
            System.out.printf("RHC: %f\n\n", opt);


            SimulatedAnnealing sa = new SimulatedAnnealing(1E10, .99, hcp);
            fit = new FixedIterationTrainer(sa, 10000);
            fit.train();

            timeSeconds = fit.getTimeToTrain() / toSeconds;
            opt = ef.value(sa.getOptimal());
            avgSA[0] += opt;
            avgSA[1] += timeSeconds;

            System.out.printf("Finished in %fs\n", timeSeconds);
            System.out.printf("SA: %f\n\n", opt);

            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
            fit = new FixedIterationTrainer(ga, 2000);
            fit.train();

            timeSeconds = fit.getTimeToTrain() / toSeconds;
            opt = ef.value(ga.getOptimal());
            avgGA[0] += opt;
            avgGA[1] += timeSeconds;

            System.out.printf("Finished in %fs\n", timeSeconds);
            System.out.printf("GA: %f\n\n", opt);

            MIMIC mimic = new MIMIC(2000, 100, pop);
            fit = new FixedIterationTrainer(mimic, 1000);
            fit.train();

            timeSeconds = fit.getTimeToTrain() / toSeconds;
            opt = ef.value(mimic.getOptimal());
            avgMIMIC[0] += opt;
            avgMIMIC[1] += timeSeconds;

            System.out.printf("Finished in %fs\n", timeSeconds);
            System.out.printf("MIMIC: %f\n\n", opt);
        }

        for (int i = 0; i < 2; i++) {
            avgRHC[i] /= RUNS;
            avgSA[i] /= RUNS;
            avgGA[i] /= RUNS;
            avgMIMIC[i] /= RUNS;
        }

        System.out.printf("%-10s%10s%10s\n", "ALGORITHM", "AVG OPT", "AVG TIME");
        System.out.printf("%-10s%10.4f%10.6fs\n", "RHC", avgRHC[0], avgRHC[1]);
        System.out.printf("%-10s%10.4f%10.6fs\n", "SA", avgSA[0], avgSA[1]);
        System.out.printf("%-10s%10.4f%10.6fs\n", "GA", avgGA[0], avgGA[1]);
        System.out.printf("%-10s%10.4f%10.6fs\n", "MIMIC", avgMIMIC[0], avgMIMIC[1]);
    }
}
