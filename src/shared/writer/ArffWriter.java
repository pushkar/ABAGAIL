package shared.writer;

import shared.AttributeType;
import shared.DataSet;
import shared.Instance;
import util.linalg.Vector;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Writes a Dataset to an arff file.
 *
 * @author Joshua Wang
 * @date 2017-29-03
 *
 */
public class ArffWriter implements Writer {

    private String fileName;
    private DataSet dataSet;
    private FileWriter fileWriter;

    public ArffWriter(String fileName, DataSet dataSet) {
        this.fileName = fileName;
        this.dataSet = dataSet;
    }

    @Override
    public void close() throws IOException {
        this.fileWriter.close();
    }

    @Override
    public void open() throws IOException {
        this.fileWriter = new FileWriter(fileName);
        int i = 0;
        for (AttributeType attrType : dataSet.getDescription().getAttributeTypes()) {
            this.fileWriter.append("@attribute A" + i + " numeric\n");
            i += 1;
        }
        this.fileWriter.append("@attribute Class numeric\n");
        this.fileWriter.append("\n@data\n");
        for (Instance inst : dataSet.getInstances()) {
            List<String> dataList = new LinkedList<>();
            Vector dataVector = inst.getData();
            for (int j = 0; j < dataVector.size(); j++) {
                dataList.add("" + dataVector.get(j));
            }
            dataList.add("" + inst.getLabel().getData().get(0));
            writeRow(dataList);
        }
    }

    /**
     * @param toWrite
     * @throws IOException
     */
    private void writeRow(List<String> toWrite) throws IOException {
        boolean addComma = false;
        for (String field : toWrite) {
            if (addComma) {
                this.fileWriter.append(",");
            }
            this.fileWriter.append(field);
            addComma = true;
        }
        this.fileWriter.append('\n');
    }

    @Override
    public void write(String str) throws IOException {
        return;
    }

    @Override
    public void nextRecord() throws IOException {
        return;
    }
}
