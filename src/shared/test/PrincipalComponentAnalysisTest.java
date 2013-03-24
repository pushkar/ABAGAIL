package shared.test;

import shared.DataSet;
import shared.Instance;
import shared.filt.PrincipalComponentAnalysis;
import util.linalg.Matrix;

/**
 * A class for testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class PrincipalComponentAnalysisTest {
    
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Instance[] instances =  {
            new Instance(new double[] {1,1,0,0,0,0,0,0}),
            new Instance(new double[] {0,0,1,1,1,0,0,0}),
            new Instance(new double[] {0,0,0,0,1,1,1,1}),
            new Instance(new double[] {1,0,1,0,1,0,1,0}),
            new Instance(new double[] {1,1,0,0,1,1,0,0}),
        };
        DataSet set = new DataSet(instances);
        System.out.println("Before PCA");
        System.out.println(set);
        PrincipalComponentAnalysis filter = new PrincipalComponentAnalysis(set);
        System.out.println(filter.getEigenValues());
        System.out.println(filter.getProjection().transpose());
        filter.filter(set);
        System.out.println("After PCA");
        System.out.println(set);
        Matrix reverse = filter.getProjection().transpose();
        for (int i = 0; i < set.size(); i++) {
            Instance instance = set.get(i);
            instance.setData(reverse.times(instance.getData()).plus(filter.getMean()));
        }
        System.out.println("After reconstructing");
        System.out.println(set);
        
    }

}
