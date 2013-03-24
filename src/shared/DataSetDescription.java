package shared;

import java.io.Serializable;
import java.util.Arrays;

import util.ABAGAILArrays;
import util.linalg.DenseVector;
import util.linalg.Vector;

/**
 * A data set description contains information
 * about the attributes of a data set
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DataSetDescription implements Serializable {
    
    /**
     * The description of the label type
     */
    private DataSetDescription labelDescription;
    
    /**
     * The types of the attributes
     */
    private AttributeType[] types;
    
    /**
     * The maximum value
     */
    private Vector max;
    
    /**
     * The minimum value instance
     */
    private Vector min;
    
    /**
     * Make a new data set description
     * @param types the types of the data
     * @param min the minimum value
     * @param max the maximum value
     * @param labelDescription the description of the label
     */
    public DataSetDescription(AttributeType[] types, Vector min, Vector max,
            DataSetDescription labelDescription) {
        this.types = types;
        this.max = max;
        this.min = min;
        this.labelDescription = labelDescription;             
    }
    
    /**
     * Make a new data set description
     * @param types the types of the data
     * @param max the maximum value
     * @param labelDescription the description of the label
     */
    public DataSetDescription(AttributeType[] types, Vector max,
            DataSetDescription labelDescription) {
        this(types, null, max, labelDescription);
        min = new DenseVector(max.size());
    }
    
    /**
     * Make a new data set description
     * @param types the types of the data
     * @param max the maximum value
     */
    public DataSetDescription(AttributeType[] types, Vector max) {
        this(types, null, max, null);
    }
    
    /**
     * Make a new data set description
     * @param types the types of the data
     * @param set the data set
     * @param min the minimum value
     * @param max the maximum value
     * @param labelDescription the description of the label
     */
    public DataSetDescription(AttributeType[] types, Vector min, Vector max) {
         this(types, min, max, null);       
    }
    
    /**
     * Make a new empty data set description
     */
    public DataSetDescription() {}
    
    /**
     * Make a new data set description induced from a data set
     * @param dataSet the data set to induce from
     */
    public DataSetDescription(DataSet dataSet) {
        induceFrom(dataSet);
    }

    
    /**
     * Get the discrete max
     * @param i the attribute index
     * @return the max of the attribute
     */
    public int getDiscreteRange(int i) {
        return (int) max.get(i) + 1;
    }
    
    /**
     * Get the discrete range
     * @return the range
     */
    public int getDiscreteRange() {
        return getDiscreteRange(0);
    }
    

    /**
     * Get the continuous range
     * @param i the attribute index
     * @return the range of the attribute
     */
    public double getRange(int i) {
        return getMax(i) - getMin(i);
    }
    
    /**
     * Get the continuous range
     * @return the range
     */
    public double getRange() {
        return getRange(0);
    }
    
    /**
     * Get the continuous max
     * @param i the attribute index
     * @return the max of the attribute
     */
    public double getMax(int i) {
        return max.get(i);
    }
    
    /**
     * Get the continuous max
     * @return the max
     */
    public double getMax() {
        return getMax(0);
    }

    /**
     * Get the continuous max
     * @param i the attribute index
     * @return the max of the attribute
     */
    public double getMin(int i) {
        return min.get(i);
    }
    
    /**
     * Get the continuous max
     * @return the max
     */
    public double getMin() {
        return getMin(0);
    }

    /**
     * Get the description of the label
     * @return the description of the label
     */
    public DataSetDescription getLabelDescription() {
        return labelDescription;
    }

    /**
     * Get the maximum value
     * @return the maximum value
     */
    public Vector getMaxVector() {
        return max;
    }

    /**
     * Get the types of the data
     * @return the types
     */
    public AttributeType[] getAttributeTypes() {
        return types;
    }
    
    /**
     * Get the attribute count
     * @return the count
     */
    public int getAttributeCount() {
        return types.length;
    }

    /**
     * Get the min of the data
     * @return the min
     */
    public Vector getMinVector() {
        return min;
    }

    /**
     * Set the label description
     * @param description the new description
     */
    public void setLabelDescription(DataSetDescription description) {
        labelDescription = description;
    }

    /**
     * Set the max
     * @param instance the new max
     */
    public void setMaxVector(Vector instance) {
        max = instance;
    }

    /**
     * Set the min
     * @param instance the new min
     */
    public void setMinVector(Vector instance) {
        min = instance;
    }

    /**
     * Set the types
     * @param types the new types
     */
    public void setAttributeTypes(AttributeType[] types) {
        this.types = types;
    }
    
    /**
     * Induce from the given data set
     * @param data the data set
     */
    public void induceFrom(DataSet data) {
        boolean hasLabels = false;
        int i = 0;
        while (data.get(i) == null) {
            i++;
        }
        if (i >= data.size()) {
            return;
        }
        if (max == null) {
            max = (Vector) data.get(i).getData().copy();
        }
        if (min == null) {
            min = (Vector) data.get(i).getData().copy();
        }
        if (types == null) {
            types = new AttributeType[data.get(i).size()];
            Arrays.fill(types, AttributeType.BINARY);
        }
        for (; i < data.size(); i++) {
            Instance cur = data.get(i);
            if (cur == null) {
                continue;
            }
            hasLabels = hasLabels || cur.getLabel() != null;
            max.maxEquals(cur.getData());
            min.minEquals(cur.getData());
            for (int j = 0; j < types.length; j++) {
                if (types[j] == AttributeType.BINARY
                        && cur.getContinuous(j) != 1 && cur.getContinuous(j) != 0) {
                            
                    types[j] = AttributeType.DISCRETE;
                }
                if (types[j] == AttributeType.DISCRETE
                        && cur.getDiscrete(j) != cur.getContinuous(j)) {
                    types[j] = AttributeType.CONTINUOUS;      
                }
            }
        }
        if (hasLabels) {
            if (labelDescription == null) {
                labelDescription = new DataSetDescription();
            }
            labelDescription.induceFrom(data.getLabelDataSet());
        }
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "Types : " + ABAGAILArrays.toString(types);
        result += "\nMax : " + max;
        result += "\nMin : " + min;
        if (labelDescription != null) {
            result += "\nLabel Description:\n" + labelDescription;
        }
        return result;
    }

}
