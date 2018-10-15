package util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.Instance;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InstanceUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceUtil.class);

    public static Instance[] loadInstances(String file, int numCols, boolean isHeader, int labelPos) {
        List<Instance> samples = new ArrayList<>();
        try {
            Reader in = new FileReader(file);
            Iterable<CSVRecord> records = null;
            if (isHeader) {
                records = CSVFormat.EXCEL.withHeader().parse(in);
            } else {
                records = CSVFormat.EXCEL.parse(in);
            }

            int rowCnt = 0;

            for (CSVRecord record : records) {
                if (record.size() != numCols) {
                    LOGGER.error("skipping row, since cols present:{} not equal to expected:{}", record.size(),
                            numCols);
                }
                double[] sampleAttributes = new double[numCols];
                for (int i = 0; i < numCols; i++) {
                    sampleAttributes[i] = Double.parseDouble(record.get(i));
                }

                Instance label = new Instance(Double.parseDouble(record.get(labelPos)));
                Instance sample = new Instance(sampleAttributes);
                sample.setLabel(label);
                LOGGER.debug("Row:[{}] Label value={} cols:{}", rowCnt, sample.getLabel(), sample.getData().size());
                samples.add(sample);
                rowCnt++;
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Err: Could not read file", e);
        } catch (IOException e) {
            LOGGER.error("Err: Could not parse csv");
        }
        return samples.toArray(new Instance[samples.size()]);
    }

    public static List<Instance[]> testTrainSplit(Instance[] instances, int testFraction) {
        List<Instance> instanceList = Arrays.asList(instances);
        Collections.shuffle(instanceList);
        List<Instance> testSet = new ArrayList<>(instanceList.subList(0, instanceList.size() / testFraction +
                instanceList.size() % testFraction));
        List<Instance> trainSet = new ArrayList<>(instanceList.subList(instanceList.size() / testFraction +
                instanceList.size() % testFraction, instanceList.size()));
        LOGGER.info("Train rows:{} Test rows:{}", trainSet.size(), testSet.size());
        List<Instance[]> testTrainSplit = new ArrayList<>();
        testTrainSplit.add(trainSet.toArray(new Instance[trainSet.size()]));
        testTrainSplit.add(testSet.toArray(new Instance[testSet.size()]));
        return testTrainSplit;
    }
}
