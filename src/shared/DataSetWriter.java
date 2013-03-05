package shared;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A class for writing data sets
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DataSetWriter {
    /**
     * The dat set
     */
    private DataSet set;
    
    /**
     * The file name
     */
    private String filename;

    /**
     * Make a new data set writer
     * @param set the data set to writer
     */
    public DataSetWriter(DataSet set, String filename) {
        this.set = set;
        this.filename = filename;
    }
    
    /**
     * Write the file out
     * @throws IOException when something goes bad
     */
    public void write() throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(filename));
        for (int i = 0; i < set.size(); i++) {
            Instance data = set.get(i);
            while (data != null) {
                for (int j = 0; j < data.size(); j++) {
                    pw.print(data.getContinuous(j));
                    if (j + 1 < data.size() || data.getLabel() != null) {
                        pw.print(", ");
                    }
                }
                data = data.getLabel();
            }
            pw.println();
        }
        pw.close();
    }
}
