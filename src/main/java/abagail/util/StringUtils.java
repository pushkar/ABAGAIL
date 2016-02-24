package abagail.util;

/**
 * Useful utilities, mostly dealing with Strings.
 */
public class StringUtils {

    /**
     * Outputs a formatted string to stdout.
     *
     * @param fmt    format string
     * @param params positional parameters to be inserted
     * @see String#format
     */
    public static void print(String fmt, Object... params) {
        System.out.println(String.format(fmt, params));
    }

}
