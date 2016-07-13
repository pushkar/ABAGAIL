package func.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import func.inst.KDTree;
import opt.test.AbaloneTest;
import shared.DataSet;
import shared.Instance;

/**
 * KNN test built to test the speed of splitter selectors in func.inst.KDTree
 * @author Robert Smith smithrobertlawrence@gmail.com
 * @version 1.0
 */
public class KNNClassifierAbaloneTest {

    public static void main(String[] args){
        Instance[] instances = initializeAbaloneInstances();
        Instance[] training = new Instance[3000];
        Instance[] testing = new Instance[1177];
        for (int i = 0; i < training.length; i++){
            training[i] = instances[i];
        }
        for (int i = 0; i < testing.length; i++){
            testing[i] = instances[training.length+i];
        }
        long buildTime = System.nanoTime();
        KDTree tree = new KDTree(new DataSet(training));
        buildTime = System.nanoTime() - buildTime;
        System.out.println("BuildTime = " + buildTime);

        for (int k = 1; k < 10; k++){
            long testTime = 0;
            for (int i = 0; i < testing.length; i++){
                long searchTime = System.nanoTime();
                tree.knn(testing[i], k);
                searchTime = System.nanoTime() - searchTime;
                testTime += searchTime;
            }
            testTime /= testing.length;
            System.out.println("K = " + k + ", average search time = " + testTime);
        }


    }

    private static Instance[] initializeAbaloneInstances() {

        double[][][] attributes = new double[4177][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/opt/test/abalone.txt")));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[7]; // 7 attributes
                attributes[i][1] = new double[1];

                for(int j = 0; j < 7; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

                attributes[i][1][0] = Double.parseDouble(scan.next());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            // classifications range from 0 to 30; split into 0 - 14 and 15 - 30
            instances[i].setLabel(new Instance(attributes[i][1][0] < 15 ? 0 : 1));
        }

        return instances;
    }
}
