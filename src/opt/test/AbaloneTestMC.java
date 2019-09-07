package opt.test; 

import opt.*;
import opt.example.*;
import opt.ga.*;
import shared.*;
import func.nn.backprop.*;
import shared.filt.LabelSplitFilter;
import shared.reader.CSVDataSetReader;
import shared.reader.DataSetReader;
import java.io.*;
import java.text.*;

/**
 * Multiclass implementation of randomized hill climbing, simulated annealing, and genetic algorithm to
 * find optimal weights to a neural network classifying abalone age.
 *
 * @author Hannah Lau
 * @version 1.0
 * @modified by John Mansfield
 */
public class AbaloneTestMC {
    private static Instance[] instances;
    private static DataSet set;
    private static int inputLayer = 9, hiddenLayer = 5, outputLayer = 29, trainingIterations = 1000;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    private static ErrorMeasure measure = new SumOfSquaresError();
    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];
    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    private static String[] oaNames = {"RHC", "SA", "GA"};
    private static String results = "";
    private static DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) throws Exception {
        instances = initializeInstances();
        set = new DataSet(instances);
        System.out.println(new DataSetDescription(set));
        for(int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(
                    new int[] {inputLayer, hiddenLayer, outputLayer});
            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
        }

        oa[0] = new RandomizedHillClimbing(nnop[0]);
        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(100, 50, 10, nnop[2]);

        for(int i = 0; i < oa.length; i++) {
            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
            train(oa[i], networks[i], oaNames[i]); //trainer.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);

            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());

            double predicted, actual;
            start = System.nanoTime();
            for(int j = 0; j < instances.length; j++) {
                networks[i].setInputValues(instances[j].getData());
                networks[i].run();

                actual = instances[j].getLabel().getData().argMax();
                predicted = networks[i].getOutputValues().argMax();
                if (actual-2<=predicted && predicted<=actual+2) {
                    ++correct;
                } else {
                    ++incorrect;
                }
            }
            end = System.nanoTime();
            testingTime = end - start;
            testingTime /= Math.pow(10,9);

            results +=  "\nResults for " + oaNames[i] + ": \nCorrectly classified " + correct + " instances." +
                    "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                    + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                    + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        }

        System.out.println(results);
    }

    private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) {
        System.out.println("\nError results for " + oaName + "\n---------------------------");

        for(int i = 0; i < trainingIterations; i++) {
            oa.train();

            double error = 0;
            for(int j = 0; j < instances.length; j++) {
                network.setInputValues(instances[j].getData());
                network.run();

                Instance output = instances[j].getLabel(), example = new Instance(network.getOutputValues());
                example.setLabel(new Instance(network.getOutputValues()));
                error += measure.value(output, example);
            }

            System.out.println(i + " | " + trainingIterations + ": " + df.format(error));
        }
    }

    //built-in csv reader
    private static Instance[] initializeInstances() throws Exception {
        DataSetReader dsr = new CSVDataSetReader((new File("src/opt/test/abalone.txt")).getAbsolutePath());
        DataSet ds = dsr.read();
        LabelSplitFilter lsf = new LabelSplitFilter();
        lsf.filter(ds);

        //Bin continuous attributes
        //ContinuousToDiscreteFilter ctdf = new ContinuousToDiscreteFilter(50);
        //ctdf.filter(ds);
        //System.out.println(new DataSetDescription(ds));

        //convert label to array and set label
        Instance[] instances = ds.getInstances();
        for (int i = 0; i < instances.length; ++i) {
            double[] label = new double[29];
            label[(int) instances[i].getLabel().getContinuous()-1]=1;
            instances[i].setLabel(new Instance(label));
        }
        return instances;
    }
}
