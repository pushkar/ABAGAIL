package shared;


/**
 * The standard euclidean distance measure
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class EuclideanDistance extends AbstractDistanceMeasure {

    /**
     * @see memory.DistanceMeasure#distanceSquared(shared.Instance, shared.Instance)
     */
    public double value(Instance va, Instance vb) {
        double sum = 0;
        for (int i = 0; i < va.size(); i++) {
            sum += (va.getContinuous(i) - vb.getContinuous(i)) 
                 * (va.getContinuous(i) - vb.getContinuous(i));
        }
        return sum;
    }

}
