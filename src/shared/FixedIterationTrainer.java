package shared;

import opt.OptimizationAlgorithm;
import opt.SimulatedAnnealing;

/**
 * A fixed iteration trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FixedIterationTrainer implements Trainer {

    public static boolean printDebug = false;
    
    /**
     * The inner trainer
     */
    private Trainer trainer;
    
    /**
     * The number of iterations to train
     */
    private int iterations;

    private long timeToTrain = 0;
    
    /**
     * Make a new fixed iterations trainer
     * @param t the trainer
     * @param iter the number of iterations
     */
    public FixedIterationTrainer(Trainer t, int iter) {
        trainer = t;
        iterations = iter;
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        timeToTrain = 0;
        long startTime = 0;
        long endTime = 0;
        double sum = 0;
        for (int i = 0; i < iterations; i++) {
            startTime = System.nanoTime();
            double currError = trainer.train();
            endTime = System.nanoTime();
            timeToTrain += (endTime - startTime);
            sum += currError;

            if (FixedIterationTrainer.printDebug && i % 100 == 0) {
                System.out.printf("%d\t", i);
                System.out.printf("\tTrained: %f", currError);
                System.out.println();
            }
        }
        return sum / iterations;
    }

    public long getTimeToTrain() {
        return timeToTrain;
    }
    

}
