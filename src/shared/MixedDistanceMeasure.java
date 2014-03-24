package shared;


/**
 * A distance measure that mixes
 * several distance measures
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MixedDistanceMeasure extends DistanceMeasure {
    
    /**
     * The distance measure for each attribute
     */
    private AttributeType[] types;
    
    /**
     * Make a new mixed distance measure
     * @param measures the measures to use
     */
    public MixedDistanceMeasure(AttributeType[] types) {
       this.types = types;
    }

    /**
     * @see memory.DistanceMeasure#distance(double[], double[])
     */
    public double value(Instance a, Instance b) {
        double distance = 0;
        for (int i = 0; i < a.size(); i++) {
            if (types[i] == AttributeType.CONTINUOUS) {
                distance += (a.getContinuous(i) - b.getContinuous(i)) 
                     * (a.getContinuous(i) - b.getContinuous(i));
            } else {
                if (a.getDiscrete(i) != b.getDiscrete(i)) {
                    distance += 1;
                }
            }
        }
        return distance;
    }
}

