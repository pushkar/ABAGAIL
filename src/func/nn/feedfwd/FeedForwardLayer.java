package func.nn.feedfwd;

import func.nn.Layer;
import java.util.List;

/**
 * A feed forward layer in a neural network
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FeedForwardLayer extends Layer {

    /**
     * Feed foward all of the nodes in this layer.
     */
    public void feedforward() {
        // Implement the stream stuff so this all happens
        // in parallel.
        List nodes = getNodes();
        nodes.parallelStream()
             .forEach((node) -> {
                FeedForwardNode ff_node = (FeedForwardNode)node;
                ff_node.feedforward();
        });
    }

}
