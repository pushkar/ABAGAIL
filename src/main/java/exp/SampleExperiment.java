package exp;

import static util.TimeUtil.formatTime;

/**
 * A skeleton main class for your experiment.
 */
public class SampleExperiment {

    // TODO: move this method to a utility class
    private static void print(String fmt, Object... params) {
        System.out.println(String.format(fmt, params));
    }

    /**
     * Main entry point.
     *
     * @param args command line arguments -- ignored
     */
    public static void main(String[] args) {
        print("Hello");
        print("Do you have a minute? [%s]", formatTime(60_000));

        // write the remainder of your code here..
        //  (and in other classes, of course)

        print("All Done!");
    }
}
