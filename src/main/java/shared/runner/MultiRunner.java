package shared.runner;

import java.io.File;

import shared.tester.AccuracyTestMetric;
import shared.tester.ConfusionMatrixTestMetric;
import shared.writer.CSVWriter;
import shared.writer.Writer;
import util.TimeUtil;

/**
 * A runner for multiple tests/experiments.  This class takes in a Runner, an array of iteration values to use,
 * and an array of test/train splits to use
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-06
 */
public class MultiRunner {

    private Runner runner;
    private int[] iterArray;
    private int[] pctTrainArray;
    private Writer writer;
    private File outputFolder;

    public MultiRunner(Runner runner, int[] iterArray, int[] pctTrainArray) {
        this.runner        = runner;
        this.iterArray     = iterArray;
        this.pctTrainArray = pctTrainArray;
    }

    /**
     * Run all combinations of iterations and test/train splits, and output the results.
     * 
     * @throws Exception
     */
    public void runAll() throws Exception {
        String[] outputFields = {
                "iterations",
                "% train",
                "% correct",
//                "% incorrect",
        };
        Writer writer = null;
        if (this.outputFolder != null) {
            File outputFile = new File(this.outputFolder, String.format("%s.csv", runner.getName()));
            writer = new CSVWriter(outputFile.getAbsolutePath(), outputFields);
            writer.open();
        }
        for (int iterations : iterArray) {
            for (int pctTrain : pctTrainArray) {
                runner.run(iterations, pctTrain);

                AccuracyTestMetric am = runner.getAccuracyMetric();
                ConfusionMatrixTestMetric cm = runner.getConfusionMatrix();
    
                //print results to the console
                String trainTime = TimeUtil.formatTime(runner.getTrainingTime());
                String testTime = TimeUtil.formatTime(runner.getTestTime());
                am.printResults();
                System.out.println("Training time: " + trainTime);
                System.out.println("Testing time: " + testTime);
                System.out.println("Number of iterations: " + iterations);
                if (pctTrain > 0 && pctTrain < 100) {
                    System.out.println(String.format("%02d%% training / %02d%% testing", pctTrain, 100 - pctTrain));
                } else {
                    System.out.println("Testing using training set.");
                }
                cm.printResults();
                System.out.println();
                
                //write results to a file if available
                if (writer != null) {
                    writer.write("" + iterations);
                    writer.write("" + pctTrain);
                    writer.write("" + am.getPctCorrect());
                    writer.nextRecord();
                }
            }
        }
        if (writer != null) {
            writer.close();
        }
    }
    
    /**
     * Set the output folder for results files.  If not set, this class
     * will not output any data to file.
     * 
     * @param outputFolder
     */
    public void setOutputFolder(File outputFolder) {
        this.outputFolder = outputFolder;
    }
}
