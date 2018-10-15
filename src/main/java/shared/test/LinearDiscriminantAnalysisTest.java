package shared.test;

import shared.DataSet;
import shared.Instance;
import shared.filt.LinearDiscriminantAnalysis;
import util.linalg.DenseVector;

/**
 * A class for testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LinearDiscriminantAnalysisTest {
    
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Instance[] instances =  {
            new Instance(new DenseVector(new double[] {100,1,0,0,0,0,0,0}), new Instance(1)),
            new Instance(new DenseVector(new double[] {0,0,10,10,100,0,0,0}), new Instance(0)),
            new Instance(new DenseVector(new double[] {0,0,0,0,1,1,10,10}), new Instance(0)),
            new Instance(new DenseVector(new double[] {100,0,10,0,1,0,1,0}), new Instance(1)),
            new Instance(new DenseVector(new double[] {100,10,0,0,10,1,0,0}), new Instance(1)),
        };
        DataSet set = new DataSet(instances);
        System.out.println("Before LDA");
        System.out.println(set);
        LinearDiscriminantAnalysis filter = new LinearDiscriminantAnalysis(set);
        filter.filter(set);
        System.out.println(filter.getProjection());
        System.out.println("After LDA");
        System.out.println(set);
        filter.reverse(set);
        System.out.println("After reconstructing");
        System.out.println(set);
        
    }

}
