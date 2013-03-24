package shared;

/**
 * An occasional printer prints out a trainer ever once in a while
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class OccasionalPrinter implements Trainer {
    /**
     * The trainer being trained
     */
    private Trainer trainer;
    /**
     * How many iterations to go between print
     */
    private int iterationsPerPrint;
    /**
     * The current iteration
     */
    private int iteration;
    /**
     * Make a new occasional printer
     * @param iterationsPerPrint the number of iterations per print
     * @param t the trainer
     */
    public OccasionalPrinter(int iterationsPerPrint, Trainer t) {
        this.iterationsPerPrint = iterationsPerPrint;
        this.trainer = t;
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        if (iteration % iterationsPerPrint == 0) {
            System.out.println(trainer);
        }
        iteration++;
        return trainer.train();
    }

}
