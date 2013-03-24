package shared;

/**
 * An interface for things that can be copied
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface Copyable {
    /**
     * Make a copy of this
     * @return the copy
     */
    public Copyable copy();

}
