package exp;

import static abagail.util.TimeUtil.formatTime;

/**
 * A skeleton main class for your experiment.
 */
public class SampleExperiment {

    // TODO: move this method to a utility class
    private static void print(String fmt, Object... params) {
        System.out.println(String.format(fmt, params));
    }

    private static void printArgs(String[] args) {
        if (args.length == 0) {
            print("  (no command line args)");
        } else {
            int i = 1;
            for (String arg: args) {
                print("  arg %d: \"%s\"", i++, arg);
            }
        }
    }

    /**
     * Main entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        print("Hello");
        printArgs(args);

        print("Do you have a minute? [%s]", formatTime(60_000));

        print("All Done!");
    }
}
