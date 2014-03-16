package opt.example;

import dist.Distribution;

import opt.ga.CrossoverFunction;
import shared.Instance;

/**
 * A cross over function for a traveling
 * salesman problem, based on
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class TravelingSalesmanCrossOver implements CrossoverFunction {
    
    /**
     * The evaluation function
     */
    private TravelingSalesmanEvaluationFunction eval;
    
    /**
     * Make a new traveling salesman cross over
     * @param eval the evaluation function to use
     */
    public TravelingSalesmanCrossOver(TravelingSalesmanEvaluationFunction eval) {
        this.eval = eval;
    }

    /**
     * @see opt.ga.CrossOverFunction#mate(opt.OptimizationData, opt.OptimizationData)
     */
    public Instance mate(Instance a, Instance b) {
        int[] nexta = new int[a.size()];
        int[] nextb = new int[b.size()];
        for (int i = 0; i < a.size() - 1; i++) {
            nexta[a.getDiscrete(i)] = a.getDiscrete(i+1);
            nextb[b.getDiscrete(i)] = b.getDiscrete(i+1);
        }
        nexta[a.getDiscrete(a.size() - 1)] = a.getDiscrete(0);
        nexta[b.getDiscrete(b.size() - 1)] = b.getDiscrete(0);
        boolean[] visited = new boolean[a.size()];
        int[] child = new int[a.size()];
        child[0] = Distribution.random.nextInt(a.size());
        visited[child[0]] = true;
        for (int i = 0; i < child.length - 1; i++) {
            int cur = child[i];
            int na = nexta[cur];
            int nb = nextb[cur];
            int next = -1;
            if (visited[na] && !visited[nb]) {
                next = nb;
            } else if (visited[nb] && !visited[na]) {
                next = na;
            } else if (!visited[na] && !visited[nb]) {
                if (eval.getDistance(cur, na) < eval.getDistance(cur, nb)) {
                    next = na;
                } else {
                    next = nb;
                }
            } else {
                do {
                    next = Distribution.random.nextInt(a.size());
                } while (visited[next]);
            }
            child[i+1] = next;
            visited[next] = true;
        }
        double[] data = new double[child.length];
        for (int i = 0; i < child.length; i++) {
            data[i] = child[i];
        }
        return new Instance(data);
    }

}
