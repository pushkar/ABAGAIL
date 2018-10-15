package example;

import opt.test.FourPeaksTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FourPeaksComparisonTest {
    private static final String comparison4PeaksFile = "output/compFourPeaks.csv";
    private static int[] iterations10 = new int[]{200000, 200000, 5500, 5500};
    private static int[] iterations9 = new int[]{140000, 140000, 5000, 5000};
    private static int[] iterations8 = new int[]{120000, 120000, 4500, 4500};
    private static int[] iterations7 = new int[]{100000, 100000, 4000, 4000};
    private static int[] iterations6 = new int[]{80000, 80000, 3500, 3500};
    private static int[] iterations5 = new int[]{40000, 40000, 3000, 3000};
    private static int[] iterations4 = new int[]{20000, 20000, 2500, 2500};
    private static int[] iterations3 = new int[]{15000, 15000, 2000, 2000};
    private static int[] iterations2 = new int[]{10000, 10000, 1500, 1500};
    private static int[] iterations1 = new int[]{5000, 5000, 1000, 1000};
    private static List<int[]> iterations = new ArrayList<>();

    @BeforeClass
    public static void init() {
        iterations.add(iterations1);
        iterations.add(iterations2);
        iterations.add(iterations3);
        iterations.add(iterations4);
        iterations.add(iterations5);
        iterations.add(iterations6);
        iterations.add(iterations7);
        iterations.add(iterations8);
        iterations.add(iterations9);
        iterations.add(iterations10);
    }

    @Test
    public void call4Peaks() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(comparison4PeaksFile));
        int cnt = 1;
        for (int[] iter : iterations) {
            writer.write(FourPeaksTest.run(iter));
            System.out.println(cnt + ": ----------------------------------------\n");
            cnt++;
        }
        writer.close();
    }

}
