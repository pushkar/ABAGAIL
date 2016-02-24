package abagail.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link TimeUtil}.
 */
public class TimeUtilTest {

    private String result;


    private void print(String fmt, Object... params) {
        System.out.println(String.format(fmt, params));
    }


    @Test
    public void zero() {
        result = TimeUtil.formatTime(0);
        print("zero: %s", result);
        assertEquals("not zero", "00:00", result);
    }

    @Test
    public void stillZero() {
        result = TimeUtil.formatTime(999);
        print("still zero: %s", result);
        assertEquals("not zero", "00:00", result);
    }

    @Test
    public void threeSeconds() {
        result = TimeUtil.formatTime(3456);
        print("three seconds: %s", result);
        assertEquals("not three", "00:03", result);
    }

    @Test
    public void oneMinSevenSecs() {
        result = TimeUtil.formatTime(67890);
        print("1m7s: %s", result);
        assertEquals("not 1m7s", "01:07", result);
    }

    @Test
    public void oneHourTwoMinsThreeSecs() {
        result = TimeUtil.formatTime(3723000);
        print("1h2m3s: %s", result);
        assertEquals("not 62m3s", "62:03", result);
    }

}
