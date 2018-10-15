package shared;



/**
 * Standard error measure, suitable for use with
 * linear output networks for regression, sigmoid
 * output networks for single class probability,
 * and soft max networks for multi class probabilities.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SumOfSquaresError extends AbstractErrorMeasure
        implements GradientErrorMeasure {

    /**
     * @see nn.error.ErrorMeasure#error(double[], nn.Pattern[], int)
     */
    public double value(Instance output, Instance example) {
        double sum = 0;
        Instance label = example.getLabel();
        for (int i = 0; i < output.size(); i++) {
            sum += (output.getContinuous(i) - label.getContinuous(i)) 
                * (output.getContinuous(i) - label.getContinuous(i))
                * example.getWeight();
        }
        return .5 * sum;
    }

    /**
     * @see nn.error.DifferentiableErrorMeasure#derivatives(double[], nn.Pattern[], int)
     */
    public double[] gradient(Instance output, Instance example) {      
        double[] errorArray = new double[output.size()];
        Instance label = example.getLabel();
        for (int i = 0; i < output.size(); i++) {
            errorArray[i] = (output.getContinuous(i) - label.getContinuous(i))
                * example.getWeight();
        }
        return errorArray;
    }

}
