package abagail.util.graph;

import org.junit.Test;

import static abagail.util.StringUtils.print;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link NodeTest}.
 */
public class NodeTest {

    private static final String WR_EC = "wrong edge count";
    private static final String WR_E = "wrong edge";
    private static final String WR_L = "wrong label";

    private Node node;
    private Edge edge;

    @Test
    public void basic() {
        node = new Node();
        print("node: %s", node);
        assertEquals(WR_EC, 0, node.getEdgeCount());
        assertEquals(WR_L, 0, node.getLabel());
        node.setLabel(5);
        assertEquals(WR_L, 5, node.getLabel());
    }

    @Test
    public void nodeWithLabel() {
        node = new Node(3);
        print("node: %s", node);
        assertEquals(WR_EC, 0, node.getEdgeCount());
        assertEquals(WR_L, 3, node.getLabel());
    }

    @Test
    public void addAnEdge() {
        node = new Node(1);
        edge = new Edge();

        node.addEdge(edge);
        // FIXME: NPE on link.toString()
//        print("node: %s", node);
        assertEquals(WR_EC, 1, node.getEdgeCount());
        assertEquals(WR_E, edge, node.getEdge(0));
        assertEquals("edge not found", true, node.getEdges().contains(edge));
    }

    // TODO: many more unit tests to get better coverage
}
