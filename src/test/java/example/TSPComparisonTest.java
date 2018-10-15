package example;

import opt.test.TravelingSalesmanTest;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TSPComparisonTest {
    private static final String comparisonTSPFilePrefix = "output/compTSP_";
    private static int[] iterations = new int[]{200000, 200000, 1000, 1000};

    @Test
    public void compareTsp() throws IOException {
        callTSP(iterations);
    }

    private void callTSP(int[] iterations) throws IOException {
        int cnt = 100;
        BufferedWriter writer = new BufferedWriter(new FileWriter(comparisonTSPFilePrefix + ".csv"));
        for (int i = 0; i < cnt; i++) {
            writer.write(TravelingSalesmanTest.run(iterations,i+1));
        }
        writer.close();
    }

}
