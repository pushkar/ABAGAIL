package util;

/**
 * A utility for preparing and presenting run time metrics.
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-07
 */
public class TimeUtil {

    public static String formatTime(long time) {
        long secs = ((long) time) / 1000;
        long min  = secs / 60;
        secs     -= min  * 60;
        return String.format("%02d:%02d", min, secs);
    }
}
