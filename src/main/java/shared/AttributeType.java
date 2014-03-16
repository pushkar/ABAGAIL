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
    private final int type;
    
    /**
     * Make a new attribute type
     * @param t the type of the attribute
     */
    private AttributeType(int t) {
        type = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AttributeType that = (AttributeType) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return type;
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
