package func.nn.feedfwd;
import func.nn.feedfwd.FeedForwardNode;

/**
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-05
 */
public class FeedForwardBiasNode extends FeedForwardNode {

    public FeedForwardBiasNode(double activation) {
        super(null);
        super.setActivation(activation);
    }

    /**
* @see nn.FeedForwardNode#feedforward()
*/
    public void feedforward() { }
}