package opt.ga;

import dist.Distribution;

import shared.Instance;

/**
 * A single point cross over function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SingleCrossOver implements CrossoverFunction {

    /**
     * @see opt.CrossOverFunction#mate(opt.OptimizationData, opt.OptimizationData)
     */
    public Instance mate(Instance a, Instance b) {
        double[] newData = new double[a.size()];
        int point = Distribution.random.nextInt(newData.length + 1);
        for (int i = 0; i < newData.length; i++) {
            if (i >= point) {
                newData[i] = a.getContinuous(i);
            } else {
                newData[i] = b.getContinuous(i);
            }
        }
        return new Instance(newData);
    }

}