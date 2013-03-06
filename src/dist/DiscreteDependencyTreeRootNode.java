package dist;


import shared.DataSet;
import shared.DataSetDescription;
import shared.Instance;
import util.ABAGAILArrays;
import util.graph.Edge;
import util.graph.Node;
import util.graph.Tree;

/**
 * A root node in a discrete dependency tree
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteDependencyTreeRootNode extends Node {
    /**
     * The unconditional probabilities
     */
    private double[] probabilities;
    
    /**
     * Build a dependency tree root out of the given node
     * @param node the node to build the root out of
     * @param ranges the ranges of the values
     * @param data the data itself
     */
    public DiscreteDependencyTreeRootNode(DataSet dataSet, Node node, double m,  Tree t) {
        DataSetDescription dsd = dataSet.getDescription();
        probabilities = new double[dsd.getDiscreteRange(node.getLabel())];
        double weightSum = 0;
        for (int i = 0; i < dataSet.size(); i++) {
            probabilities[dataSet.get(i).getDiscrete(node.getLabel())]
                += dataSet.get(i).getWeight();
            weightSum += dataSet.get(i).getWeight();
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = (probabilities[i] + m / probabilities.length)
                / (weightSum + m);
        }
        t.addNode(this);
        setLabel(node.getLabel());
        for (int i = 0; i < node.getEdgeCount(); i++) {
            DiscreteDependencyTreeNode dtn = new DiscreteDependencyTreeNode(dataSet,
                node.getEdge(i).getOther(node), node.getLabel(), m, t);
            connectDirected(dtn, new Edge());
        }
    }
    
    /**
     * Calculate the probability
     * @param instance the instance
     * @return the probability
     */
    public double probabilityOf(Instance instance) {
        DiscreteDistribution dd = new DiscreteDistribution(probabilities);
        double p = dd.p(new Instance(instance.getDiscrete(getLabel())));
        for (int i = 0; i < getEdgeCount(); i++) {
            DiscreteDependencyTreeNode dtn = (DiscreteDependencyTreeNode) getEdge(i).getOther(this);
            p *= dtn.probabilityOf(instance);
        }
        return p;
    }
    
    /**
     * Sample from the root of the tree
     * @param node the root of the tree
     */
    public void generateRandom(Instance instance) {
        DiscreteDistribution dd = new DiscreteDistribution(probabilities);
        instance.getData().set(getLabel(), dd.sample(null).getDiscrete());
        for (int i = 0; i < getEdgeCount(); i++) {
            DiscreteDependencyTreeNode dtn = (DiscreteDependencyTreeNode) getEdge(i).getOther(this);
            dtn.generateRandom(instance);
        }
    }
    
    /**
     * Sample from the root of the tree
     * @param node the root of the tree
     */
    public void generateMostLikely(Instance instance) {
        DiscreteDistribution dd = new DiscreteDistribution(probabilities);
        instance.getData().set(getLabel(), dd.mode(null).getDiscrete());
        for (int i = 0; i < getEdgeCount(); i++) {
            DiscreteDependencyTreeNode dtn = (DiscreteDependencyTreeNode) getEdge(i).getOther(this);
            dtn.generateMostLikely(instance);
        }
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
       return super.toString() + "\n" + ABAGAILArrays.toString(probabilities); 
    }

}
