package shared.test;

import func.dtree.*;
import shared.*;
import shared.filt.*;
import shared.reader.ArffDataSetReader;
import shared.writer.ArffWriter;
import util.linalg.DenseVector;
import util.linalg.Matrix;
import util.linalg.Vector;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Joshua Wang joshuawang@gatech.edu
 */
public class FeatureSelectionTest {
    private enum FeatureTransformationType {
        ICA,
        PCA,
        RPF,
        DTREE
    }

    public static String[] extractExtraArgs(String[] args, int index, int numExtraArgs) {
        String[] extraArgs = new String[numExtraArgs];
        for (int i = 0; i < numExtraArgs; i++) {
            int argsIndex = index + i + 1;
            if (argsIndex < args.length) {
                extraArgs[i] = args[argsIndex];
            }
        }
        return extraArgs;
    }

    public static void main(String[] args) throws Exception {
        String dataFile = new File("").getAbsolutePath() + "/src/shared/test/spambase.arff";
        FeatureTransformationType featureTransformationType = FeatureTransformationType.DTREE;
        String[] extraArgs = new String[0];
        boolean writeData = true;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-d")) {
                if (args.length > i + 1) {
                    dataFile = args[i + 1];
                }
            } else if (args[i].equals("-ICA")) {
                extraArgs = extractExtraArgs(args, i, 1);
                featureTransformationType = FeatureTransformationType.ICA;
            } else if (args[i].equals("-PCA")) {
                extraArgs = extractExtraArgs(args, i, 2);
                featureTransformationType = FeatureTransformationType.PCA;
            } else if (args[i].equals("-RPF")) {
                extraArgs = extractExtraArgs(args, i, 1);
                featureTransformationType = FeatureTransformationType.RPF;
            } else if (args[i].equals("-n")) {
                writeData = false;
            }
        }

        // Preprocess the data
        ArffDataSetReader dataReader = new ArffDataSetReader(dataFile);
        DataSet dataSet = dataReader.read();
        int labelIndex = dataSet.getInstances()[0].getData().size() - 1;

        // Label Split
        LabelSelectFilter lsf = new LabelSelectFilter(labelIndex);
        lsf.filter(dataSet);

        DataSet originalDataSet = dataSet.copy();

        // Select the features!
        for (int r = 0; r < 1; r++) {
            dataSet = originalDataSet.copy();

            // Define our feature selector
            DataSetFilter featureTransformer = null;
            if (featureTransformationType == FeatureTransformationType.ICA) {
                featureTransformer = new IndependentComponentAnalysis(dataSet, Integer.parseInt(extraArgs[0]));
            } else if (featureTransformationType == FeatureTransformationType.PCA) {
                featureTransformer = new PrincipalComponentAnalysis(
                        dataSet,
                        Integer.parseInt(extraArgs[0]),
                        Double.parseDouble(extraArgs[1])
                );
            } else if (featureTransformationType == featureTransformationType.RPF) {
                featureTransformer = new RandomizedProjectionFilter(
                        Integer.parseInt(extraArgs[0]),
                        dataSet.getDescription().getAttributeCount()
                );
            } else {
                SplitEvaluator splitEvaluator = new InformationGainSplitEvaluator();
                PruningCriteria pruningCriteria = new ChiSquarePruningCriteria(2);
                featureTransformer = new DecisionTreeSelector(splitEvaluator, null);
            }

            HashMap<StatisticType, Vector> preStats = getStatistics(dataSet);
            double preLinearKurtosis = getLinearKurtosisExcess(
                    preStats.get(StatisticType.KURTOSIS),
                    preStats.get(StatisticType.VARIANCE)
            );

            featureTransformer.filter(dataSet);

            //System.out.println("Features have been transformed!");

            if (writeData) {
                int endNdx = dataFile.lastIndexOf('.');
                String path = dataFile.substring(0, endNdx);
                String newFileName = path + "_" + featureTransformationType.name() + ".arff";

                System.out.println("Writing to " + newFileName);
                ArffWriter arffWriter = new ArffWriter(newFileName, dataSet);
                arffWriter.open();
                arffWriter.close();
                System.out.println("Completed.");
            } else {
                //System.out.println("NOT writing the data.");
            }

            //System.out.printf("%s:\n", featureTransformationType.name());
            if (featureTransformationType == FeatureTransformationType.PCA) {
                PrincipalComponentAnalysis pca = (PrincipalComponentAnalysis) featureTransformer;
                System.out.println("Eigenvalues:");
                double[] eigenValues = diagonalValues(pca.getEigenValues());
                for (int i = 0; i < eigenValues.length; i++) {
                    System.out.println(eigenValues[i]);
                }
                Matrix projection = pca.getProjection();
                System.out.printf("Projection is (%d x %d)\n", projection.m(), projection.n());
                System.out.println(projection);
                System.out.println();

            } else if (featureTransformationType == FeatureTransformationType.RPF) {
                RandomizedProjectionFilter rpf = (RandomizedProjectionFilter) featureTransformer;
                System.out.println(rpf.getProjection());

            } else if (featureTransformationType == FeatureTransformationType.ICA) {
                IndependentComponentAnalysis ica = (IndependentComponentAnalysis) featureTransformer;
                HashMap<StatisticType, Vector> postStats = getStatistics(dataSet);
                double postLinearKurtosis = getLinearKurtosisExcess(
                        postStats.get(StatisticType.KURTOSIS),
                        postStats.get(StatisticType.VARIANCE)
                );
                System.out.println(ica.getProjection());


                System.out.printf("Pre-Kurtosis: %f\n", preLinearKurtosis);
                System.out.println(preStats.get(StatisticType.KURTOSIS));

                System.out.printf("Post-Kurtosis: %f\n", postLinearKurtosis);
                System.out.println(postStats.get(StatisticType.KURTOSIS));

            } else if (featureTransformationType == FeatureTransformationType.DTREE) {
                DecisionTreeSelector dtree = (DecisionTreeSelector) featureTransformer;
                System.out.println(dtree.getRelevantAttributes());
            }
            if (featureTransformer instanceof ReversibleFilter) {
                ReversibleFilter reversibleFilter = (ReversibleFilter) featureTransformer;
                reversibleFilter.reverse(dataSet);
                double reconstructionError = getReconstructionError(
                        dataSet.getInstances(),
                        originalDataSet.getInstances(),
                        new EuclideanDistance()
                );
                System.out.printf("Reconstruction error: %f\n", reconstructionError);
            }
        }

    }

    public static double[] diagonalValues(Matrix matrix) {
        int dim = Math.min(matrix.m(), matrix.n());
        double[] values = new double[dim];
        for (int i = 0; i < dim; i++) {
            values[i] = matrix.get(i, i);
        }
        return values;
    }

    private enum StatisticType {
        MEAN,
        VARIANCE,
        KURTOSIS
    }

    public static HashMap<StatisticType, Vector> getStatistics(DataSet set) {
        HashMap<StatisticType, Vector> statisticMap = new HashMap<>();
        double[] mean = getMean(set);
        statisticMap.put(StatisticType.MEAN, new DenseVector(mean));

        double[] variance = getVariance(set, mean);
        statisticMap.put(StatisticType.VARIANCE, new DenseVector(variance));

        double[] kurtosis = getKurtosis(set, mean, variance);
        statisticMap.put(StatisticType.KURTOSIS, new DenseVector(kurtosis));

        return statisticMap;
    }

    public static double getLinearKurtosisExcess(Vector kurtosisVector, Vector varianceVector) {
        double linKurtosis = 0;

        double variance2 = 0;
        for (int i = 0; i < varianceVector.size(); i++) {
            variance2 += varianceVector.get(i);
        }
        variance2 = Math.pow(variance2, 2);

        for (int i = 0; i < kurtosisVector.size(); i++) {
            linKurtosis += Math.pow(varianceVector.get(i), 2) * (kurtosisVector.get(i) - 3);
        }

        linKurtosis *= (1 / variance2);

        return linKurtosis;
    }

    public static double[] getKurtosis(DataSet set) {
        double[] mean = getMean(set);
        return getKurtosis(set, mean, getVariance(set, mean));
    }

    public static double[] getKurtosis(DataSet set, double[] mean, double[] variance) {
        double[] m4 = new double[set.getDescription().getAttributeCount()];
        double[] m22 = variance;
        double[] k = new double[m4.length];

        for (Instance inst : set.getInstances()) {
            Vector data = inst.getData();
            for (int i = 0; i < data.size(); i++) {
                double diff2 = Math.pow((data.get(i) - mean[i]), 2);
                m4[i] += Math.pow(diff2, 2);
            }
        }

        for (int i = 0; i < m4.length; i++) {
            m22[i] = Math.pow(m22[i], 2);
            m4[i] = m4[i] / set.size();
            k[i] = m4[i] / m22[i];
        }

        return k;
    }

    public static double[] getVariance(DataSet set) {
        return getVariance(set, getMean(set));
    }

    public static double[] getVariance(DataSet set, double[] mean) {
        double[] m2 = new double[set.getDescription().getAttributeCount()];

        for (Instance inst : set.getInstances()) {
            Vector data = inst.getData();
            for (int i = 0; i < data.size(); i++) {
                double diff2 = Math.pow((data.get(i) - mean[i]), 2);
                m2[i] += diff2;
            }
        }

        for (int i = 0; i < m2.length; i++) {
            m2[i] = m2[i] / set.size();
        }

        return m2;
    }

    public static double[] getMean(DataSet set) {
        double[] mean = new double[set.getDescription().getAttributeCount()];
        for (Instance inst : set.getInstances()) {
            Vector data = inst.getData();
            for (int i = 0; i < data.size(); i++) {
                mean[i] += data.get(i);
            }
        }

        for (int i = 0; i < mean.length; i++) {
            mean[i] = mean[i] / set.size();
        }
        return mean;
    }

    public static double getReconstructionError(Instance[] instancesA, Instance[] instancesB, DistanceMeasure measure) {
        double error = 0;

        int size = Math.min(instancesA.length, instancesB.length);
        for (int i = 0; i < size; i++) {
            error += measure.value(instancesA[i], instancesB[i]);
        }
        return error / size;
    }
}
