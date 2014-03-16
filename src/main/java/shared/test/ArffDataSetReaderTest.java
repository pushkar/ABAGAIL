package shared.test;

import java.io.File;

import shared.DataSet;
import shared.DataSetDescription;
import shared.reader.ArffDataSetReader;
import shared.reader.DataSetReader;
import shared.filt.ContinuousToDiscreteFilter;
import shared.filt.LabelSplitFilter;
import shared.reader.DataSetLabelBinarySeperator;

/**
 * A data set reader
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ArffDataSetReaderTest {
    /**
     * The test main
     * @param args ignored parameters
     */
    public static void main(String[] args) throws Exception {
        DataSetReader dsr = new ArffDataSetReader(new File("").getAbsolutePath() + "/src/shared/test/abalone.arff");
        // read in the raw data
        DataSet ds = dsr.read();
        // split out the label
        LabelSplitFilter lsf = new LabelSplitFilter();
        lsf.filter(ds);
        ContinuousToDiscreteFilter ctdf = new ContinuousToDiscreteFilter(10);
        ctdf.filter(ds);
        DataSetLabelBinarySeperator.seperateLabels(ds);
        System.out.println(ds);
        System.out.println(new DataSetDescription(ds));
    }
}
