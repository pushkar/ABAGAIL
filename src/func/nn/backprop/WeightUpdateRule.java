package func.nn.backprop;

import java.io.Serializable;

/**
 * An  abstract class with an update rule for the weight value of a <code> BackPropagationLink </code> object used
 * to pass values between <code> BackPropagationNode </code> objects that lie within two consecutive <code> BackPropagationLayer </code>
 * objects of a <code> BackPropagationNetwork </code>.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class WeightUpdateRule implements Serializable {
    
    /**
     * Updates the given link's weight value.
     * @param link the link to update
     * @see BackPropagationLink
     */
    public abstract void update(BackPropagationLink link);

}
