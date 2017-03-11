package func;

import dist.*;
import dist.Distribution;
import dist.DiscreteDistribution;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;

/**
 * A class for constructing a ensemble of classifiers
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class AdaBoostClassifier extends AbstractConditionalDistribution implements FunctionApproximater  {

    /**
     * The supplier of classifiers to use
     */
    private final FunctionApproximaterSupplier classifierSupplier;
    
    /**
     * The classifiers themselves
     */
    private FunctionApproximater[] classifiers;
    
    /**
     * The weights for each of the classifiers
     */
    private double[] weights;
    
    /**
     * The range of the class
     */
    private int classRange;
    
    /**
     * The size of the ensemble
     */
    private int size;

    /**
     * Create a new ensemble given a {@link FunctionApproximaterSupplier} of classifiers.
     * @param size the size of the ensemble
     * @param classifierSupplier the {@link FunctionApproximaterSupplier} that supplies classifiers to use
     */
    public AdaBoostClassifier(int size, FunctionApproximaterSupplier classifierSupplier) {
        this.size = size;
        this.classifierSupplier = classifierSupplier;
    }

    /**
     * Create a new ensemble with a given classifier type
     * @param size the size of the ensemble
     * @param classifier the classifier class to use
     */
    public AdaBoostClassifier(int size, final Class classifier) {
        this(size, new FunctionApproximaterSupplier() {
            @Override public FunctionApproximater get() {
                try {
                    return (FunctionApproximater)
                        classifier.getConstructor(new Class[0]).newInstance(new Object[0]);
                } catch (Exception e) {
                    throw new UnsupportedOperationException("Could not create " + classifier);
                }
            }
        });
    }

    /**
     * Create a new decision stump ensemble
     * @param size the size of the ensemble
     */
    public AdaBoostClassifier(int size) {
        this(size, DecisionStumpClassifier.class);
    }
    
    /**
     * Create a new default ensemble
     */
    public AdaBoostClassifier() {
        this(100);
    }

    /**
     * Build the ensemble
     * @param instances the instances to train with
     */
    public void estimate(DataSet instances) {
        classifiers = new FunctionApproximater[size];
        weights = new double[size];
        // initialize the weights of the instances
        for (int i = 0; i < instances.size(); i++) {
            instances.get(i).setWeight(1.0 / instances.size());
        }
        // getting some info
        if (instances.getDescription() == null) {
             DataSetDescription desc = new DataSetDescription();
             desc.induceFrom(instances);
             instances.setDescription(desc);
         }               
        classRange = instances.getDescription().getLabelDescription().getDiscreteRange();
        for (int i = 0; i < classifiers.length; i++) {
            // make a new classifier
            classifiers[i] = classifierSupplier.get();
            classifiers[i].estimate(instances);
            // find the error for the newest classifier
            double error = 0;
            for (int j = 0; j < instances.size(); j++) {
                if (classifiers[i].value(instances.get(j)).getDiscrete()
                        != instances.get(j).getLabel().getDiscrete()) {
                    error += instances.get(j).getWeight();            
                }
            }
            double beta = error / (1 - error);
            // set the weight of the classifier
            weights[i] = Math.log(1 / beta);
            // the classifier didn't do any good
            if (error == .5) {
                classifiers[i] = null;
                break;
            } else if (error == 0) {
                break;
            }
            // readjust the weights of the instances
            // and calculate the sum of the weights
            double weightSum = 0;
            for (int j = 0; j < instances.size(); j++) {
                if (classifiers[i].value(instances.get(j)).getDiscrete()
                        == instances.get(j).getLabel().getDiscrete()) {
                    instances.get(j).setWeight(instances.get(j).getWeight()
                        * beta);
                    weightSum += instances.get(j).getWeight();          
                } else {
                    weightSum += instances.get(j).getWeight();
                }
            }
            // normalize the weights
            for (int j = 0; j < instances.size(); j++) {
                instances.get(j).setWeight(instances.get(j).getWeight() / weightSum);
            }


        }
    }
    
    /**
     * Get the classification for an instances
     * @param data the data to classify
     * @return the class distribution
     */
    public Instance value(Instance data) {
        double[] votes = new double[classRange];
        for (int i = 0; i < classifiers.length && classifiers[i] != null; i++) {
            votes[classifiers[i].value(data).getDiscrete()] +=
                weights[i];
        }
        int classification = 0;
        for (int i = 1; i < votes.length; i++) {
            if (votes[i] > votes[classification]) {
                classification = i;
            }
        }
        return new Instance(classification);
    }
    
    /**
     * @see func.Classifier#classDistribution(shared.Instance)
     */
    public Distribution distributionFor(Instance data) {
        Instance v = value(data);
        double[] p = new double[classRange];
        p[v.getDiscrete()] = 1;
        return new DiscreteDistribution(p);
    }
    
    /**
     * Get the size of the ensemble
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the size of the ensemble
     */
    public void setSize(int i) {
        size = i;
    }

    /**
     * Get the classifiers
     * @return the classfiers
     */
    public FunctionApproximater[] getClassifiers() {
        return classifiers;
    }

    /**
     * Get the weights of the classifiers
     * @return the weights of classifiers
     */
    public double[] getWeights() {
        return weights;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String ret = "";
        for (int i = 0; i < classifiers.length && classifiers[i] != null; i++) {
            ret += "weight " + weights[i] + "\n";
            ret += classifiers[i] + "\n\n";
        }
        return ret;
    }


}
