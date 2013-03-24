package shared.filt;

import dist.MultivariateGaussian;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import util.linalg.CholeskyFactorization;
import util.linalg.LowerTriangularMatrix;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;
import util.linalg.SymmetricEigenvalueDecomposition;
import util.linalg.UpperTriangularMatrix;
import util.linalg.Vector;

/**
 * A filter that performs fisher linear discriminant
 * analysis on a data set
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class LinearDiscriminantAnalysis implements ReversibleFilter {
    
    /**
     * The projection matrix
     */
    private Matrix projection;
    /**
     * The mean
     */
    private Vector mean;
    
    /**
     * Make a new PCA filter
     * @param toKeep the number of components to keep
     * @param dataSet the set form which to estimate components
     */
    public LinearDiscriminantAnalysis(DataSet dataSet) {
        // calculate the mean
        MultivariateGaussian mg = new MultivariateGaussian();
        mg.estimate(dataSet);
        mean = mg.getMean();
        
        if (dataSet.getDescription() == null) {
            dataSet.setDescription(new DataSetDescription(dataSet));
        }
        
        // calculate the class counts and weight sums
        int classCount = dataSet.getDescription()
             .getLabelDescription().getDiscreteRange();
        int toKeep = classCount - 1;
        int[] classCounts = new int[classCount];
        double[] weightSums = new double[classCount];
        double weightSum = 0;
        for (int i = 0; i < dataSet.size(); i++) {
            int classification = dataSet.get(i).getLabel().getDiscrete();
            classCounts[classification]++;
            weightSums[classification] += dataSet.get(i).getWeight();
            weightSum += dataSet.get(i).getWeight();
        }
        // normalize the weight sums
        for (int i = 0; i < weightSums.length; i++) {
            weightSums[i] /= weightSum;
        }
        // seperate out the data
        Instance[][] instances = new Instance[classCount][];
        for (int i = 0; i < instances.length; i++) {
            instances[i] = new Instance[classCounts[i]];
            classCounts[i] = 0;
        }
        for (int i = 0; i < dataSet.size(); i++) {
            int classification = dataSet.get(i).getLabel().getDiscrete();
            instances[classification][classCounts[classification]] = dataSet.get(i);
            classCounts[classification]++;
        }
        
        // the between class covariance matrix
        Matrix sb = new RectangularMatrix(mean.size(), mean.size());
        // the within class covariance matrix
        Matrix sw = new RectangularMatrix(mean.size(), mean.size());
        // calculate the two matrices
        for (int i = 0; i < classCount; i++) {
            mg = new MultivariateGaussian();
            mg.estimate(new DataSet(instances[i]));
            sw.plusEquals(mg.getCovarianceMatrix().times(weightSums[i]));
            Vector classMean = mg.getMean();
            Vector classMeanMinusMean = classMean.minus(mean);
            sb.plusEquals(classMeanMinusMean.outerProduct(
                classMeanMinusMean).times(weightSums[i]));
        }
        
        
        // solve the symmetric-definite generalized eigenvalue problem
        CholeskyFactorization cf = new CholeskyFactorization(sw);
        LowerTriangularMatrix g = cf.getL();
        LowerTriangularMatrix gInverse = g.inverse();
        UpperTriangularMatrix gInverseTranspose = (UpperTriangularMatrix) gInverse.transpose();
        Matrix c = gInverse.times(sb).times(gInverseTranspose);
        SymmetricEigenvalueDecomposition sed = new SymmetricEigenvalueDecomposition(c);
        Matrix eigenVectors = gInverseTranspose.times(sed.getU());

        // keep the top vectors
        projection = new RectangularMatrix(toKeep, eigenVectors.m());
        for (int i = 0; i < toKeep; i++) {
            Vector v = eigenVectors.getColumn(i);
            projection.setRow(i, v.times(1.0/v.norm()));
        }
    }

    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            instance.setData(instance.getData().minus(mean));
            instance.setData(projection.times(instance.getData()));
        }
        dataSet.setDescription(null);
    }
   

    /**
     * @see shared.filt.ReversibleFilter#reverse(shared.DataSet)
     */
    public void reverse(DataSet dataSet) {
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            instance.setData(projection.transpose().times(instance.getData()));
            instance.setData(instance.getData().plus(mean));
        }
        dataSet.setDescription(null);
    }

    /**
     * Get the projection matrix used
     * @return the projection matrix
     */
    public Matrix getProjection() {
        return projection;
    }


    /**
     * Get the mean
     * @return the mean vector
     */
    public Vector getMean() {
        return mean;
    }

}
