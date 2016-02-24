package abagail.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link MaxHeap}.
 */
public class MaxHeapTest {

    private static final String UX_SIZE = "unexpected size";
    private static final String UX_ITEM = "unexpected item";
    private static final String UX_KEY = "unexpected key";

    private static final double TOLERANCE = 1e-6;
    private static final int CAPACITY = 10;

    private static final String FOO = "foo";
    private static final String BAR = "bar";

    private MaxHeap maxHeap;

    @Test
    public void basic() {
        maxHeap = new MaxHeap(CAPACITY);
        assertEquals(UX_SIZE, 0, maxHeap.size());

        maxHeap.add(FOO, 3.0);
        assertEquals(UX_SIZE, 1, maxHeap.size());
        assertEquals(UX_KEY, 3.0, maxHeap.getMaxKey(), TOLERANCE);

        maxHeap.add(BAR, 4.2);
        assertEquals(UX_SIZE, 2, maxHeap.size());
        assertEquals(UX_KEY, 4.2, maxHeap.getMaxKey(), TOLERANCE);

        assertEquals(UX_ITEM, BAR, maxHeap.extractMax());
        assertEquals(UX_SIZE, 1, maxHeap.size());
    }

    // TODO: more unit tests to get better coverage...

}
