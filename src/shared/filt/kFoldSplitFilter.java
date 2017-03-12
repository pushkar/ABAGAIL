package shared.filt;

import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * A filter that supports k-fold splitting of a dataset for cross validation
 * @author Daniel Cohen dcohen@gatech.edu, Joshua Wang joshuawang@gatech.edu
 * @version 2.0
 */

public class kFoldSplitFilter implements DataSetFilter {
    private int foldCount;
    private ArrayList<DataSet> folds;
    private DataSet[][] validationPairs;
    private boolean filtered;

    public kFoldSplitFilter(int foldCount) {
        this.foldCount = Math.max(foldCount, 2);
        this.folds = new ArrayList<>();
        this.validationPairs = new DataSet[this.foldCount][2];
        filtered = false;
    }

    public void filter(DataSet data) {
        LinkedList<Integer> unusedIndices = new LinkedList<>();
        for (int i = 0 ; i < data.size(); i++) {
            unusedIndices.add(i);
        }

        int foldSize = data.size() / foldCount;
        Random rand = new Random();

        for (int currentFold = 0; currentFold < foldCount; currentFold++) {
            DataSet currentSet = new DataSet(new Instance[foldSize], data.getDescription());
            for (int i = 0; i < foldSize; i++){
                int randInd = rand.nextInt(unusedIndices.size());
                int position = unusedIndices.remove(randInd);
                Instance instance = data.get(position);
                currentSet.set(i, instance);
            }
            this.folds.add(currentSet);
        }
        filtered = true;
        buildValidationPairs();
    }

    public ArrayList<DataSet> getValidationFolds(DataSet currentFold) {
        ArrayList<DataSet> result = new ArrayList<>(this.folds);
        result.remove(currentFold);
        return result;
    }

    public ArrayList<DataSet> getFolds() {
        return this.folds;
    }

    public DataSet[] getValidationPair(int validationIndex) {
        if (filtered) {
            return validationPairs[validationIndex];
        }
        return new DataSet[] {null, null};
    }

    public int getFoldCount() {
        return this.foldCount;
    }

    private void buildValidationPairs() {
        if (filtered) {
            int foldSize = folds.get(0).size();
            DataSetDescription description = folds.get(0).getDescription();
            int[] lastPos = new int[foldCount];

            for (int i = 0; i < foldCount; i++) {
                validationPairs[i] = new DataSet[] {
                        new DataSet(new Instance[foldSize * (foldCount - 1)], description),
                        new DataSet(new Instance[foldSize], description)
                };
            }

            for (int i = 0; i < foldCount; i++) {
                DataSet fold = folds.get(i);
                for (int n = 0; n < foldSize; n++) {
                    Instance inst = fold.get(n);

                    // This ith fold is the validation fold for the ith pair
                    validationPairs[i][1].set(n, inst);

                    // Now consider every other pair as training folds
                    for (int j = 0; j < foldCount; j++) {
                        if (j != i) {
                            validationPairs[j][0].set(lastPos[j]++, inst);
                        }
                    }
                }
            }
        }
    }
}
