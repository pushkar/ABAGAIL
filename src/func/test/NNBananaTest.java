package func.test;

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
 * A classification test for CS 4641 on the Banana dataset
 * @author Joshua Wang jwang614@gatech.edu
 * @version 1.0
 */
public class NNBananaTest {

    /**
     * Backpropagates the perceptron with the banana dataset
     * @param args -f for number of folds -d for the data file
     */
    public static void main(String[] args) {
        try {
            BackPropagationNetworkFactory factory =
                    new BackPropagationNetworkFactory();

            // Handle command line arguments
            String dataFile = "../../shared/test/banana.arff";
            int foldCount = 10;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-d") && args.length > i) {
                    dataFile = args[i + 1];
                } else if (args[i].equals("-f") && args.length > i) {
                    foldCount = Integer.parseInt(args[i + 1]);
                }
            }

            // Preprocess the data
            ArffDataSetReader dataReader = new ArffDataSetReader(dataFile);
            DataSet dataSet = dataReader.read();

            // Pre-labelling (everything, labels included)
            NormalizedFilter nf = new NormalizedFilter();
            nf.filter(dataSet);

            // Label Split
            LabelSelectFilter lsf = new LabelSelectFilter(2);
            lsf.filter(dataSet);

            // Post-labelling (attributes only)
            StandardizedFilter sf = new StandardizedFilter();
            sf.filter(dataSet);
            nf.filter(dataSet);

            TestTrainSplitFilter ttsf = new TestTrainSplitFilter(70);
            ttsf.filter(dataSet);

            DataSet trainingSet = ttsf.getTrainingSet();
            DataSet testingSet = ttsf.getTestingSet();

            // Split into folds for cross-validation
            kFoldSplitFilter kfsf = new kFoldSplitFilter(foldCount);
            kfsf.filter(trainingSet);

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

                // Generate our learner
                BackPropagationNetwork network = factory.createClassificationNetwork(new int[] { 2, 6, 1 });

                // Train our learner
                WeightUpdateRule weightUpdateRule = new StandardUpdateRule(0.0012,0.0);
                BatchBackPropagationTrainer backPropagationTrainer = new BatchBackPropagationTrainer(trainingData, network,
                        new SumOfSquaresError(), weightUpdateRule);

                ThresholdTrainer trainer = new ThresholdTrainer(backPropagationTrainer,0.045,10000);
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
