package example;

import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.StandardGeneticAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;
import util.InstanceUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * an example of how a dataset can be used to perform neural network analysis by
 * replacing backpropagation with the 4 randomized optimization techniques.
 * Some of the parameters such as input layer, hidden layer and number of units is specific to this dataset.
 * and may need to be modified for other datasets.
 */
public class EyeStateTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EyeStateTest.class);


    private static final int NUM_COLS = 15;
    private static final int LABEL_POS = 14;
    private static int inputLayer = 128, hiddenLayer = 64, outputLayer = 1;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private static String[] oaNames = {"RHC", "SA", "GA"};

    private static int[] optIterations = new int[]{1000, 2000, 100};

    private BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    private BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];
    private OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    private DataSet set;
    private Instance[] trainSet;

    public EyeStateTest(String filename) throws IOException {
        Instance[] allInstances = InstanceUtil.loadInstances(filename, NUM_COLS, true, LABEL_POS);
        List<Instance[]> testTrainList = InstanceUtil.testTrainSplit(allInstances, 5);
        trainSet = testTrainList.get(0);
        set = new DataSet(trainSet);
        // run setup
        setup();
        process();
    }


    private void setup() {
        for (int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(new int[]{inputLayer, hiddenLayer, outputLayer});
            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], new SumOfSquaresError());
        }
        oa[0] = new RandomizedHillClimbing(nnop[0]);
        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);
    }

    private void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName, Instance[] trainSet,
                       int myIterations) throws IOException {
        int printThreshold = Math.round(myIterations / 10);
        ErrorMeasure measure = new SumOfSquaresError();
        LOGGER.info("\nError results for " + oaName + "\n---------------------------");
        BufferedWriter writer = new BufferedWriter(new FileWriter("output/" + oaName + "_error.csv"));
        for (int i = 0; i < myIterations; i++) {
            oa.train();
            double error = 0;
            for (int j = 0; j < trainSet.length; j++) {
                network.setInputValues(trainSet[j].getData());
                network.run();
                Instance output = trainSet[j].getLabel(), example = new Instance(network.getOutputValues());
                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
                error += measure.value(output, example);
            }
            writer.write(i + "," + df.format(error) + "\n");
            if (printThreshold != 0 && i % printThreshold == 0) {
                LOGGER.info("{}-> {}", i, df.format(error));
            }
        }
        writer.close();
    }

    private void process() throws IOException {
        for (int i = 0; i < oa.length; i++) {
            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
            train(oa[i], networks[i], oaNames[i], trainSet, optIterations[i]); //trainer.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10, 9);

            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());

            double predicted, actual;
            String acc = null;
            start = System.nanoTime();
            int printThreshold = Math.round(trainSet.length / 10);

            BufferedWriter writer = new BufferedWriter(new FileWriter("output/" + oaNames[i] + "_acc_testTime.csv"));

            for (int j = 0; j < trainSet.length; j++) {
                networks[i].setInputValues(trainSet[j].getData());
                networks[i].run();

                predicted = trainSet[j].getLabel().getContinuous();
                actual = networks[i].getOutputValues().get(0);
                if (Math.abs(predicted - actual) < 0.5) {
                    correct++;
                } else {
                    incorrect++;
                }
                end = System.nanoTime();
                testingTime = end - start;
                testingTime /= Math.pow(10, 9);
                acc = df.format(correct / (correct + incorrect) * 100);
                if (j % printThreshold == 0) {
                    LOGGER.info("\n[{}:{}]-> Results for " + oaNames[i] + ": Correct:" + correct +
                                    " Incorrect: " + incorrect + "  Accuracy: "
                                    + acc + "% [ Testing time: " + df.format(testingTime) + " sec.]\n", oaNames[i],
                            optIterations[i]);
                }
                writer.write(j + "," + df.format(testingTime) + "," + acc + "\n");
            }
            writer.close();
            LOGGER.info("[{}]==> Final: Accuracy={}% Training Time: {} sec. Total Time:{} sec.", oaNames[i],
                    acc, df.format(trainingTime),
                    df.format((System.nanoTime() - start) / Math.pow(10, 9)));
        }
    }
}