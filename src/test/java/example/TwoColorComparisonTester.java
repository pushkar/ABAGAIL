package example;

import opt.test.TwoColorsTest;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TwoColorComparisonTester {
    private static final String comparison2cFilePrefix = "output/comp2C_";

    @Test
    public void compare2C100() throws IOException {
        callTwoC(100);
    }

    @Test
    public void compare2C1000() throws IOException {
        callTwoC(1000);
    }

    private void callTwoC(int iterations) throws IOException {
        int cnt = 100;
        BufferedWriter writer = new BufferedWriter(new FileWriter(comparison2cFilePrefix + iterations + ".csv"));
        for (int i = 0; i < cnt; i++) {
            writer.write(TwoColorsTest.run(iterations));
        }
        writer.close();
    }
}
