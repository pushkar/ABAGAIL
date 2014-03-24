package shared;


/**
 * The standard euclidean distance measure
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class EuclideanDistance extends DistanceMeasure {

    /**
     * @see memory.DistanceMeasure#distanceSquared(shared.Instance, shared.Instance)
     */
    public double value(Instance a, Instance b) {
        double sum = 0;
        System.out.println(a.getContinuous(0) + " jjj " + b.getContinuous(0));
        for (int i = 0; i < a.size(); i++) {
            sum += (a.getContinuous(i) - b.getContinuous(i)) 
                 * (a.getContinuous(i) - b.getContinuous(i));
        }
        return sum;
    }
}
