package util;

/**
 * A utility for preparing and presenting run time metrics.
 *
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-07
 */
public class TimeUtil {

    /**
     * Formats the given number of milliseconds in minutes and seconds.
     *
     * @param timeMs time in milliseconds
     * @return formatted time "mm:ss"
     */
    public static String formatTime(long timeMs) {
        long secs = timeMs / 1000;
        long min  = secs / 60;
        secs     -= min  * 60;
        return String.format("%02d:%02d", min, secs);
    }
}
