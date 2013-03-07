package opt.ga;

import shared.Instance;

import dist.Distribution;

/**
 * A uniform cross over function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class UniformCrossOver implements CrossoverFunction {

    /**
     * @see opt.CrossOverFunction#mate(opt.OptimizationData, opt.OptimizationData)
     */
    public Instance mate(Instance a, Instance b) {
        double[] newData = new double[a.size()];
        for (int i = 0; i < newData.length; i++) {
            if (Distribution.random.nextBoolean()) {
                newData[i] = a.getContinuous(i);
            } else {
                newData[i] = b.getContinuous(i);
            }
        }
        return new Instance(newData);
    }

}