package func;

import dist.*;
import dist.Distribution;
import dist.DiscreteDistribution;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;

/**
 * A class for construcing a ensemble of decision stumps
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class AdaBoostClassifier extends AbstractConditionalDistribution implements FunctionApproximater  {
    /**
     * The classifier class to use
     */
    private Class classifier;
    
    /**
     * The stumps themselves
     */
    private FunctionApproximater[] classifiers;
    
    /**
     * The weights for each of the stumps
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
     * Create a new decision stump ensemble
     * @param size the number of stumps
     * @param splitEvaluator the splitting strategy to use
     * @param classifier the classifier class to use
     */
    public AdaBoostClassifier(int size, Class classifier) {
        this.size = size;
        this.classifier = classifier;
    }
    
    /**
     * Create a new decision stump ensemble
     * @param size the number of stumps
     * @param splitEvaluator the splitting strategy to use
     */
    public AdaBoostClassifier(int size) {
        this(size, DecisionStumpClassifier.class);
    }
    
    /**
     * Make a new default ensemble
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
            try {
                // make a new classifier
                classifiers[i] = (FunctionApproximater) 
                    classifier.getConstructor(new Class[0]).newInstance(new Object[0]);
                classifiers[i].estimate(instances);
            } catch (Exception e) {
                throw new UnsupportedOperationException("Could not create " + classifier);
            }
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
     * Get the stump count
     * @return the stump count
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the stump count
     * @param i the stump count
     */
    public void setSize(int i) {
        size = i;
    }

    /**
     * Get the decision stumps
     * @return the stumps
     */
    public FunctionApproximater[] getClassifiers() {
        return classifiers;
    }

    /**
     * Get the weights of the stumps
     * @return the stumps
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
