package shared.filt;

import shared.DataSet;
import shared.Instance;

import java.util.ArrayList;
import java.util.Random;

/**
 * A filter that supports k-fold splitting of a dataset for cross validation
 * @author Daniel Cohen dcohen@gatech.edu
 * @version 1.0
 */

public class kFoldSplitFilter implements DataSetFilter {
    private int foldCount;
    private ArrayList<DataSet> folds;

    public kFoldSplitFilter(int foldCount) {
        this.foldCount = foldCount;
        this.folds = new ArrayList<>();
    }

    public void filter(DataSet data) {
        int foldSize = data.size() / foldCount;
        Random rand = new Random();

        for (int currentFold = 0; currentFold < foldCount; currentFold++) {
            DataSet currentSet = new DataSet(new Instance[foldSize], data.getDescription());
            int i = 0;
            while (i < foldSize) {
                int position = rand.nextInt(data.size());
                Instance instance = data.get(position);
                if (instance != null && instance.getData() != null) {
                    currentSet.set(i, instance);
                    data.set(position, null);
                    i++;
                }
            }
            this.folds.add(currentSet);
        }
    }

    public ArrayList<DataSet> getValidationFolds(DataSet currentFold) {
        ArrayList<DataSet> result = new ArrayList<>(this.folds);
        result.remove(currentFold);
        return result;
    }

    public ArrayList<DataSet> getFolds() {
        return this.folds;
    }

    public int getFoldCount() {
        return this.foldCount;
    }
}
    
