package shared.tester;

import shared.Instance;

import java.util.Locale;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * CrossValidationTestMetric is a test metric that supports k-fold
 * cross validation.
 * @author Daniel Cohen dcohen@gatech.edu
 * @version 1.0
 */

public class CrossValidationTestMetric extends TestMetric {

    private int foldAccum;
    private double foldAvg;

    private double finalAccum;
    private double finalAvg;

    private int foldSize;
    private int validationSize;
    private int k;


    public CrossValidationTestMetric(int dataSize, int k) {
        this.foldSize = dataSize / k;
        this.validationSize = k - 1;
        this.k = k;
    }

    public void nextFold() {
        this.foldAvg /= (double) this.validationSize;
        this.finalAccum += this.foldAvg;
        this.foldAvg = 0.0;
    }

    public void nextValidationFold() {
        this.foldAvg += (double) this.foldAccum / this.foldSize;
        this.foldAccum = 0;
    }

    @Override
    public void addResult(Instance expected, Instance actual) {
        Comparison c = new Comparison(expected, actual);

        if (c.isAllCorrect()) {
            this.foldAccum++;
        }
    }
        

    @Override
    public String toString() {
        this.finalAvg = this.finalAccum / (this.k - 1);
        NumberFormat percentFormatter = new DecimalFormat("##.##%");

        return "Correctly classified: " + percentFormatter.format(this.finalAvg) + "\n" +
               "Incorrectly classified: " + percentFormatter.format(1 - this.finalAvg);
    }

    @Override
    public void printResults() {
        System.out.println(this);
    }
        
}
