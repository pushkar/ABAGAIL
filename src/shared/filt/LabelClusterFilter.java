package shared.filt;

import func.FunctionApproximater;
import shared.DataSet;
import shared.Instance;

/**
 * @author Joshua Wang
 */
public class LabelClusterFilter implements DataSetFilter {
    private FunctionApproximater clusterer;

    public LabelClusterFilter(FunctionApproximater clusterer) {
        this.clusterer = clusterer;
    }

    public void filter(DataSet set) {
        for (Instance inst : set.getInstances()) {
            inst.setLabel(clusterer.value(inst));
        }
    }
}
