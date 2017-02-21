package abagail.util.graph;

/**
 * Represents a weighted edge.
 *
 * @author Andrew Guillory - gtg008g@mail.gatech.edu
 */
public class WeightedEdge extends Edge implements Comparable<WeightedEdge> {

    /**
     * The weight of the edge.
     */
    private double weight;

    /**
     * Creates a new weighted edge.
     *
     * @param weight the weight of the edge
     */
    public WeightedEdge(double weight) {
        this.weight = weight;
    }

    /**
     * Returns the weight of this edge.
     *
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight of this edge.
     *
     * @param d the new weight
     */
    public void setWeight(double d) {
        weight = d;
    }

    @Override
    public int compareTo(WeightedEdge e) {
        if (getWeight() > e.getWeight()) {
            return 1;
        } else if (getWeight() < e.getWeight()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " x " + weight;
    }

}
