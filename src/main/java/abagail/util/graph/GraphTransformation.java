package abagail.util.graph;

/**
 * Defines a transform function on a graph.
 *
 * @author Andrew Guillory - gtg008g@mail.gatech.edu
 */
public interface GraphTransformation {

    /**
     * Transforms the given graph.
     *
     * @param g the graph to transform
     * @return the transformed graph
     */
    Graph transform(Graph g);

}
