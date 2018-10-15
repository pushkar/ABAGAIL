package util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.Instance;

import java.util.List;

public class InstanceUtilTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceUtilTest.class);
    private String eyeFile = "C:\\Users\\AF55267\\Documents\\personal\\ML\\homework\\hw-2\\abagail2\\data\\eye_state" +
            ".csv";

    @Test
    public void loadInstances() {
        Instance[] instances = InstanceUtil.loadInstances("data/foo.csv", 15, true, 14);
        List<Instance[]> testTrainSplit = InstanceUtil.testTrainSplit(instances, 5);
        LOGGER.info("Training ==>{}", testTrainSplit.get(0).length);
        LOGGER.info("Testing ==>{}", testTrainSplit.get(1).length);
    }
}