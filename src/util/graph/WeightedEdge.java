package util.graph;

/**
 * A class representing a weighted edge
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class WeightedEdge extends Edge implements Comparable {
    
    /**
     * The weight of the edge
     */
    private double weight;
    
    /**
     * Make a new weighted edge
     * @param weight the weight of the edge
     */
    public WeightedEdge(double weight) {
        this.weight = weight;
    }

    /**
     * Get the weight
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Set the weight
     * @param d the new weight
     */
    public void setWeight(double d) {
        weight = d;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        WeightedEdge e = (WeightedEdge) o;
        if (getWeight() > e.getWeight()) {
            return 1;
        } else if (getWeight() < e.getWeight()) {
            return -1;
        } else {
            return 0;
        }
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return super.toString() + " x " + weight;
    }

}
