package abagail.util.graph;

import org.junit.Test;

import static abagail.util.StringUtils.print;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link TreeTest}.
 */
public class TreeTest {

    private static final Node NODE = new Node(1);

    private Tree tree;

    @Test
    public void basic() {
        tree = new Tree();
        // FIXME: same toString() as Graph? How can we differentiate?
        print("tree: >%s<", tree);

        assertNull("not null", tree.getRoot());

        tree.setRoot(NODE);
        print("tree: >%s<", tree);
        assertEquals("wrong root", NODE, tree.getRoot());

        // FIXME: shouldn't setting a node as root add that node to the graph?
//        assertEquals("wrong node count", 1, tree.getNodeCount());
    }

    @Test
    public void treeWithRoot() {
        tree = new Tree(NODE);
        // FIXME: can't see the root node in the toString()
        print("tree: >%s<", tree);

        assertEquals("wrong root", NODE, tree.getRoot());

        // FIXME: shouldn't setting a node as root add that node to the graph?
//        assertEquals("wrong node count", 1, tree.getNodeCount());
    }

    // TODO: many more unit tests to get better coverage
}
