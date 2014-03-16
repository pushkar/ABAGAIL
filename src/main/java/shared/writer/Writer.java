package shared.writer;

import java.io.IOException;

/**
 * This interface defines an API for a Writer object.  Writers are used to write results
 * to a file of a certain type.
 * 
 * The writer lets a caller write to a given record, or advance to the next record.
 * As an example, a CSVWriter might consider each line a record.  A user can write
 * to a line, which will create comma separated values.  The call to nextRecord
 * will then go to the next line.
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-07
 */
public interface Writer {

    /**
     * Close a writer and flush it's contents.
     * 
     * @throws IOException
     */
    public void close() throws IOException;
    
    /**
     * Open a writer for writing.
     * 
     * @throws IOException
     */
    public void open() throws IOException;
    
    /**
     * Write a datapoint to a record.
     * 
     * @param str
     * @throws IOException
     */
    public void write(String str) throws IOException;
    
    /**
     * Advance to the next record.
     * 
     * @throws IOException
     */
    public void nextRecord() throws IOException;
}
