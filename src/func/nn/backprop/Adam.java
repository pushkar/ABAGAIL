package func.nn.backprop;

/**
 * Adam implementation
 * pseudocode from Kingma and Ba
 * @author John Mansfield
 * @version 1.0
 */
public class Adam extends WeightUpdateRule {

  /**
   * The learning rate
   */
  private double alpha;

  /**
   * Decay rate B1 (between 0,1)
   */
  private double decay1;

  /**
   * Decay rate B2 (between 0,1)
   */
  private double decay2;

  /**
   * Mean (biased first moment estimate)
   */
  private double mean;

  /**
   * Uncentered variance (biased second raw moment estimate)
   */
  private double variance;

  /**
   * Adam constructor
   * @param alpha the initial learning rate
   * @param decay1 controls exponential decay
   * @param decay2 controls exponential decay
   */
  public Adam(double alpha, double decay1, double decay2) {
    this.alpha = alpha;
    this.decay1 = decay1;
    this.decay2 = decay2;
    this.mean=0;
    this.variance=0;
  }

  /**
   * Adam constructor with default values
   * suggested by paper
   */
  public Adam() {
    this(.001, .9, .999);
    this.mean=0;
    this.variance=0;
  }

  /**
   * @see func.nn.backprop.WeightUpdateRule#update(func.nn.backprop.BackPropagationLink)
   */

  public void update(BackPropagationLink link) {
    link.setTimeStep(link.getTimeStep()+1);
    mean = (decay1 * mean) + (1 - decay1) * link.getError();
    variance = (decay2 * variance) + (1 - decay2) * (Math.pow(link.getError(), 2));
    link.setLearningRate(alpha*Math.pow((1 - Math.pow(decay2, link.getTimeStep())), .5) / (1-Math.pow(decay1, link.getTimeStep())));
    link.setWeight(link.getWeight() - link.getLearningRate() * mean / (Math.pow(variance, .5) + .0000001));
  }
}
