package dist;


import util.ABAGAILArrays;
import util.graph.Edge;
import util.graph.Node;
import util.graph.Tree;
import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;

/**
 * A node in a discrete dependency tree
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteDependencyTreeNode extends Node {
    /** 
     * The conditional probabilities 
     */
    private double[][] probabilities;
    /**
     * The parent
     */
    private int parent;
    
    /**
     * Make a dependency tree node
     * @param ranges the ranges of the data
     * @param data the data itself
     * @param node the node
     * @param parent the parent node index
     * @param m the bayesian estimate parameter
     * @param t the tree
     */
    public DiscreteDependencyTreeNode(DataSet dataSet,
               Node node, int parent, double m, Tree t) {
        DataSetDescription dsd = dataSet.getDescription();
        double[][] probabilities = 
            new double[dsd.getDiscreteRange(parent)][dsd.getDiscreteRange(node.getLabel())];
        double[] sums = new double[dsd.getDiscreteRange(parent)];
        for (int i = 0; i < dataSet.size(); i++) {
            probabilities[dataSet.get(i).getDiscrete(parent)]
                [dataSet.get(i).getDiscrete(node.getLabel())] += dataSet.get(i).getWeight();
            sums[dataSet.get(i).getDiscrete(parent)] += dataSet.get(i).getWeight();
        }
        for (int i = 0; i < probabilities.length; i++) {
            for (int j = 0; j < probabilities[i].length; j++) {
                probabilities[i][j] = (probabilities[i][j] + m / probabilities[i].length)
                    / (sums[i] + m);
            }
        }
        this.probabilities = probabilities;
        this.parent = parent;
        t.addNode(this);
        setLabel(node.getLabel());
        for (int i = 0; i < node.getEdgeCount(); i++) {
            DiscreteDependencyTreeNode dtc = new DiscreteDependencyTreeNode(
                dataSet, node.getEdge(i).getOther(node), node.getLabel(), m, t);
            connectDirected(dtc, new Edge());
        }
    }
    
    /**
     * Calculate the probability
     * @param instance the instance
     * @return the probability
     */
    public double probabilityOf(Instance sample) {
        DiscreteDistribution dd = new DiscreteDistribution(
            probabilities[sample.getDiscrete(parent)]);
        double p = dd.p(new Instance(sample.getDiscrete(getLabel())));
        for (int i = 0; i < getEdgeCount(); i++) {
            DiscreteDependencyTreeNode dtn = (DiscreteDependencyTreeNode) getEdge(i).getOther(this);
            p *= dtn.probabilityOf(sample);
        }
        return p;
    }  

    /**
     * Sample from the node
     * @param sample the sample so far
     */
    public void generateRandom(Instance sample) {
        DiscreteDistribution dd = new DiscreteDistribution(
            probabilities[sample.getDiscrete(parent)]);
        sample.getData().set(getLabel(), dd.sample(null).getDiscrete());
        for (int i = 0; i < getEdgeCount(); i++) {
            DiscreteDependencyTreeNode dtn = (DiscreteDependencyTreeNode) getEdge(i).getOther(this);
            dtn.generateRandom(sample);
        }
    }  
    
    /**
     * Sample from the node
     * @param sample the sample so far
     */
    public void generateMostLikely(Instance sample) {
        DiscreteDistribution dd = new DiscreteDistribution(
            probabilities[sample.getDiscrete(parent)]);
        sample.getData().set(getLabel(), dd.mode(null).getDiscrete());
        for (int i = 0; i < getEdgeCount(); i++) {
            DiscreteDependencyTreeNode dtn = (DiscreteDependencyTreeNode) getEdge(i).getOther(this);
            dtn.generateRandom(sample);
        }
    }    
      
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return super.toString() + " Parent = " + parent + "\n" + ABAGAILArrays.toString(probabilities);
    }


}
