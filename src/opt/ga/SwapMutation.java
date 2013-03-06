package opt.ga;

import dist.Distribution;
import shared.Instance;

/**
 * A swap one mutation
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SwapMutation implements MutationFunction {

    /**
     * @see opt.ga.MutationFunction#mutate(opt.OptimizationData)
     */
    public void mutate(Instance d) {
        int i = Distribution.random.nextInt(d.size());
        int j = Distribution.random.nextInt(d.size());
        double temp = d.getContinuous(i);
        d.getData().set(i, d.getContinuous(j));
        d.getData().set(j, temp);
    }
}
