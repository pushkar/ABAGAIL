package shared.test;

import shared.DataSet;
import shared.DataSetDescription;
import shared.filt.ContinuousToDiscreteFilter;
import shared.filt.LabelSplitFilter;
import shared.reader.CSVDataSetReader;
import shared.reader.DataSetReader;

import java.io.File;

/**
 * A data set reader
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class CSVDataSetReaderTest {
    /**
     * The test main
     * @param args ignored parameters
     */
    public static void main(String[] args) throws Exception {
        DataSetReader dsr = new CSVDataSetReader(new File("").getAbsolutePath() + "/src/shared/test/abalone.data");
        // read in the raw data
        DataSet ds = dsr.read();
        // split out the label
        LabelSplitFilter lsf = new LabelSplitFilter();
        lsf.filter(ds);
        ContinuousToDiscreteFilter ctdf = new ContinuousToDiscreteFilter(10);
        ctdf.filter(ds);
        System.out.println(ds);
        System.out.println(new DataSetDescription(ds));
    }
}
