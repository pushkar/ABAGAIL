package shared;


/**
 * A distance measure that mixes
 * several distance measures
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MixedDistanceMeasure extends AbstractDistanceMeasure{
    
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
    public double value(Instance va, Instance vb) {
        double distance = 0;
        for (int i = 0; i < va.size(); i++) {
            if (types[i] == AttributeType.CONTINUOUS) {
                distance += (va.getContinuous(i) - vb.getContinuous(i)) 
                     * (va.getContinuous(i) - vb.getContinuous(i));
            } else {
                if (va.getDiscrete(i) != vb.getDiscrete(i)) {
                    distance += 1;
                }
            }
        }
        return distance;
    }

}

