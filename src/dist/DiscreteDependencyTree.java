package dist;


import util.linalg.DenseVector;
import util.graph.DFSTree;
import util.graph.Graph;
import util.graph.KruskalsMST;
import util.graph.Node;
import util.graph.Tree;
import util.graph.WeightedEdge;

import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;


/**
 * A discrete dependency distribution
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteDependencyTree extends AbstractDistribution {   
    /**
     * The dependency tree root
     */
    private DiscreteDependencyTreeRootNode root;
    
    /**
     * The tree
     */
    private Tree dt;
    
    /**
     * The m value
     */
    private double m;
    
    /**
     * Description the data set
     */
    private DataSetDescription description;
    
    /**
     * Make a new discrete dependency tree distribution
     * @param m the small positive value to add when making the tree
     */
    public DiscreteDependencyTree(double m) {
        this.m = m;
    }
    
    /**
     * Make a new discrete dependency tree distribution
     * @param m the small positive value to add when making the tree
     */
    public DiscreteDependencyTree(double m, int[] ranges) {
        this.m = m;
        description = new DataSetDescription();
        description.setMinVector(new DenseVector(ranges.length));
        DenseVector max = new DenseVector(ranges.length);
        for (int i = 0; i < max.size(); i++) {
            max.set(i, ranges[i] - 1);
        }
        description.setMaxVector(max);
    }

    /**
     * @see dist.Distribution#probabilityOf(shared.Instance)
     */
    public double p(Instance i) {
        return root.probabilityOf(i);
    }

    /**
     * @see dist.Distribution#generateRandom(shared.Instance)
     */
    public Instance sample(Instance ignored) {
        Instance i = new Instance(new DenseVector(dt.getNodeCount()));
        root.generateRandom(i);
        return i;
    }

    /**
     * @see dist.Distribution#generateMostLikely(shared.Instance)
     */
    public Instance mode(Instance ignored) {
        Instance i = new Instance(new DenseVector(dt.getNodeCount()));
        root.generateMostLikely(i);
        return i;
    }

    /**
     * @see dist.Distribution#estimate(shared.DataSet)
     */
    public void estimate(DataSet observations) {
        if (description != null) {
            observations.setDescription(description);
        } else if (observations.getDescription() == null) {
            observations.setDescription(new DataSetDescription(observations));
        }
        double[][] mutualI = calculateMutualInformation(observations);
        // construct the graph
        Tree rg = buildDirectedMST(observations, mutualI);
        // make the dependency tree
        dt = new Tree();
        root = new DiscreteDependencyTreeRootNode(observations, rg.getRoot(), m, dt);
        dt.setRoot(root);
        
    }

    /**
     * Build the directed mst from the mutual information
     * and ranges
     * @param ranges the ranges
     * @param mutualI the mutual information values
     * @return the directed mst
     */
    private Tree buildDirectedMST(DataSet observations, double[][] mutualI) {
        Graph g = new Graph();
        for (int i = 0; i < observations.get(0).size(); i++) {
            Node n = new Node(i);
            g.addNode(n);
        }
        for (int i = 0; i < observations.get(0).size(); i++) {
            for (int j = 0; j < i; j++) {
                Node a = g.getNode(i);
                Node b = g.getNode(j);
                a.connect(b, new WeightedEdge(-mutualI[i][j]));
            }
        }
        // find the mst
        g = new KruskalsMST().transform(g);
        // direct it
        Tree rg = (Tree) new DFSTree().transform(g);
        return rg;
    }

    /**
     * Calculate the mutual information from the data
     * @param ranges the ranges of the data
     * @param data the data itself
     * @return the mutual informations
     */
    private double[][] calculateMutualInformation(DataSet observations) {
        DataSetDescription dsd = observations.getDescription();
        // probs[i][j] is the probability that x_i = j
        double[][] probs = new double[observations.get(0).size()][];
        for (int i = 0; i < probs.length; i++) {
            probs[i] = new double[dsd.getDiscreteRange(i)];
        }
        double weightSum = 0;
        // fill in probs
        for (int i = 0; i < observations.size(); i++) {
            for (int j = 0; j < observations.get(i).size(); j++) {
                probs[j][observations.get(i).getDiscrete(j)] +=
                    observations.get(i).getWeight();
            }
            weightSum += observations.get(i).getWeight();
        }
        // normalize
        for (int i = 0; i < probs.length; i++) {
            for (int j = 0; j < probs[i].length; j++) {
                probs[i][j] /= weightSum;
            }
        }
        // calculate the entropies of the different variables
        double[] entropies = new double[observations.get(0).size()];
        for (int i = 0; i < observations.get(0).size(); i++) {
            for (int j = 0; j < dsd.getDiscreteRange(i); j++) {
                if (probs[i][j] != 0) {
                    entropies[i] -= probs[i][j] * Math.log(probs[i][j]);
                }
            }
        }
        // calculate the mutual information between all variables
        double[][] mutualI = new double[observations.get(0).size()][];
        for (int i = 0; i < mutualI.length; i++) {
            mutualI[i] = new double[i];
            for (int j = 0; j < i; j++) {
                // the joint probabilities
                // joints[a][b] is the probability that x_i = a && x_j = b
                double[][] joints = new double[dsd.getDiscreteRange(i)][dsd.getDiscreteRange(j)];
                // fill in the joints
                for (int k = 0; k < observations.size(); k++) {
                    Instance instance = observations.get(k);
                    joints[instance.getDiscrete(i)][instance.getDiscrete(j)] ++;
                }
                // normalize
                for (int k = 0; k < joints.length; k++) {
                    for (int l = 0; l < joints[k].length; l++) {
                        joints[k][l] /= weightSum;
                    }
                }
                // calculate the mutual information I(x_i; x_j)
                // add the entropy of x_i
                mutualI[i][j] += entropies[i];
                // and the entropy of x_j
                mutualI[i][j] += entropies[j];
                // subtract the joint entropy
                for (int k = 0; k < joints.length; k++) {
                    for (int l = 0; l < joints[k].length; l++) {
                        if (joints[k][l] != 0) {
                            mutualI[i][j] += joints[k][l] * Math.log(joints[k][l]);
                        }
                    }
                }
            }
        }
        return mutualI;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return dt.toString();
    }



}
