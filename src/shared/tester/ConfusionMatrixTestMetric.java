package shared.tester;

import java.util.HashMap;
import java.util.Map;

import shared.AttributeType;
import shared.DataSetDescription;
import shared.Instance;
import shared.reader.DataSetLabelBinarySeperator;

/**
 * A test metric to generate a confusion matrix.  This metric expects the true labels
 * to be supplied at construction time, both to make sure the results are binned correctly
 * and to ensure clean output.
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-05
 */
public class ConfusionMatrixTestMetric extends TestMetric {

    /**
     * A matrix entry.  This class holds an expected and actual instance
     * as a pair, and defines equals and hashCode for that pair.
     * 
     * @author thejenix
     *
     */
    private class MatrixEntry {
        private Instance expected;
        private Instance actual;
        
        public MatrixEntry(Instance expected, Instance actual) {
            this.expected = expected;
            this.actual   = actual;
        }
        
        @Override
        public boolean equals(Object arg0) {
            if (!(arg0 instanceof MatrixEntry)) {
                return super.equals(arg0);
            }
            
            MatrixEntry me = (MatrixEntry) arg0;
            if (me.expected.size() != expected.size()
                    || me.actual.size() != actual.size()) {
                return false;
            }
            //use the comparison class to test that these are equal
            Comparison c = new Comparison(expected, me.expected);
            Comparison d = new Comparison(actual,   me.actual);
            return c.isAllCorrect() && d.isAllCorrect();
        }
        
        @Override
        public int hashCode() {
            int hashCode = 0;
            for (int ii = 0; ii < expected.size(); ii++) {
                //scale the expected value, to provide separation between corresponding pairs
                // (e.g. a, b should be different from b, a)
                hashCode += 0x10000 * expected.getContinuous(ii);
                hashCode += actual.getContinuous(ii);
            }
            return hashCode;
        }
    }

    private Instance[] labels;
    private String[]   labelStrs;
    private Instance nullLabel = new Instance(-1);
    
    private Map<MatrixEntry, Integer> matrix = new HashMap<MatrixEntry, Integer>();

    /**
     * Construct the test metric with double valued labels.
     * 
     * NOTE: these display with several significant figures...we may want to change this.
     * @param labels
     */
    public ConfusionMatrixTestMetric(double[] labels) {
        this.labels    = new Instance[labels.length];
        this.labelStrs = new String[labels.length];
        for (int i = 0; i < labels.length; i++) {
            this.labels   [i] = new Instance(labels[i]);
            this.labelStrs[i] = Double.toString(labels[i]);
        }
    }

    /**
     * Construct the test metric with discrete (integer) labels.
     * 
     * @param labels
     */
    public ConfusionMatrixTestMetric(int[] labels) {
        this.labels    = new Instance[labels.length];
        this.labelStrs = new String[labels.length];
        for (int i = 0; i < labels.length; i++) {
            this.labels   [i] = new Instance(labels[i]);
            this.labelStrs[i] = Integer.toString(labels[i]);
        }
    }
    
    /**
     * Construct the test metric with boolean labels.
     * 
     * @param labels
     */
    public ConfusionMatrixTestMetric(boolean[] labels) {
        this.labels    = new Instance[labels.length];
        this.labelStrs = new String[labels.length];
        for (int i = 0; i < labels.length; i++) {
            this.labels   [i] = new Instance(labels[i]);
            //use "t" and "f" as the output string, for brevity
            this.labelStrs[i] = labels[i] ? "t" : "f";
        }
    }

    /**
     * Construct the test metric with discrete values, contained in the label desc.
     * 
     * @param labelDesc
     */
    public ConfusionMatrixTestMetric(DataSetDescription labelDesc) {
        for (AttributeType type : labelDesc.getAttributeTypes()) {
            if (type == AttributeType.CONTINUOUS) {
                throw new IllegalStateException("This metric only works with discrete or binary labels");
            }
        }
        int range = labelDesc.getDiscreteRange();
        this.labels    = new Instance[range];
        this.labelStrs = new String  [range];
        for (int i = 0; i < labelDesc.getDiscreteRange(); i++) {
            this.labels   [i] = new Instance(i);
            this.labelStrs[i] = Integer.toString(i);
        }
    }

    @Override
    public void addResult(Instance expected, Instance actual) {        
        //find the actual value in the list of classes
        //...this makes sure we work with homogeneous label values, so our
        // matrix is readable.
        Instance found = findLabel(this.labels, actual);
        MatrixEntry e = new MatrixEntry(expected, found);
        if (matrix.containsKey(e)) {
            matrix.put(e, matrix.get(e) + 1);
        } else {
            matrix.put(e, 1);
        }
    }

    /**
     * Find a label in the array of expected labels, using the Comparison class to validate correctness.
     * This is important for building the matrix, as it smooths out the noise (however small) that may be present
     * in the output of the classifier.
     * 
     * @param labels
     * @param toFind
     * @return The corresponding label instance found in the array, or an object to represent the null label (i.e. not found)
     */
    private Instance findLabel(Instance[] labels, Instance toFind) {
        Instance found = this.nullLabel;
        for (Instance label : labels) {
            Comparison c = new Comparison(label, toFind);
            if (c.isAllCorrect()) {
                found = label;
                break;
            }
        }
        return found;
    }
    

    /**
     * 
     * NOTE: Rows are "expected", columns are "actual"
     * 
     */
    @Override
    public void printResults() {
        System.out.println("Confusion Matrix:");
        System.out.println();
        //TODO: substitute letters instead of the acutal values, for the axes (like weka)
        //print the top labels
        for (String l : labelStrs) {
            System.out.print("\t");
            System.out.print(l);
        }
        System.out.print("\t");
        System.out.print("?");
        for (int ii = 0; ii < labels.length; ii++) {
            Instance lr = labels[ii];
            System.out.println();
            System.out.print(labelStrs[ii]);
            for (Instance lc : labels) {
                Integer val = matrix.get(new MatrixEntry(lr, lc));
                if (val == null) {
                    val = 0;
                }
                System.out.print("\t");
                System.out.print(val);
            }
            Integer val = matrix.get(new MatrixEntry(lr, this.nullLabel));
            if (val == null) {
                val = 0;
            }
            System.out.print("\t");
            System.out.print(val);
        }
        
        System.out.println();
    }
}
