package shared.filt;

import dist.MultivariateGaussian;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import shared.filt.ica.*;
import util.linalg.DenseVector;
import util.linalg.DiagonalMatrix;
import util.linalg.Matrix;
import util.linalg.SymmetricEigenvalueDecomposition;
import util.linalg.Vector;

/**
 * A filter for performing ICA on data
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class IndependentComponentAnalysis implements ReversibleFilter {
    /**
     * The projection matrix
     */
    private Matrix projection;
    /**
     * The reverse projection
     */
    private Matrix reverseProjection;
    
    /**
     * The pca preprocessing filter
     */
    private PrincipalComponentAnalysis pca;

    /**
     * Make a new ICA filter
     * @param dataSet the set form which to estimate components
     */
    public IndependentComponentAnalysis(DataSet dataSet) {
        this(dataSet, -1);
    }
    
    /**
     * Make a new ICA filter
     * @param dataSet the set form which to estimate components
     * @param numberOfComponents the number of components
     */
    public IndependentComponentAnalysis(DataSet dataSet, int numberOfComponents) {
        this(dataSet, numberOfComponents, 1.0,
            new HyperbolicTangentContrast(), .00001, 1000);
    }
    
    /**
     * Make a new ICA filter
     * @param dataSet the set form which to estimate components
     * @param numberOfComponents the number of components to estimate
     * @param mu the step size
     * @param cf the contrast function
     * @param tolerance the tolerance value
     */
    public IndependentComponentAnalysis(DataSet dataSet, int numberOfComponents,
            double mu, ContrastFunction cf, double tolerance, int maxIterations) {
        // copy the data set
        Instance[] copy = new Instance[dataSet.size()];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = (Instance) dataSet.get(i).copy();
        }
        dataSet = new DataSet(copy);
        
        if (numberOfComponents == -1) {
            numberOfComponents = copy[0].size();
        }
        
        // perform pca and whitening
        pca = new PrincipalComponentAnalysis(dataSet);      
        pca.filter(dataSet);
        MultivariateGaussian mg = new MultivariateGaussian();
        mg.estimate(dataSet);
        DiagonalMatrix covarianceMatrix = new DiagonalMatrix(mg.getCovarianceMatrix());
        DiagonalMatrix whiteningMatrix = covarianceMatrix.inverse().squareRoot();
        DiagonalMatrix dewhiteningMatrix = covarianceMatrix.squareRoot();
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            instance.setData(whiteningMatrix.times(instance.getData()));
        }
        
        // make the w matrix out of random orthonormal stuff
        RandomizedProjectionFilter rpf = new RandomizedProjectionFilter(whiteningMatrix.m(), numberOfComponents);
        Matrix w = rpf.getProjection();
        
        // do the damn thing
        boolean done = false;
        int iterations = 0;
        while (!done && iterations < maxIterations) {
            Matrix oldW = (Matrix) w.copy();
            // loop through the w weight vectors and do the update
            for (int i = 0; i < w.n(); i++) {
                Vector wv = w.getColumn(i);
                // the expectation of x*g(wt * x)
                Vector exg = new DenseVector(wv.size());
                // the expectation of g'(wt * x)
                double egprime = 0;
                // the expectation of wt*x*g(wt*x)
                double beta = 0;
                for (int j = 0; j < dataSet.size(); j++) {
                      Vector x = dataSet.get(j).getData();
                      double dotProduct = wv.dotProduct(x);
                      Vector xg = x.times(cf.g(dotProduct));
                      exg.plusEquals(xg);
                      egprime += cf.gprime(dotProduct);
                      beta += wv.dotProduct(xg);
                }
                exg.timesEquals(1.0/dataSet.size());
                beta *= 1.0/dataSet.size();
                egprime *= 1.0/dataSet.size();

                // the change in w
                Vector dw = exg.minus(wv.times(beta));
                dw.timesEquals(-mu/(egprime - beta));
                // apply the change
                wv.plusEquals(dw);
                // normalize
                wv.timesEquals(1/wv.norm());
                w.setColumn(i, wv);
            }
            // do the symmetric eigenvalue decomposition on w*wt
            SymmetricEigenvalueDecomposition sed = 
                new SymmetricEigenvalueDecomposition(w.transpose().times(w));
            // deccorolate the thing
            w = w.times(sed.getU().times(sed.getD().inverse().squareRoot()
                .times(sed.getU().transpose())));
            // check if we are done
            Matrix ones = oldW.transpose().times(w);
            double maxOff = 0;
            for (int i = 0; i < ones.m(); i++) {
                maxOff = Math.max(maxOff, 1 - Math.abs(ones.get(i,i)));
            }
            done = maxOff < tolerance;
            iterations++;
        }
        // make the projection and reverse projections
        projection = w.transpose().times(whiteningMatrix);
        reverseProjection = dewhiteningMatrix.times(w);      
    }

    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        pca.filter(dataSet);
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            instance.setData(projection.times(instance.getData()));
        }
        
        dataSet.setDescription(new DataSetDescription(dataSet));
    }
    
    /**
     * @see shared.filt.ReversibleFilter#reverse(shared.DataSet)
     */
    public void reverse(DataSet dataSet) {
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            instance.setData(reverseProjection.times(instance.getData()));
        }
        pca.reverse(dataSet);
        dataSet.setDescription(new DataSetDescription(dataSet));
    }
    
    

    /**
     * Get the pca filter
     * @return the pca filter
     */
    public PrincipalComponentAnalysis getPCA() {
        return pca;
    }

    /**
     * Get the projection
     * @return the projection
     */
    public Matrix getProjection() {
        return projection;
    }

    /**
     * Get the reverse projection
     * @return the reverse projection
     */
    public Matrix getReverseProjection() {
        return reverseProjection;
    }


}
