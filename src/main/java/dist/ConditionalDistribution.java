package dist;

import shared.Instance;

/**
 * A conditional probability distribution
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface ConditionalDistribution extends Distribution {
    
    /**
     * Get the distribution for an instance
     * @param i the instance
     * @return the  distribution
     */
    public Distribution distributionFor(Instance i);

}
