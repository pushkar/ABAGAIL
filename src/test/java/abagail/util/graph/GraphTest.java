package abagail.util.graph;

import org.junit.Test;

import static abagail.util.StringUtils.print;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GraphTest}.
 */
public class GraphTest {

    private static final String E_WR_NC = "wrong node count";
    private static final String E_WR_L = "wrong label";
    private static final String E_WR_SIZE = "wrong size";

    private Graph graph;
    private Node node;

    @Test
    public void basic() {
        graph = new Graph();
        // FIXME: empty graph is blank string?! We should show something!
        print("graph: >%s<", graph);

        assertEquals(E_WR_NC, 0, graph.getNodeCount());
        // FIXME: IndexOutOfBoundsException
//        assertNull(E_NOT_NULL, graph.getNode(0));

        assertEquals(E_WR_SIZE, 0, graph.getNodes().size());
        assertEquals(E_WR_SIZE, 0, graph.getEdges().size());
    }

    @Test
    public void addSomeNodes() {
        graph = new Graph();
        graph.addNode(new Node());
        graph.addNode(new Node());
        graph.addNode(new Node());

        assertEquals(E_WR_SIZE, 3, graph.getNodeCount());
        for (int i = 0; i < 3; i++) {
            node = graph.getNode(i);
            assertEquals(E_WR_L, i, node.getLabel());
        }
    }

    // TODO: many more unit tests to get better coverage
}
