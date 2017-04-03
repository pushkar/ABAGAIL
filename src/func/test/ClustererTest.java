package func.test;

import dist.AbstractConditionalDistribution;
import dist.Distribution;
import dist.MultivariateGaussian;
import func.EMClusterer;
import func.FunctionApproximater;
import func.KMeansClusterer;
import shared.*;
import shared.filt.ClusterFilter;
import shared.filt.LabelClusterFilter;
import shared.filt.LabelSelectFilter;
import shared.reader.ArffDataSetReader;
import shared.writer.ArffWriter;
import util.linalg.DenseVector;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.Vector;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ClustererTest {
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) throws Exception {
        String dataFile = new File("").getAbsolutePath() + "/src/shared/test/banana.arff";
        int k = 2;
        boolean useEM = false;
        boolean writeToFile = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-d")) {
                if (args.length > i + 1) {
                    dataFile = args[i + 1];
                }
            } else if (args[i].equals("-k")) {
                if (args.length > i + 1) {
                    k = Integer.parseInt(args[i + 1]);
                }
            } else if (args[i].equals("-em")) {
                useEM = true;
            } else if (args[i].equals("-w")) {
                writeToFile = true;
            }
        }

        // Preprocess the data
        ArffDataSetReader dataReader = new ArffDataSetReader(dataFile);
        DataSet dataSet = dataReader.read();
        int labelIndex = dataSet.getDescription().getAttributeCount() - 1;

        // Label Split
        LabelSelectFilter lsf = new LabelSelectFilter(labelIndex);
        lsf.filter(dataSet);

        DataSet clusteredDataSet = new DataSet(dataSet.getInstances(), dataSet.getDescription());

        FunctionApproximater clusterer;
        if (useEM) {
            clusterer = new EMClusterer(k, 1E-6, 1000);
        } else {
            clusterer = new KMeansClusterer(k);
        }

        clusterer.estimate(clusteredDataSet);

        HashMap<Double, HashMap<Double, Integer>> labelFreq = new HashMap<>();
        Set<Double> allClusters = new HashSet<>();

        for (Instance inst : dataSet.getInstances()) {
            double key = inst.getLabel().getData().get(0);
            double value = clusterer.value(inst).getData().get(0);
            allClusters.add(value);
            HashMap<Double, Integer> freq;
            if (labelFreq.containsKey(key)) {
                freq = labelFreq.get(key);
            } else {
                freq = new HashMap<>();
            }
            int count = 0;
            if (freq.containsKey(value)) {
                count = freq.get(value);
            }
            freq.put(value, count + 1);
            labelFreq.put(key, freq);
        }

        int m = labelFreq.size();
        int n = allClusters.size();

        double[][] clusterResults = new double[m + 1][n + 1];

        int c = 1;
        for (double cluster : allClusters) {
            clusterResults[0][c] = cluster;
            int r = 1;
            for (double label : labelFreq.keySet()) {
                if (c == 1) {
                    clusterResults[r][0] = label;
                }
                HashMap<Double, Integer> freq = labelFreq.get(label);
                if (freq.containsKey(cluster)) {
                    clusterResults[r][c] = freq.get(cluster);
                }
                r += 1;
            }
            c += 1;
        }

        Matrix confusionMatrix = new RectangularMatrix(clusterResults);


        System.out.println("Results:");
        System.out.println(confusionMatrix);
        System.out.println(labelFreq);

        DistanceMeasure measure = new EuclideanDistance();
        HashMap<Double, List<Double>> silhouetteMetric = ClusterMetric.silhouetteMetric(
                dataSet.getInstances(),
                clusterer,
                measure
        );
        double averageSilhouette = 0.0;
        Vector clusterAverageSilhouette = new DenseVector(silhouetteMetric.size());
        Vector clusterSize = new DenseVector(silhouetteMetric.size());
        int i = 0;
        int count = 0;
        for (double clusterId : silhouetteMetric.keySet()) {
            System.out.println("Measuring Cluster " + i);
            List<Double> clusterSilhouette = silhouetteMetric.get(clusterId);
            clusterSize.set(i, clusterSilhouette.size());
            double sum = 0;
            for (double s : clusterSilhouette) {
                sum += s;
                averageSilhouette += s;
                count += 1;
            }
            clusterAverageSilhouette.set(i, sum / clusterSilhouette.size());
            i += 1;
        }
        averageSilhouette /= count;
        System.out.printf("Silhouette Metric for %d instances:\n", dataSet.size());
        System.out.println(averageSilhouette);
        System.out.println();
        System.out.println(clusterSize);
        System.out.println(clusterAverageSilhouette);

        if (writeToFile) {
            ClusterFilter cf = new ClusterFilter(clusterer);
            cf.filter(dataSet);
            int endNdx = dataFile.lastIndexOf('.');
            String path = dataFile.substring(0, endNdx);
            String newFileName = path + "_" + (useEM ? "EM" : "KMEANS") + ".arff";

            System.out.println("Writing to " + newFileName);
            ArffWriter arffWriter = new ArffWriter(newFileName, dataSet);
            arffWriter.open();
            arffWriter.close();
            System.out.println("Completed.");
        }
    }
}