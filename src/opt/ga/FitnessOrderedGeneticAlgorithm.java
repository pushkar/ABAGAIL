package opt.ga;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;



import dist.DiscreteDistribution;

import opt.OptimizationAlgorithm;
import opt.ga.GeneticAlgorithmProblem;
import shared.Instance;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Genetic algorithms are pretty stupid.
 * This is based on the version in Andrew Moore's tutorial.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @author Vincenzo Dentamaro vdentamaro3@gatech.edu
 * @version 1.0
 */
public class FitnessOrderedGeneticAlgorithm extends OptimizationAlgorithm {
    
    /**
     * The random number generator
     */
    private static final Random random = new Random();
    
    /**
     * The population size
     */
    private int populationSize;
    
    /**
     * The number of population to mate
     * each time step
     */
    private int toMate;
    
    /**
     * The number of population to mutate
     * each time step
     */
    private int toMutate;
    
    /**
     * The population
     */
    private Instance[] population;
    
    /**
     * The values of the population
     */
    private double[] values;
    
    private HashMap<Double, Instance> map = new HashMap<>();
    
    /**
     * Make a new genetic algorithm
     * @param populationSize the size
     * @param toMate the number to mate each iteration
     * @param toMutate the number to mutate each iteration
     * @param gap the problem to solve
     */
    public FitnessOrderedGeneticAlgorithm(int populationSize, int toMate, int toMutate, GeneticAlgorithmProblem gap) {
        super(gap);
        this.toMate = toMate;
        this.toMutate = toMutate;
        this.populationSize = populationSize;
        population = new Instance[populationSize];
        for (int i = 0; i < population.length; i++) {
            population[i] = gap.random();
        }
        values = new double[populationSize];
        for (int i = 0; i < values.length; i++) {
            values[i] = gap.value(population[i]);
            
            map.put(values[i], population[i]);
        }
        
        
       
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
    	Arrays.sort(values);
        GeneticAlgorithmProblem ga = (GeneticAlgorithmProblem) getOptimizationProblem();
        double[] probabilities = new double[population.length];
        // calculate probability distribution over the population
        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = values[i];
            sum += probabilities[i];
        }
        if (Double.isInfinite(sum)) {
            return sum;
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        DiscreteDistribution dd = new DiscreteDistribution(probabilities);
  
        
        
        
        // make the children
        double[] newValues = new double[populationSize];
        Instance[] newPopulation = new Instance[populationSize];
        for (int i = 0; i < toMate; i++) {
            // pick the mates
        	int randa = ThreadLocalRandom.current().nextInt(toMate, populationSize);
        	int randb = ThreadLocalRandom.current().nextInt(toMate, populationSize);
        	
            Instance a = map.get(values[randa]);
            Instance b = map.get(values[randb]);
            // make the kid
            newPopulation[i] = ga.mate(a, b);
            newValues[i] = -1;
        }
        // elite for the rest
        for (int i = toMate; i < newPopulation.length; i++) {
        	int randa = ThreadLocalRandom.current().nextInt(toMate, populationSize);

            newPopulation[i] = (Instance) map.get(values[randa]).copy();
            newValues[i] = values[randa];
        }
        // mutate
        for (int i = 0; i < toMutate; i++) {
        	int j = random.nextInt(newPopulation.length);
            ga.mutate(newPopulation[j]);
            newValues[j] = -1;
        }
        // calculate the new values
        for (int i = 0; i < newValues.length; i++) {
            if (newValues[i] == -1) {
                newValues[i] = ga.value(newPopulation[i]);
            }
        }
        // the new generation
        population = newPopulation;
        values = newValues;
        
        map = new HashMap<Double, Instance>();
        for (int i = 0; i < values.length; i++) {

            map.put(values[i], population[i]);
        }
        
        
        return sum / populationSize;
    }

    /**
     * @see opt.OptimizationAlgorithm#getOptimalData()
     */
    public Instance getOptimal() {
       return map.get(values[values.length-1]);
    }

}
