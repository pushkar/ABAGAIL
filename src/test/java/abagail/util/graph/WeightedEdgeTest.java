package abagail.util.graph;

import abagail.util.StringUtils;
import org.junit.Test;

import static abagail.util.StringUtils.print;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link WeightedEdgeTest}.
 */
public class WeightedEdgeTest {

    private static final String E_BAD_COMP = "bad comparison";
    private static final double TOLERANCE = 1e-6;

    private WeightedEdge wedge;
    private WeightedEdge bigger;
    private WeightedEdge smaller;

    @Test
    public void basic() {
        wedge = new WeightedEdge(1.23);
        // FIXME: NPE on toString() because nodes not set
//        print("weighted edge: %s", wedge);

        assertEquals("wrong weight", 1.23, wedge.getWeight(), TOLERANCE);
        wedge.setWeight(4.56);
        assertEquals("wrong weight", 4.56, wedge.getWeight(), TOLERANCE);
    }

    @Test
    public void comparable() {
        wedge = new WeightedEdge(1.0);
        smaller = new WeightedEdge(0.5);
        bigger = new WeightedEdge(2.0);

        assertTrue(E_BAD_COMP, wedge.compareTo(smaller) > 0);
        assertTrue(E_BAD_COMP, wedge.compareTo(bigger) < 0);
    }


    // TODO: many more unit tests to get better coverage
}
