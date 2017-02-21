package abagail.util;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link AbagailArrays}.
 */
public class AbagailArraysTest {

    private static final int SIZE = 100;
    private static final double TOLERANCE = 1e-6;

    private static double[] SMALL_ARRAY = {0.1, 1.0};
    private static double[] NUMBERS;

    @BeforeClass
    public static void initNumbers() {
        NUMBERS = new double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            NUMBERS[i] = i;
        }
    }

    @Test
    public void randomizedSelect() {
        assertEquals("not 11th smallest", 10,
                AbagailArrays.randomizedSelect(NUMBERS, 11), TOLERANCE);
        assertEquals("not 32nd smallest", 31,
                AbagailArrays.randomizedSelect(NUMBERS, 32), TOLERANCE);
    }

    @Test
    public void searchBig() {
        assertEquals("wrong index", 21, AbagailArrays.search(NUMBERS, 21));
    }

    @Test
    public void searchSmall() {
        assertEquals("wrong index", 1, AbagailArrays.search(SMALL_ARRAY, 0.2));
    }

    // TODO: more unit tests to get better coverage...
}
