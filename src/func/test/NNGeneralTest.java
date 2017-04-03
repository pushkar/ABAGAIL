package func.test;

import java.io.File;
import java.io.IOException;

import func.nn.backprop.*;
import shared.filt.*;
import shared.reader.ArffDataSetReader;
import shared.DataSet;
import shared.Instance;
import shared.SumOfSquaresError;
import shared.ThresholdTrainer;
import shared.tester.*;

/**
 * A classification test using a multilayer perceptron
 * @author Joshua Wang jwang614@gatech.edu
 * @version 1.0
 */
public class NNGeneralTest {

    /**
     * Backpropagates the perceptron for a given dataset
     * @param args -f for number of folds -d for the data file
     */
    public static void main(String[] args) {
        try {
            BackPropagationNetworkFactory factory =
                    new BackPropagationNetworkFactory();

            // Handle command line arguments
            String dataFile = new File("").getAbsolutePath() + "/src/shared/test/banana.arff";
            String hiddenLayers = "1";
            double learningRate = 0.0012;
            double threshold = 0.045;
            int maxIterations = 10000;

            int foldCount = 10;
            for (int i = 0; i < args.length; i++) {
                if (args.length > i + 1) {
                    String nextArg = args[i + 1];
                    if (args[i].equals("-d")) {
                        dataFile = nextArg;
                    } else if (args[i].equals("-f")) {
                        foldCount = Integer.parseInt(nextArg);
                    } else if (args[i].equals("-h")) {
                        hiddenLayers = nextArg;
                    } else if (args[i].equals("-l")) {
                        learningRate = Double.parseDouble(nextArg);
                    } else if (args[i].equals("-i")) {
                        maxIterations = Integer.parseInt(nextArg);
                    } else if (args[i].equals("-t")) {
                        threshold = Double.parseDouble(nextArg);
                    }
                }
            }

            // Preprocess the data
            ArffDataSetReader dataReader = new ArffDataSetReader(dataFile);
            DataSet dataSet = dataReader.read();

            // Pre-labelling (everything, labels included)
            NormalizedFilter nf = new NormalizedFilter();
            //nf.filter(dataSet);

            // Label Split
            LabelSelectFilter lsf = new LabelSelectFilter(dataSet.getDescription().getAttributeCount() - 1);
            lsf.filter(dataSet);

            // Post-labelling (attributes only)
            StandardizedFilter sf = new StandardizedFilter();
            //sf.filter(dataSet);
            //nf.filter(dataSet);

            RandomOrderFilter rof = new RandomOrderFilter();
            rof.filter(dataSet);

            TestTrainSplitFilter ttsf = new TestTrainSplitFilter(70);
            ttsf.filter(dataSet);

            DataSet trainingSet = ttsf.getTrainingSet();
            DataSet testingSet = ttsf.getTestingSet();

            System.out.println("Training Set Size: " + trainingSet.size());

            // Split into folds for cross-validation
            kFoldSplitFilter kfsf = new kFoldSplitFilter(foldCount);
            kfsf.filter(trainingSet);

            // Get the layers
            String[] hiddenSplit = hiddenLayers.split(",");
            int[] layers = new int[2 + hiddenSplit.length];

            layers[0] = dataSet.getDescription().getAttributeCount();
            for (int i = 0; i < hiddenSplit.length; i++) {
                layers[i + 1] = Integer.parseInt(hiddenSplit[i]);
            }
            layers[layers.length - 1] = 1;

            BackPropagationNetwork bestNetwork = null;
            AccuracyTestMetric bestAcc = null;
            ConfusionMatrixTestMetric bestCm = null;
            int bestFold = -1;

            double timeSum = 0.0;
            double accuracySum = 0.0;
            for (int f = 0; f < foldCount; f++) {
                System.out.printf("\nTraining on validation pair %d:\n", f + 1);
                DataSet[] vPair = kfsf.getValidationPair(f);
                DataSet trainingData = vPair[0];
                Instance[] validationInstances = vPair[1].getInstances();
                System.out.println("Fold Validation Size: " + validationInstances.length);

                // Generate our learner
                BackPropagationNetwork network = factory.createClassificationNetwork(layers);

                // Train our learner
                WeightUpdateRule weightUpdateRule = new StandardUpdateRule(learningRate,0.0);
                BatchBackPropagationTrainer backPropagationTrainer = new BatchBackPropagationTrainer(trainingData, network,
                        new SumOfSquaresError(), weightUpdateRule);

                ThresholdTrainer trainer = new ThresholdTrainer(backPropagationTrainer,threshold,maxIterations);
                double error = trainer.train();
                timeSum += trainer.getTimeToTrain() / 1E9;

                BackPropagationNetwork networkTrained = new BackPropagationNetworkBinary(network);

                System.out.printf("\nTrained in %d iterations with avg SSE of %f\n", trainer.getIterations(), error);
                double[] weights = network.getWeights();
                for (int i = 0; i < weights.length; i++) {
                    System.out.printf("%f,",weights[i]);
                }
                System.out.println();

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
