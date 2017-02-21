package abagail.util.graph;

import org.junit.Test;

import static abagail.util.StringUtils.print;

/**
 * Unit tests for {@link KruskalsMstTest}.
 */
public class KruskalsMstTest {

    private static final double ONE = 1.0;
    /*
        NOTE:
        Graph construction seems a little awkward, suggesting that the
        APIs for Node, Edge, Graph should be revisited.

        Also, we had to replicate code (see DfsTreeTest) because we needed
        weighted edges, rather than plain edges. Perhaps there is a better
        pattern to use to reduce duplication?
     */

    private Graph createSimpleGraph() {
        /*
            n0 -> n1 -> n2
         */
        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();

        n0.connect(n1, new WeightedEdge(ONE));
        n1.connect(n2, new WeightedEdge(ONE));

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

        n0.connect(n1, new WeightedEdge(ONE));
        n0.connect(n3, new WeightedEdge(ONE));
        n1.connect(n2, new WeightedEdge(ONE));
        n1.connect(n4, new WeightedEdge(ONE));
        n3.connect(n4, new WeightedEdge(ONE));
        n3.connect(n6, new WeightedEdge(ONE));
        n4.connect(n5, new WeightedEdge(ONE));

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
    public void mstSimple() {
        Graph g = createSimpleGraph();
        print("Original graph:%n%s", g);

        Graph mst = new KruskalsMst().transform(g);
        print("Minimum Spanning Tree:%n%s", mst);

        // TODO: add assertions to confirm that the result is correct
    }

    @Test
    public void mstComplex() {
        Graph g = createComplexGraph();
        print("Original graph:%n%s", g);

        Graph mst = new KruskalsMst().transform(g);
        print("Minimum Spanning Tree:%n%s", mst);

        // TODO: add assertions to confirm that the result is correct
    }

    // TODO: add more unit tests to increase coverage (and corner cases)
}
