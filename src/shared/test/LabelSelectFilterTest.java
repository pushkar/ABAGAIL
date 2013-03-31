package shared.test;

import java.io.File;

import shared.DataSet;
import shared.DataSetDescription;
import shared.filt.ContinuousToDiscreteFilter;
import shared.filt.LabelSelectFilter;
import shared.filt.LabelSplitFilter;
import shared.reader.ArffDataSetReader;
import shared.reader.DataSetLabelBinarySeperator;
import shared.reader.DataSetReader;

public class LabelSelectFilterTest {
    /**
     * The test main
     * @param args ignored parameters
     */
    public static void main(String[] args) throws Exception {
        DataSetReader dsr = new ArffDataSetReader(new File("").getAbsolutePath() + "/src/shared/test/abalone.arff");
        // read in the raw data
        DataSet ds = dsr.read();
        // split out the label
        LabelSelectFilter lsf = new LabelSelectFilter(1);
        lsf.filter(ds);
        ContinuousToDiscreteFilter ctdf = new ContinuousToDiscreteFilter(10);
        ctdf.filter(ds);
        System.out.println(ds);
        System.out.println(new DataSetDescription(ds));
    }
}
