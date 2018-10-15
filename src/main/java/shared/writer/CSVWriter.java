package shared.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Write arbitrary data to a CSV file.  This is used to write results out,
 * to be consumed by another program (GNUPlot, etc).
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-07
 *
 */
public class CSVWriter implements Writer {

    private String fileName;
    private List<String> fields;
    private List<String> buffer;
    private FileWriter fileWriter;

    public CSVWriter(String fileName, String[] fields) {
        this.fileName = fileName;
        this.fields   = Arrays.asList(fields);
        this.buffer   = new ArrayList<String>();
    }

    @Override
    public void close() throws IOException {
        this.fileWriter.close();
    }

    @Override
    public void open() throws IOException {
        this.fileWriter = new FileWriter(fileName);
        writeRow(this.fields);
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
        this.buffer.add(str);
    }

    @Override
    public void nextRecord() throws IOException {
        writeRow(buffer);
        //clear the buffer for the next record
        buffer.clear();
    }
}
