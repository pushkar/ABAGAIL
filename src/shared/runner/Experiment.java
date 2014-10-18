package shared.runner;

import shared.DataSet;
import shared.reader.DataSetReader;

/**
 * Created by zooky on 10/11/14.
 */
public interface Experiment {
    /**
     * Set type of DataSetReader according to type of datafile
     */
    public void setDataSetReader();
    /**
     *
     * @return A DataSet
     */
    public DataSet getDataSet();





}
