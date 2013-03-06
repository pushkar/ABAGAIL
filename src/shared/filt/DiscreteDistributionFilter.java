package shared.filt;

import dist.ConditionalDistribution;
import dist.Distribution;
import dist.DiscreteDistribution;
import shared.DataSet;
import shared.Instance;
import util.linalg.DenseVector;

/**
 * A filter that replaces data with a class distribution
 * from a classifier
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteDistributionFilter implements DataSetFilter {
    /**
     * The classifier in use
     */
    private ConditionalDistribution classifier;
    
    /**
     * Make a new classifier filter
     * @param classifier the classifier
     */
    public DiscreteDistributionFilter(ConditionalDistribution classifier) {
        this.classifier = classifier;
    }

    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            Distribution dist = classifier.distributionFor(instance);
            instance.setData(new DenseVector(
                ((DiscreteDistribution) dist).getProbabilities()));
        }
    }
    

}
