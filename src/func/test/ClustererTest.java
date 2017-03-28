package func.test;

import dist.AbstractConditionalDistribution;
import dist.Distribution;
import dist.MultivariateGaussian;
import func.EMClusterer;
import func.KMeansClusterer;
import shared.DataSet;
import shared.Instance;
import shared.filt.LabelSelectFilter;
import shared.reader.ArffDataSetReader;
import util.linalg.DenseVector;
import util.linalg.RectangularMatrix;

import java.io.File;

/**
 * Testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ClustererTest {
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) throws Exception {
        String dataFile = new File("").getAbsolutePath() + "/src/shared/test/spambase.arff";
        int k = 2;
        boolean useEM = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-d")) {
                if (args.length > i + 1) {
                    dataFile = args[i + 1];
                }
            } else if (args[i].equals("-k")) {
                if (args.length > i + 1) {
                    k = Integer.parseInt(args[i + 1]);
                }
            } else if (args[i].equals("-em")) {
                useEM = true;
            }
        }

        // Preprocess the data
        ArffDataSetReader dataReader = new ArffDataSetReader(dataFile);
        DataSet dataSet = dataReader.read();
        int labelIndex = dataSet.getInstances()[0].getData().size() - 1;

        // Label Split
        LabelSelectFilter lsf = new LabelSelectFilter(labelIndex);
        lsf.filter(dataSet);

        AbstractConditionalDistribution clusterer;

        if (useEM) {
            clusterer = new EMClusterer(k, 1E-6, 1000);
        } else {
            clusterer = new KMeansClusterer(k);
        }

        clusterer.estimate(dataSet);

        System.out.println("Results:");
        System.out.println(clusterer);
    }
}
