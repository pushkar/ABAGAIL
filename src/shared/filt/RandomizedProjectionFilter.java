package shared.filt;

import shared.DataSet;
import shared.Instance;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.SingularValueDecomposition;

/**
 * A filter that performs a random projection
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RandomizedProjectionFilter implements ReversibleFilter {
    /**
     * The projection itself
     */
    private Matrix projection;
    /**
     * Make a new random projection
     * @param componentsToKeep the number of components to keep
     * @param components the number of original components
     */
    public RandomizedProjectionFilter(int componentsOut, int componentsIn){
        projection = RectangularMatrix.random(componentsOut, componentsIn);
        projection.minusEquals(RectangularMatrix.ones(projection.m(), projection.n()).times(.5));
        SingularValueDecomposition svd = new SingularValueDecomposition(projection);
        if (componentsIn <= componentsOut) {
            projection = svd.getU().get(0,projection.m(),0,projection.n());
        } else {
            projection = svd.getV().get(0,projection.m(),0,projection.n());
        }
    }

    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            instance.setData(projection.times(instance.getData()));
        }
    }
    
    /**
     * @see shared.filt.ReversibleFilter#reverse(shared.DataSet)
     */
    public void reverse(DataSet dataSet) {
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            instance.setData(projection.transpose().times(instance.getData()));
        }
    }

    /**
     * Get the projection
     * @return the projection
     */
    public Matrix getProjection() {
        return projection;
    }



}
