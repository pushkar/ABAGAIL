package abagail.util.graph;

import org.junit.Test;

import static abagail.util.StringUtils.print;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DfsTree}.
 */
public class DfsTreeTest {

    private static final String E_SIZE = "unexpected size";
    private static final String E_WR_NODE = "wrong node";

    private Graph graph;
    private Graph dfs;
    private GraphTransformation transform;

    /*
        NOTE:
        Graph construction seems a little awkward, suggesting that the
        APIs for Node, Edge, Graph should be revisited.
     */

    private Graph createSimpleGraph() {
        /*
            n0 -> n1 -> n2
         */
        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();

        n0.connect(n1, new Edge());
        n1.connect(n2, new Edge());

        Graph graph = new Graph();
        graph.addNode(n0);
        graph.addNode(n1);
        graph.addNode(n2);
        return graph;
    }

    private Graph createComplexGraph() {
        /*
             n0 -> n1 -> n2
             |     |
             v     v
             n3 -> n4 -> n5
             |
             v
             n6
         */
        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();
        Node n3 = new Node();
        Node n4 = new Node();
        Node n5 = new Node();
        Node n6 = new Node();

        n0.connect(n1, new Edge());
        n0.connect(n3, new Edge());
        n1.connect(n2, new Edge());
        n1.connect(n4, new Edge());
        n3.connect(n4, new Edge());
        n3.connect(n6, new Edge());
        n4.connect(n5, new Edge());

        Graph graph = new Graph();
        graph.addNode(n0);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);
        graph.addNode(n5);
        graph.addNode(n6);

        return graph;
    }

    @Test
    public void simpleGraph() {
        graph = createSimpleGraph();
        print("simple graph: %n%s", graph);

        transform = new DfsTree();

        dfs = transform.transform(graph);
        print("DFS: %n%s", dfs);
        print("orig graph: %n%s", graph);
        // NOTE: original graph has been pruned, in place (not a copy)

        // TODO: need a better way of creating these assertions.
        Node node0 = graph.getNode(0);
        Node node1 = graph.getNode(1);
        Node node2 = graph.getNode(2);

        assertEquals(E_SIZE, 1, node0.getEdgeCount());
        assertEquals(E_SIZE, 1, node1.getEdgeCount());
        assertEquals(E_SIZE, 0, node2.getEdgeCount());

        Edge edge01 = node0.getEdge(0);
        assertEquals(E_WR_NODE, node0, edge01.getA());
        assertEquals(E_WR_NODE, node1, edge01.getB());

        Edge edge12 = node1.getEdge(0);
        assertEquals(E_WR_NODE, node1, edge12.getA());
        assertEquals(E_WR_NODE, node2, edge12.getB());
    }

    @Test
    public void complexGraph() {
        graph = createComplexGraph();
        print("complex graph: %n%s", graph);

        transform = new DfsTree();

        dfs = transform.transform(graph);
        print("DFS: %n%s", dfs);
        print("orig graph: %n%s", graph);
        // NOTE: original graph has been pruned, in place (not a copy)

        // TODO: need to assert the expected form of the DFS graph
    }

    /*
     NOTES:
        DfsTree assumes that nodes in the graph will be indexed 0 .. n-1
        So why do we have the option of setting a node label?
        Should the graph enforce this labeling scheme?
     */

    // TODO: many more unit tests for better coverage
}
