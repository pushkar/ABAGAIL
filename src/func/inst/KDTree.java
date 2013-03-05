package func.inst;
import java.io.Serializable;

import java.util.Arrays;
import java.util.Random;

import shared.*;
import shared.DataSet;
import shared.Instance;

/**
 * A KDTree implementation
 * Algorithms from Andrew Moore's tutorial
 * @author Andrew Guillory
 * @version 1.0
 */
public class KDTree implements Serializable {
    
    /**
     * Random number generator
     */
    private static final Random random = new Random();

    /** 
     * The head node of the kd tree
     */
    private KDTreeNode head;

    /**
     * The dimensionality of the tree (k)
     */
    private int dimensions;
    
    /**
     * The distance measure to use
     */
    private DistanceMeasure distanceMeasure;

    /**
     * Build a kd tree from the given parallel arrays
     * of keys and data
     * @param keys the array of keys
     * @param distance the distance measure
     */
    public KDTree(DataSet keys, DistanceMeasure distance) {
        dimensions = keys.get(0).size();
        distanceMeasure = distance;
        KDTreeNode[] nodes = new KDTreeNode[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            nodes[i] = new KDTreeNode(keys.get(i));
        }
        head = buildTree(nodes, 0, nodes.length);
    }
    
    /**
     * Build a kd tree from the given parallel arrays
     * of keys and data
     * @param keys the array of keys
     * @param data the array of data
     * @param distance the distance measure
     */
    public KDTree(DataSet keys) {
        this(keys, new EuclideanDistance());
    }

    /**
     * Build a tree from a list of nodes
     * @param nodes the list of nodes
     * @return the head node of the built tree
     */
    private KDTreeNode buildTree(KDTreeNode[] nodes, int start, int end) {
        if (start >= end) {
            // if we're done return null
            return null;
        } else if (start + 1 == end) {
            // or the last element
            return nodes[start];
        }
        // choose splitter
        int splitterIndex = chooseSplitterRandom(nodes, start, end);
        KDTreeNode splitter = nodes[splitterIndex];
        //  patition based on splitter
        splitterIndex = partition(nodes, start, end, splitterIndex);
        // recursively build tree
        splitter.setLeft(buildTree(nodes, start, splitterIndex));
        splitter.setRight(buildTree(nodes, splitterIndex + 1, end));
        return splitter;
    }
    
    /**
     * Partition an array based on a splitter
     * @param comparables the array
     * @param start the start index
     * @param end the end index
     * @param splitterIndex the splitter's index
     * @return the new splitter index
     */
    private int partition(Comparable[] comparables, int start, int end, 
            int splitterIndex) {
        swap(comparables, splitterIndex, end - 1);
        splitterIndex = end - 1;
        Comparable splitter = comparables[splitterIndex];
        int i = start - 1;
        for (int j =  start; j < end - 1; j++) {
            if (splitter.compareTo(comparables[j]) > 0) {
                i++;
                swap(comparables, i, j);
            }
        }
        swap(comparables, splitterIndex, i + 1);
        return i + 1;
    }
    
    /**
     * Swap two elements in an array
     * @param objects the array
     * @param i the first index
     * @param j the second index
     */
    private void swap(Object[] objects, int i, int j) {
        Object temp = objects[i];
        objects[i] = objects[j];
        objects[j] = temp;
    }
    
    /**
     * Choose a random splitter
     * @param nodes the nodes to choose from
     * @param start the start
     * @param end the end
     * @return the splitter
     */
    private int chooseSplitterRandom(KDTreeNode[] nodes, int start, int end) {
      int splitter = random.nextInt(end - start) + start;
      int dimension = random.nextInt(dimensions);
      nodes[splitter].setDimension(dimension);
      return splitter;
    }
    

    /**
     * Choose a splitter from a list of nodes
     * This isn't used because it is much slower than random,
     * and im not sure how to implement it any faster
     * @param nodes the list of nodes to pick from
     * @param start the start index
     * @param end the end index
     * @return the best splitter from the list
     */
    private int chooseSplitterSmart(KDTreeNode[] nodes, int start, int end) {
        // find the ranges of the dimensions
        double[] min = new double[dimensions];
        Arrays.fill(min, Double.POSITIVE_INFINITY);
        double[] max = new double[dimensions];
        Arrays.fill(max, Double.NEGATIVE_INFINITY);
        for (int i = start; i < end; i++) {
            Instance key = nodes[i].getInstance();
            for (int j = 0; j < dimensions; j++) {
                min[j] = Math.min(min[j], key.getContinuous(j));
                max[j] = Math.max(max[j], key.getContinuous(j));
            }
        }
        // find the widest dimension
        int widestDimension = 0;
        double widestWidth = max[0] - min[0];
        for (int i = 1; i < dimensions; i++) {
            if (max[i] - min[i] > widestWidth) {
                widestDimension = i;
                widestWidth = max[i] - min[i];
            }
        }
        // find the middle of the widest dimension
        double median = (max[widestDimension] - min[widestDimension]) / 2;
        // find the best splitter
        double bestDifference = Double.POSITIVE_INFINITY;
        int splitterIndex = -1; 
        for (int i = start; i < end; i++) {
            KDTreeNode node = nodes[i];
            if (Math.abs(node.getInstance().getContinuous(widestDimension) - median)
                < bestDifference) {
                splitterIndex = i;
                bestDifference =
                    Math.abs(node.getInstance().getContinuous(widestDimension) - median);
            }
        }
        nodes[splitterIndex].setDimension(widestDimension);
        return splitterIndex;
    }

    /**
     * Perform a k nearest neighbor search
     * @param target the target of the search
     * @param k how many neighbors to find
     * @return the neighbors
     */
    public Instance[] knn(Instance target, int k) {
        NearestNeighborQueue results = new NearestNeighborQueue(k);
        knn(head, target, new HyperRectangle(dimensions), results);
        return results.getNearest();
    }
    
    /**
     * Perform a nearest neighbor search
     * @param target the target
     * @return the neighbors
     */
    public Instance[] nn(Instance target) {
        NearestNeighborQueue results = new NearestNeighborQueue();
        knn(head, target, new HyperRectangle(dimensions), results);
        return results.getNearest();         
    }

    /**
     * Perform a range search
     * @param target the target
     * @param range the range
     * @return the neighbors in the range
     */
    public Instance[] range(Instance target, double range) {
        NearestNeighborQueue results = new NearestNeighborQueue(range);
        knn(head, target, new HyperRectangle(dimensions), results);
        return results.getNearest();         
    }
    
    /**
     * Perform a k nearest neighbor range search
     * @param target the target
     * @param k the k value
     * @param range the range
     * @return the neighbours
     */
    public Instance[] knnrange(Instance target, int k, double range) {
        NearestNeighborQueue results = new NearestNeighborQueue(k, range);
        knn(head, target, new HyperRectangle(dimensions), results);
        return results.getNearest();         
    }
    
    /**
     * Perform a nearest neighbor search
     * @param node the node to search on
     * @param target the target
     * @param hr the hyper rectangle
     * @param results the current results
     */
    private void knn(KDTreeNode node, Instance target, HyperRectangle hr,  
            NearestNeighborQueue results) {
        if (node == null) {
            return;
        }
        HyperRectangle leftHR = hr.splitLeft(
            node.getSplitValue(), node.getDimension());
        HyperRectangle rightHR = hr.splitRight(
            node.getSplitValue(), node.getDimension());
        HyperRectangle nearHR, farHR;
        KDTreeNode nearNode, farNode;
        if (target.getContinuous(node.getDimension()) < node.getSplitValue()) {
            nearHR = leftHR; nearNode = node.getLeft();
            farHR = rightHR; farNode = node.getRight();
        } else {
            nearHR = rightHR; nearNode = node.getRight();
            farHR = leftHR; farNode = node.getLeft();
        }
        knn(nearNode, target, nearHR, results);
        if (distanceMeasure.value(
                farHR.pointNearestTo(target), target) 
                <= results.getMaxDistance()) {
            results.add(node.getInstance(), 
                distanceMeasure.value(node.getInstance(), target));
            knn(farNode, target, farHR, results);
        }
    }
}
