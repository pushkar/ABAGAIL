package func.nn.backprop;

import java.io.Serializable;

/**
 * An  update rule for a back propagation link
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class WeightUpdateRule implements Serializable {
    
    /**
     * Update the given link
     * @param link the link to update
     */
    public abstract void update(BackPropagationLink link);

}
