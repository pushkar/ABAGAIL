package func.nn.backprop;

/**
 * A subclass of <code> WeightUpdateRule </code> with a resilient backpropagation implementation which focuses on
 * the error and its sign value in order to determine whether to increase, decrease, or change the direction
 * of the learning rate for updating the weight value of a <code> BackPropagationLink </code> object used to pass values between 
 * <code> BackPropagationNode </code> objects that lie within each <code> BackPropagationLayer </code> of a 
 * <code> BackPropagationNetwork </code>.
 * A resilient backpropagation implementation
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RPROPUpdateRule extends WeightUpdateRule {

	/**
	 * The factor by which we increase the learning rate.
	 */
	private static final double INCREASE = 1.2;
	/**
	 * The factor by which we decrease the learning rate.
	 */
	private static final double DECREASE = .5;
	
	/**
	 * The initial learning rate to be stored and later adjusted in each back propagation link's learning rate variable.
	 * @see BackPropagationLink#setLearningRate(double)
	 * @see BackPropagationLink#getLearningRate()
	 */
	private double initialLearningRate;

	/**
	 * The maximum learning rate to which each link's learning rate can be adjusted.
	 * @see BackPropagationLink#setLearningRate(double)
	 * @see BackPropagationLink#getLearningRate()
	 */
	private double maxLearningRate;
	
	/**
	 * The minimum learning rate to which each link's learning rate can be adjusted.
	 * @see BackPropagationLink#setLearningRate(double)
	 * @see BackPropagationLink#getLearningRate()
	 */
	private double minLearningRate;
	
	/**
	 * Creates a new rpop update rule object with a given an initial learning rate that determines the magnitude
     * with which each link's weights are adjusted. This constructor also provides upper and lower bounds for 
     * the learning rate as it changes.
     * @param initial the initial learning rate
	 * @param max the maximum learning rate
	 * @param min the minimum learning rate
     * @see BackPropagationLink
     * @see BackPropagationNode
     * @see RPROPUpdateRule#RPROPUpdateRule()
     * @see RPROPUpdateRule#initialLearningRate
	 */
	public RPROPUpdateRule(double initial, double max, double min) {
		this.initialLearningRate = initial;
		this.maxLearningRate = max;
		this.minLearningRate = min;
	}
	
	/**
	 * Creates a new generic rprop update rule with an initial learning rate of 0.1, a maximum
	 * learning rate of 50, and a minimum learning rate of 0.000001.
     * @see RPROPUpdateRule#RPROPUpdateRule(double, double, double)
     * @see RPROPUpdateRule#initialLearningRate
	 */
	public RPROPUpdateRule() {
		this(.1, 50, .000001);
	}

	/**
	 * Updates a given link's weight value depending on the current and previous error values. When both
	 * error values are in the same direction (both positive or both negative), it recognizes that the recent
	 * weight change was not enough to get the error back to zero and so the learning rate is increased. It
	 * then adjusts the weight in the direction opposite the sign of the error so that the error is moving closer
	 * to zero. If the error values are in opposite directions (one is positive and one is negative), then the
	 * change was too large and so we decrease it and reset to the previous step while also setting the error to 0.
	 * When the error for either is zero, the learning rate is used to make a step in some direction opposite the 
	 * value of the current error.
     * @param link a back propagation link whose weight is to be updated
     * @see BackPropagationLink#getLearningRate()
     * @see BackPropagationLink#getError()
     * @see BackPropagationLink#getLastError()
     * @see BackPropagationLink#changeWeight(double)
     * @see BackPropagationLink#setError(double)
     * @see RPROPUpdateRule#initialLearningRate
	 * @see RPROPUpdateRule#maxLearningRate
	 * @see RPROPUpdateRule#minLearningRate
	 */
	public void update(BackPropagationLink link) {
		if (link.getLearningRate() == 0) {
			link.setLearningRate(initialLearningRate);
		}
		double sign = 0;
		if  (link.getError() < 0) {
			sign = -1;
		} else if (link.getError() > 0) {
			sign = 1;
		}
		if (link.getLastError() * link.getError() > 0) {
			link.setLearningRate(Math.min(
				link.getLearningRate() * INCREASE, maxLearningRate));
			link.changeWeight(-sign * link.getLearningRate());
		} else if (link.getLastError() * link.getError() < 0) {
			link.setLearningRate(Math.max(
				link.getLearningRate() * DECREASE, minLearningRate));
			link.setError(0);
			link.changeWeight(-link.getLastChange());
		} else {
			link.changeWeight(-sign * link.getLearningRate());
		}
	}

}
