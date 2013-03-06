package func;

import shared.DataSet;
import shared.DistanceMeasure;
import shared.EuclideanDistance;
import shared.Instance;
import util.linalg.DenseVector;
import dist.*;
import dist.Distribution;
import dist.DiscreteDistribution;

/**
 * A K means clusterer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class KMeansClusterer extends AbstractConditionalDistribution implements FunctionApproximater {
    /**
     * The cluster centers
     */
    private Instance[] clusterCenters;
    
    /**
     * The number of clusters
     */
    private int k;
    
    /**
     * The distance measure
     */
    private DistanceMeasure distanceMeasure;
    
    /**
     * Make a new k means clustere
     * @param k the k value
     * @param distanceMeasure the distance measure
     */
    public KMeansClusterer(int k) {
        this.k = k;
        this.distanceMeasure = new EuclideanDistance();
    }
    
    /**
     * Make a new clusterer
     */
    public KMeansClusterer() {
        this(2);
    }

    /**
     * @see func.Classifier#classDistribution(shared.Instance)
     */
    public Distribution distributionFor(Instance instance) {
        double[] distribution = new double[k];
        for (int i = 0; i < k; i++) {
            distribution[i] +=
                1/distanceMeasure.value(instance, clusterCenters[i]);   
        }
        double sum = 0;
        for (int i = 0; i < distribution.length; i++) {
            sum += distribution[i];
        }
        if (Double.isInfinite(sum)) {
            sum = 0;
            for (int i = 0; i < distribution.length; i++) {
                if (Double.isInfinite(distribution[i])) {
                    distribution[i] = 1;
                    sum ++;
                } else {
                    distribution[i] = 0;
                }
            }
        }
        for (int i = 0; i < distribution.length; i++) {
            distribution[i] /= sum;
        }
        return new DiscreteDistribution(distribution);
    }

    /**
     * @see func.FunctionApproximater#estimate(shared.DataSet)
     */
    public void estimate(DataSet set) {
        clusterCenters = new Instance[k];
        int[] assignments = new int[set.size()];
        // random initial centers
        for (int i = 0; i < clusterCenters.length; i++) {
            int pick;
            do {
                pick = Distribution.random.nextInt(set.size());
            } while (assignments[pick] != 0);
            assignments[pick] = 1;
            clusterCenters[i] = (Instance) set.get(pick).copy();
        }
        boolean changed = false;
        // the main loop
        do {
            changed = false;
            // make the assignments
            for (int i = 0; i < set.size(); i++) {
                // find the closest center
                int closest = 0;
                double closestDistance = distanceMeasure
                    .value(set.get(i), clusterCenters[0]);
                for (int j = 1; j < k; j++) {
                    double distance = distanceMeasure
                        .value(set.get(i), clusterCenters[j]);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closest = j;
                    }
                }
                if (assignments[i] != closest) {
                    changed = true;
                }
                assignments[i] = closest;
            }
            if (changed) {
                double[] assignmentCount = new double[k];
                // make the new clusters
                for (int i = 0; i < k; i++) {
                    clusterCenters[i].setData(new DenseVector(
                        clusterCenters[i].getData().size()));
                }
                for (int i = 0; i < set.size(); i++) {
                    clusterCenters[assignments[i]].getData().plusEquals(
                        set.get(i).getData().times(set.get(i).getWeight()));
                    assignmentCount[assignments[i]] += set.get(i).getWeight();    
                }
                for (int i = 0; i < k; i++) {
                    clusterCenters[i].getData().timesEquals(1/assignmentCount[i]);
                }
            }
        } while (changed);
    }

    /**
     * @see func.FunctionApproximater#value(shared.Instance)
     */
    public Instance value(Instance data) {
        return distributionFor(data).mode();
    }

    /**
     * Get the cluster centers
     * @return the cluster centers
     */
    public Instance[] getClusterCenters() {
        return clusterCenters;
    }
        
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "k = " + k + "\n";
        for (int i = 0; i < clusterCenters.length; i++) {
            result += clusterCenters[i].toString() + "\n";
        }
        return result;
    }


}
