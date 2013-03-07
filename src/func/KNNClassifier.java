package func;

import dist.*;
import dist.Distribution;
import dist.DiscreteDistribution;
import func.inst.KDTree;
import shared.*;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;

/**
 * A knn classifier
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class KNNClassifier extends AbstractConditionalDistribution implements FunctionApproximater {
    
    /**
     * The distance measure
     */
    private DistanceMeasure distanceMeasure;
    
    /**
     * The range limit for the neighbors
     */
    private double range;
    
    /**
     * The number of neighbors
     */
    private int k;
    
    /**
     * Whether or not to weight by distance
     */
    private boolean weightByDistance;
    
    /**
     * The range of classes
     */
    private int classRange;
    
    /**
     * The tree
     */
    private KDTree tree;
    
    /**
     * Make a new knn classifier
     */
    public KNNClassifier() {
        this(1, new EuclideanDistance());
    }
    
    /**
     * Build a new classifier
     * @param examples the examples
     * @param k the k value
     * @param measure the distance measure
     */
    public KNNClassifier(int k,
            DistanceMeasure measure) {
        this(k, false, measure, -1);
    }
    
    /**
     * Build a new classifier
     * @param examples the examples
     * @param k the k value
     * @param weight the weight
     * @param measure the distance measure
     */
    public KNNClassifier(int k, boolean weight,
            DistanceMeasure measure) {
        this(k, weight, measure, -1);
    }
    
    /**
     * Build a new classifier
     * @param examples the examples
     * @param k the k value
     * @param weight the weight
     * @param measure the distance measure
     * @param range the range
     */
    public KNNClassifier(int k, boolean weight,
            DistanceMeasure measure, double range) {
        this.k = k;
        this.weightByDistance = weight;
        this.range = range;
        this.distanceMeasure = measure;

    }
    
    /**
     * Estimate from data
     * @param examples the examples
     */
    public void estimate(DataSet examples) {
        if (examples.getDescription() == null) {
            examples.setDescription(new DataSetDescription(examples));
        }
        classRange = examples.getDescription().getLabelDescription().getDiscreteRange();
        tree = new KDTree(examples, distanceMeasure);
    }
    
    /**
     * Get the class distribution
     * @param data the data
     * @return the class distribution
     */
    public Distribution distributionFor(Instance data) {
        double[] distribution = new double[classRange];
        Object[] results;
        if (range > 0) {
            results = tree.knnrange(data, k, range);
        } else {
            results = tree.knn(data, k);
        }
        for (int i = 0; i < results.length; i++) {
            Instance neighbor = (Instance) results[i];
            if (weightByDistance) {
                distribution[neighbor.getLabel().getDiscrete()] +=
                     neighbor.getWeight()/distanceMeasure.value(data, neighbor);
            } else {
                distribution[neighbor.getLabel().getDiscrete()] +=
                     neighbor.getWeight();
            }
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
     * Get the classification for an example
     * @param data the data to get the classification for
     * @return the classification
     */
    public Instance value(Instance data) {
        return distributionFor(data).mode();
    }
    
    /**
     * Get the distance measure
     * @return the distance measure
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * Get the k value
     * @return the k value
     */
    public int getK() {
        return k;
    }

    /**
     * Does it weight by distance
     * @return true if it does
     */
    public boolean isWeightByDistance() {
        return weightByDistance;
    }

    /**
     * Set the distance measure
     * @param measure the new measure
     */
    public void setDistanceMeasure(DistanceMeasure measure) {
        distanceMeasure = measure;
    }

    /**
     * Set the k value
     * @param i the new k
     */
    public void setK(int i) {
        k = i;
    }

    /**
     * Set the new weighting policy
     * @param b the new policy
     */
    public void setWeightByDistance(boolean b) {
        weightByDistance = b;
    }

}







