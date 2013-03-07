package util.graph;

/**
 * A graph transform is a tranformation of a graph
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface GraphTransformation {
    
    /**
     * Transform the given graph
     * @param g the graph to transform
     * @return the transformed graph
     */
    public Graph transform(Graph g);

}
