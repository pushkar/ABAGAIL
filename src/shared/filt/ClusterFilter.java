package shared.filt;

import func.FunctionApproximater;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import util.linalg.DenseVector;
import util.linalg.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Joshua Wang
 */
public class ClusterFilter implements DataSetFilter {
    FunctionApproximater clusterer;

    public ClusterFilter(FunctionApproximater clusterer) {
        this.clusterer = clusterer;
    }

    public void filter(DataSet set) {
        // Performs 1-hot encoding based on the cluster assignments
        clusterer.estimate(set);
        HashMap<Double, Integer> clusterMap = new HashMap<>();
        int clusterID = 0;
        Instance[] instances = set.getInstances();
        double[] clusterValues = new double[instances.length];
        for (int i = 0; i < instances.length; i++) {
            double cluster = clusterer.value(instances[i]).getData().get(0);
            clusterValues[i] = cluster;
            if (!clusterMap.containsKey(cluster)) {
                clusterMap.put(cluster, clusterID++);
            }
        }
        int clusterCount = clusterMap.size();

        for (int i = 0; i < instances.length; i++) {
            double[] newAttributes = new double[clusterCount];
            double cluster = clusterValues[i];
            int cID = clusterMap.get(cluster);
            newAttributes[cID] = 1.0;

            instances[i].setData(new DenseVector(newAttributes));
        }

        set.setDescription(new DataSetDescription(set));
    }
}
