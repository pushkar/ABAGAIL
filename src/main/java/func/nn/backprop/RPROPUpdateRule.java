package func.nn.backprop;

/**
 * A resilient backpropagation implementation
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RPROPUpdateRule extends WeightUpdateRule {

	/**
	 * The increase parameter
	 */
	private static final double INCREASE = 1.2;
	/**
	 * The decrease parameter
	 */
	private static final double DECREASE = .5;
	
	/**
	 * The initial learning rate
	 */
	private double initialLearningRate;

	/**
	 * The max learning rate
	 */
	private double maxLearningRate;
	
	/**
	 * The min learning rate
	 */
	private double minLearningRate;
	
	/**
	 * Make a new rprop update rule
	 * @param initial the initial learning rate
	 * @param max the maximum learning rate
	 * @param min the minimum learning rate
	 */
	public RPROPUpdateRule(double initial, double max, double min) {
		this.initialLearningRate = initial;
		this.maxLearningRate = max;
		this.minLearningRate = min;
	}
	
	/**
	 * Make a new rprop update rule with default values
	 */
	public RPROPUpdateRule() {
		this(.1, 50, .000001);
	}

	/**
	 * @see nn.backprop.BackPropagationUpdateRule#update(nn.backprop.BackPropagationLink)
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
