package opt.test;

import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.StandardGeneticAlgorithm;
import shared.*;
import shared.filt.kFoldSplitFilter;
import shared.filt.TestTrainSplitFilter;
import shared.filt.LabelSelectFilter;
import shared.filt.NormalizedFilter;
import shared.filt.StandardizedFilter;
import shared.reader.ArffDataSetReader;
import shared.tester.AccuracyTestMetric;
import shared.tester.ConfusionMatrixTestMetric;
import shared.tester.NeuralNetworkTester;
import shared.tester.TestMetric;
import shared.tester.Tester;
import func.nn.feedfwd.FeedForwardNetwork;
import func.nn.feedfwd.FeedForwardNetworkBinary;
import func.nn.feedfwd.FeedForwardNeuralNetworkFactory;

import java.io.IOException;

/**
 * This class uses a standard FeedForwardNetwork and various optimization problems.
 * Currently it sets the new weights to be the optimal instance in the dataset,
 * generating a majority class. This does NOT optimize the network parameters!
 *
 * @author Joshua Wang joshuawang@gatech.edu
 */
public class BananaTestNoBackprop {

    private enum OptimizationType {
        RHC,
        SA,
        GA
    }

    public static String[] extractExtraArgs(String[] args, int index, int numExtraArgs) {
        String[] extraArgs = new String[numExtraArgs];
        for (int i = 0; i < numExtraArgs; i++) {
            int argsIndex = index + i + 1;
            if (argsIndex < args.length) {
                extraArgs[i] = args[argsIndex];
            }
        }
        return extraArgs;
    }

    /**
     * Tests out the perceptron with the classic xor test
     * @param args ignored
     */
    public static void main(String[] args) {
        // 1) Construct data instances for training.  These will also be run
        //    through the network at the bottom to verify the output
        String dataFile = "../../shared/test/banana.arff";
        OptimizationType optType = OptimizationType.RHC;
        int iterations = 5000;
        int foldCount = 10;
        String[] extraArgs = null;
        double learningRate = 1.0;

        try {

            // Retrieve configuration
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-d")) {
                    if (args.length > i) {
                        dataFile = args[i + 1];
                    }
                } else if (args[i].equals("-i")) {
                    if (args.length > i) {
                        iterations = Integer.parseInt(args[i + 1]);
                    }
                } else if (args[i].equals("-f")) {
                    if (args.length > i) {
                        foldCount = Integer.parseInt(args[i + 1]);
                    }
                } else if (args[i].equals("-l")) {
                    if (args.length > i) {
                        learningRate = Double.parseDouble(args[i + 1]);
                    }
                } else if (args[i].equals("-sa")) {
                    extraArgs = extractExtraArgs(args, i, 2);
                    optType = OptimizationType.SA;
                } else if (args[i].equals("-ga")) {
                    extraArgs = extractExtraArgs(args, i, 3);
                    optType = OptimizationType.GA;
                }
            }

            // Preprocess the data
            ArffDataSetReader dataReader = new ArffDataSetReader(dataFile);
            DataSet dataSet = dataReader.read();

            // Pre-labelling
            NormalizedFilter nf = new NormalizedFilter();
            nf.filter(dataSet);

            // Label
            LabelSelectFilter lsf = new LabelSelectFilter(2);
            lsf.filter(dataSet);

            // Post-labelling
            StandardizedFilter sf = new StandardizedFilter();
            sf.filter(dataSet);
            nf.filter(dataSet);

            TestTrainSplitFilter ttsf = new TestTrainSplitFilter(70);
            ttsf.filter(dataSet);
            DataSet trainingSet = ttsf.getTrainingSet();
            DataSet testingSet = ttsf.getTestingSet();

            kFoldSplitFilter kfsf = new kFoldSplitFilter(foldCount);
            kfsf.filter(trainingSet);

            FeedForwardNetwork bestNetwork = null;
            AccuracyTestMetric bestAcc = null;
            ConfusionMatrixTestMetric bestCm = null;
            int bestFold = -1;

            double accuracySum = 0.0;
            double timeSum = 0.0;
            for (int f = 0; f < foldCount; f++) {
                System.out.printf("\nTraining on validation pair %d:\n", f + 1);
                DataSet[] vPair = kfsf.getValidationPair(f);
                DataSet trainingData = vPair[0];
                Instance[] validationInstances = vPair[1].getInstances();

                // 2) Instantiate a network using the FeedForwardNeuralNetworkFactory.  This network
                //    will be our classifier.
                FeedForwardNeuralNetworkFactory factory = new FeedForwardNeuralNetworkFactory();
                // 2a) These numbers correspond to the number of nodes in each layer.
                //     This network has 4 input nodes, 3 hidden nodes in 1 layer, and 1 output node in the output layer.
                FeedForwardNetwork networkCont = factory.createClassificationNetwork(new int[]{2, 6, 1});
                FeedForwardNetwork network = new FeedForwardNetworkBinary(networkCont);
                //FeedForwardNetwork network = networkCont;

                // 3) Instantiate a measure, which is used to evaluate each possible set of weights.
                ErrorMeasure measure = new SumOfSquaresError();

                // 4) Instantiate an optimization problem, which is used to specify the dataset, evaluation
                //    function, mutator and crossover function (for Genetic Algorithms), and any other
                //    parameters used in optimization.
                NeuralNetworkOptimizationProblem nno = new NeuralNetworkOptimizationProblem(
                        trainingData,
                        network,
                        measure,
                        learningRate,
                        1.0
                );

                // 5) Instantiate a specific OptimizationAlgorithm, which defines how we pick our next potential
                //    hypothesis.
                OptimizationAlgorithm o = new RandomizedHillClimbing(nno, 1);
                if (optType.equals(OptimizationType.GA)) {
                    o = new StandardGeneticAlgorithm(
                            Integer.parseInt(extraArgs[0]),
                            Integer.parseInt(extraArgs[1]),
                            Integer.parseInt(extraArgs[2]),
                            nno
                    );
                } else if (optType.equals(OptimizationType.SA)) {
                    o = new SimulatedAnnealing(
                            Double.parseDouble(extraArgs[0]),
                            Double.parseDouble(extraArgs[1]),
                            nno
                    );
                }

                FixedIterationTrainer.printDebug = true;
                // 6) Instantiate a trainer.  The FixtIterationTrainer takes another trainer (in this case,
                //    an OptimizationAlgorithm) and executes it a specified number of times.
                FixedIterationTrainer trainer = new FixedIterationTrainer(o, iterations);

                // 7) Run the trainer.  This may take a little while to run, depending on the OptimizationAlgorithm,
                //    size of the data, and number of iterations.
                double error = trainer.train();
                System.out.printf("Final average error: %f\n", error);
                timeSum += trainer.getTimeToTrain() / 1E9;

                // 8) Once training is done, get the optimal solution from the OptimizationAlgorithm.  These are the
                //    optimal weights found for this network.
                Instance opt = o.getOptimal();
                network.setWeights(opt.getData());

                FeedForwardNetwork networkTrained = new FeedForwardNetworkBinary(network);
                //FeedForwardNetworkBinary networkTrained = network;

                //9) Run the training data through the network with the weights discovered through optimization, and
                //    print out the expected label and result of the classifier for each instance.
                AccuracyTestMetric acc = new AccuracyTestMetric();
                ConfusionMatrixTestMetric cm = new ConfusionMatrixTestMetric(new int[]{0, 1});
                Tester t = new NeuralNetworkTester(networkTrained, acc, cm);
                t.test(validationInstances);

                acc.printResults();
                cm.printResults();
                accuracySum += acc.getPctCorrect();

                if (bestAcc == null || acc.getPctCorrect() > bestAcc.getPctCorrect()) {
                    bestAcc = acc;
                    bestCm = cm;
                    bestNetwork = networkTrained;
                    bestFold = f;
                }
            }
            accuracySum /= foldCount;
            timeSum /= foldCount;

            System.out.printf("\nBest network at validation pair %d of %d:\n", bestFold + 1, foldCount);
            bestAcc.printResults();
            bestCm.printResults();


            System.out.println("\nResults on entire test set:");
            System.out.printf("%d-fold validation accuracy: %f%%\n", foldCount, accuracySum * 100.0);
            System.out.printf("%d-fold average training time: %fs\n", foldCount, timeSum);

            AccuracyTestMetric acc = new AccuracyTestMetric();
            ConfusionMatrixTestMetric cm = new ConfusionMatrixTestMetric(new int[]{0, 1});
            Tester t = new NeuralNetworkTester(bestNetwork, acc, cm);
            t.test(testingSet.getInstances());

            acc.printResults();
            cm.printResults();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
