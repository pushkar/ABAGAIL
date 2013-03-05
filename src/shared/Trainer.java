package shared;

import java.io.Serializable;

/**
 * An abstract trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface Trainer extends Serializable {
    /**
     * The train the whatever
     * @return the error
     */
    public abstract double train();
}
