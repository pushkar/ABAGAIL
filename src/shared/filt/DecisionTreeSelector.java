package shared.filt;

import func.DecisionTreeClassifier;
import func.dtree.*;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import util.linalg.DenseVector;
import util.linalg.Vector;

import java.util.*;

/**
 * @author Joshua Wang joshuawang@gatech.edu
 */
public class DecisionTreeSelector implements DataSetFilter{

    private SplitEvaluator splitEvaluator;
    private PruningCriteria pruningCriteria;

    private List<Integer> relevantAttributes;

    public DecisionTreeSelector(SplitEvaluator splitEvaluator, PruningCriteria pruningCriteria) {
        this.splitEvaluator = splitEvaluator;
        this.pruningCriteria = pruningCriteria;
    }

    public DecisionTreeSelector(SplitEvaluator splitEvaluator) {
        this(splitEvaluator, null);
    }

    public void filter(DataSet set) {
        // Create a standard decision tree
        DecisionTreeClassifier dtree = new DecisionTreeClassifier(splitEvaluator, pruningCriteria, true);
        dtree.estimate(set);

        // Extract the attributes that were split on
        relevantAttributes = new LinkedList(getIntegerAttributes(dtree.getRoot()));
        Collections.sort(relevantAttributes);

        // Keep only those attributes
        int numRelevantAttributes = relevantAttributes.size();
        for (int i = 0; i < set.size(); i++) {
            double[] relevantData = new double[numRelevantAttributes];
            int relevantNdx = 0;

            Instance inst = set.get(i);
            Vector data = inst.getData();
            for (Integer j : relevantAttributes) {
                relevantData[relevantNdx++] = data.get(j);
            }

            inst.setData(new DenseVector(relevantData));
        }

        // Induce data description
        set.setDescription(new DataSetDescription(set));
    }

    private Set<Integer> getIntegerAttributes(DecisionTreeNode node) {
        Set<Integer> aSet = new HashSet<>();
        if (node == null || node.isLeaf()) {
            return aSet;
        }

        DecisionTreeSplit split = node.getSplit();
        if (split instanceof IntegerAttribute) {
            IntegerAttribute iAttr = (IntegerAttribute) split;
            aSet.add(iAttr.getAttribute());
        }

        for (DecisionTreeNode child : node.getNodes()) {
            aSet.addAll(getIntegerAttributes(child));
        }

        return aSet;
    }

    public List<Integer> getRelevantAttributes() {
        return relevantAttributes;
    }

    public SplitEvaluator getSplitEvaluator() {
        return splitEvaluator;
    }

    public PruningCriteria getPruningCriteria() {
        return pruningCriteria;
    }
}
