package shared;

/**
 * A fixed time (milliseconds) trainer
 * @author Timothy Ting
 * @version 1.0
 */
public class FixedTimeTrainer implements Trainer {

    /**
     * The inner trainer
     */
    private Trainer trainer;

    /**
     * The number of iterations we've trained
     */
    private int iterations;


    /**
     * The amount of time (in ms) to train
     */
    private double time;

    /**
     * Make a new fixed time trainer
     * @param t the trainer
     * @param ms the time in ms to train
     */
    public FixedTimeTrainer(Trainer t, double ms) {
        trainer = t;
        time = ms;
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        double start = System.nanoTime(), end;
        double trainingTime = 0;
        while (trainingTime < time) {
            trainer.train();
            iterations++;

            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,6);
        }
        return iterations;
    }


    /**
     * Return the number of iterations
     */
    public int getIterations() {
        return iterations;
    }


}