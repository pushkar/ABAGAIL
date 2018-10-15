package example;

import org.junit.Test;

import java.io.IOException;

public class EyeStateTestTest {

    @Test
    public void testEye() throws IOException {
        new EyeStateTest("data/foo.csv");
    }

}