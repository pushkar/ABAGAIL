package abagail.util.graph;

import org.junit.Test;

import static abagail.util.StringUtils.print;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Edge}.
 */
public class EdgeTest {

    private static final String E_NOT_NULL = "not null";
    private static final String E_WR_NODE = "wrong node";
    private static final String E_WR_OTHER = "wrong other node";

    private static final Node NODE_1 = new Node(1);
    private static final Node NODE_2 = new Node(2);

    private Edge edge;

    @Test
    public void basic() {
        edge = new Edge();
        assertNull(E_NOT_NULL, edge.getA());
        assertNull(E_NOT_NULL, edge.getB());
        // FIXME: NPE when either node is NOT set.
//        print("edge: %s", edge);

        edge.setA(NODE_1);
        edge.setB(NODE_2);

        print("edge: %s", edge);
        assertEquals(E_WR_NODE, NODE_1, edge.getA());
        assertEquals(E_WR_NODE, NODE_2, edge.getB());

        assertEquals(E_WR_OTHER, NODE_2, edge.getOther(NODE_1));
        assertEquals(E_WR_OTHER, NODE_1, edge.getOther(NODE_2));
        // TODO: what should the result of edge.getOther(NODE_3) be? null?
    }

    // TODO: many more unit tests to get better coverage
}
