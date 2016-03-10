package opt.test;

import dist.*;
import opt.*;
import opt.example.*;
import opt.ga.*;
import shared.*;
import func.nn.backprop.*;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Implement randomized algorithms on neural network using HAR dataset
 *
 * @author Yushi Sun
 * @version 1.0
 */
public class HARTest {
    private static Instance[] instances = initializeInstances();
    private static Instance[] valInstances = initializeValInstances();

    //network definition for har
    private static int inputLayer = 561, hiddenLayer = 6, outputLayer = 6, trainingIterations = 1000;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    
    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet set = new DataSet(instances);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    private static String[] oaNames = {"RHC", "SA", "GA"};
    private static String results = "";

    private static DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) {
        for(int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(
                new int[] {inputLayer, hiddenLayer, outputLayer});
            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
        }

        oa[0] = new RandomizedHillClimbing(nnop[0]);
        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);

        //for every optimization algorithm
        for(int i = 0; i < oa.length; i++) {
            //record start time
            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
            //train the optimization algorithm
            train(oa[i], networks[i], oaNames[i]); //trainer.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);

            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());

            int predicted, actual;
            start = System.nanoTime();
            for(int j = 0; j < valInstances.length; j++) {
                networks[i].setInputValues(valInstances[j].getData());
                networks[i].run();

                actual = valInstances[j].getLabel().getData().argMax();
                predicted = networks[i].getOutputValues().argMax();

                double trash = predicted == actual ? correct++ : incorrect++;

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

    /**
     * train the network
     */
    private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) {
        //print optimization algorithm name
        System.out.println("\nError results for " + oaName + "\n---------------------------");

        //for every iteration
        for(int i = 0; i < trainingIterations; i++) {
            //call oa.train to optimize one stage
            double fitness = oa.train();

            //the training error
            double terror = 1/fitness;
            //normalize the training error over the number of training examples
            terror /= instances.length;
            
            //now calculate the test error
            double valerror = 0;
            //for every instance
            for(int j = 0; j < valInstances.length; j++) {
                //set the input value
                network.setInputValues(valInstances[j].getData());
                //run the network
                network.run();

                //get the output values
                Instance outputLabel = new Instance(network.getOutputValues());
                //accumulate the terror
                valerror += measure.value(outputLabel, valInstances[j]);
            }
            valerror /= valInstances.length;

            //print the error
            System.out.println("iteration: " + Integer.toString(i) + " training error: "+df.format(terror)+" validation error: "+df.format(valerror));
        }
    }

    /**
     * Read the txt file and build the array of instance
     */

    private static Instance[] initializeInstances() {
        System.out.println("reading training set...");

        //first dim: number of instance
        //second dim: 0 for attributes, 1 for prediction
        //third dim: vector of attributes
        final int NINSTANCE = 7352;
        final int NATTR = 561;
        final int NCLASS = 6;
        double[][] attributes = new double[NINSTANCE][];
        int [] classes = new int[NINSTANCE];

        try {
            BufferedReader xbr = new BufferedReader(new FileReader(new File("src/opt/test/har/X_train.txt")));
            BufferedReader ybr = new BufferedReader(new FileReader(new File("src/opt/test/har/y_train.txt")));

            for(int i = 0; i < attributes.length; i++) {
                String line = xbr.readLine().trim();
                //System.out.println(line);
                Scanner xscan = new Scanner(line);
                Scanner yscan = new Scanner(ybr.readLine().trim());
                //xscan.useDelimiter(" ");

                attributes[i] = new double[NATTR]; // NATTR attributes in har

                //read the attributes
                for(int j = 0; j < NATTR; j++) {
                    String str = xscan.next();
                    //System.out.println(str);
                    attributes[i][j] = Double.parseDouble(str);
                }

                //read the ground truth class
                classes[i] = Integer.parseInt(yscan.next());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[NINSTANCE];

        for(int i = 0; i < NINSTANCE; i++) {
            instances[i] = new Instance(attributes[i]);
            double[] label = new double[NCLASS];
            label[classes[i]-1]=1.0;
            instances[i].setLabel(new Instance(label));
        }

        return instances;
    }
    private static Instance[] initializeValInstances() {
        System.out.println("reading test set...");
        //first dim: number of instance
        //second dim: 0 for attributes, 1 for prediction
        //third dim: vector of attributes
        final int NINSTANCE = 2947;
        final int NATTR = 561;
        final int NCLASS = 6;
        double[][] attributes = new double[NINSTANCE][];
        int [] classes = new int[NINSTANCE];

        try {
            BufferedReader xbr = new BufferedReader(new FileReader(new File("src/opt/test/har/X_test.txt")));
            BufferedReader ybr = new BufferedReader(new FileReader(new File("src/opt/test/har/y_test.txt")));

            for(int i = 0; i < attributes.length; i++) {
                Scanner xscan = new Scanner(xbr.readLine());
                Scanner yscan = new Scanner(ybr.readLine());
                //xscan.useDelimiter(" ");

                attributes[i] = new double[NATTR]; // NATTR attributes in har

                //read the attributes
                for(int j = 0; j < NATTR; j++)
                    attributes[i][j] = Double.parseDouble(xscan.next());

                //read the ground truth class
                classes[i] = Integer.parseInt(yscan.next());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[NINSTANCE];

        for(int i = 0; i < NINSTANCE; i++) {
            instances[i] = new Instance(attributes[i]);
            double[] label = new double[NCLASS];
            label[classes[i]-1]=1.0;
            instances[i].setLabel(new Instance(label));
        }

        return instances;
    }
}
