package shared;

/**
 * An attribute type specifies what type an attribute
 * within a data set is
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class AttributeType {
    /**
     * The binary type
     */
    public static final AttributeType BINARY = new AttributeType(1);   
    /**
     * The integer / discrete type
     */
    public static final AttributeType DISCRETE = new AttributeType(2);
    /**
     * The continuous type
     */
    public static final AttributeType CONTINUOUS = new AttributeType(3);
    
    /**
     * The type of the attribute
     */
    private int type;
    
    /**
     * Make a new attribute type
     * @param t the type of the attribute
     */
    private AttributeType(int t) {
        type = t;
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        return ((AttributeType) o).type == type;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (this == BINARY) {
            return "BINARY";
        } else if (this == DISCRETE) {
            return "DISCRETE";
        } else if (this == CONTINUOUS) {
            return "CONTINUOUS";
        } else {
            return "UNKNOWN";
        }
    }

}
