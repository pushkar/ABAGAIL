package shared;

import func.FunctionApproximater;
import util.linalg.DenseVector;
import util.linalg.Matrix;
import util.linalg.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Joshua Wang
 */
public class ClusterMetric {
    public static HashMap<Double, List<Double>> silhouetteMetric(Instance[] instances,
                                          FunctionApproximater clusterer,
                                          DistanceMeasure measure) {
        HashMap<Double, List<Double>> silhouetteMap = new HashMap<>();
        double[][] distMatrix = new double[instances.length][instances.length];
        double[] clusterIds = new double[instances.length];
        for (int i = 0; i < instances.length; i++) {
            clusterIds[i] = getClusterId(instances[i], clusterer);
            for (int j = 0; j < i; j++) {
                distMatrix[i][j] = measure.value(instances[i], instances[j]);
                distMatrix[j][i] = distMatrix[i][j];
            }
        }
        for (int i = 0; i < instances.length; i++) {
            //System.out.println("Instance " + i);
            double cId = getClusterId(instances[i], clusterer);
            double a = getAverageIntraDissimilarity(i, instances, clusterIds, distMatrix);
            double b = getAverageInterDissimilarity(i, instances, clusterIds, distMatrix);
            double s = (b - a) / (Math.max(a, b));
            List<Double> silhouettes = new LinkedList<>();
            if (silhouetteMap.containsKey(cId)) {
                silhouettes = silhouetteMap.get(cId);
            }
            silhouettes.add(s);
            silhouetteMap.put(cId, silhouettes);
        }

        return silhouetteMap;
    }

    private static double getAverageIntraDissimilarity(int index,
                                                       Instance[] instances,
                                                       double[] clusterIds,
                                                       double[][] distMatrix) {
        double aSum = 0;
        Instance candidate = instances[index];
        double clusterId = clusterIds[index];
        for (int i = 0; i < instances.length; i++) {
            double cId = clusterIds[i];
            if (i != index && cId == clusterId) {
                aSum += distMatrix[i][index];
            }
        }
        return aSum / (instances.length - 1);
    }

    private static double getAverageInterDissimilarity(int index,
                                                       Instance[] instances,
                                                       double[] clusterIds,
                                                       double[][] distMatrix) {
        HashMap<Double, double[]> clusterMap = new HashMap<>();
        Instance candidate = instances[index];
        double clusterId = clusterIds[index];
        for (int i = 0; i < instances.length; i++) {
            double cId = clusterIds[i];
            if (i != index && cId != clusterId) {
                double[] sumCount = new double[2];

                if (clusterMap.containsKey(cId)) {
                    sumCount = clusterMap.get(cId);
                }

                sumCount[0] += distMatrix[i][index];
                sumCount[1] += 1;

                clusterMap.put(cId, sumCount);
            }
        }

        double minB = -1;

        for (double cId : clusterMap.keySet()) {
            double[] sumCount = clusterMap.get(cId);
            double b = sumCount[0] / sumCount[1];
            if (minB == -1 || b < minB) {
                minB = b;
            }
        }

        return minB;
    }

    private static double getClusterId(Instance instance, FunctionApproximater clusterer) {
        return clusterer.value(instance).getData().get(0);
    }
}