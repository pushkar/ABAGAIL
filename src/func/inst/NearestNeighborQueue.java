package func.inst;

import shared.Instance;
import util.MaxHeap;

/**
 * A class for storing and updating knn search results
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class NearestNeighborQueue {

    /**
     * The queue based on distance, that is
     * the items farthest from the target are the first to go
     */
    private MaxHeap queue;
    
    /**
     * The number of items we are searching for
     */
    private int k;
    
    /**
     * The maximum distance for range searches
     */
    private double maxDistance;
    
    /**
     * Make a new search results object
     * for a search
     * @param k the k value
     * @param maxDistance the range value
     */
    public NearestNeighborQueue(int k, double maxDistance) {
        this.k = k;
        this.queue = new MaxHeap(k);
        this.maxDistance = maxDistance;
    }
    
    /**
     * Make a new search result
     * @param maxDistance the maximum range
     */
    public NearestNeighborQueue(double maxDistance) {
        this(Integer.MAX_VALUE, maxDistance);
    }
    
    /**
     * Make a new search results object
     * @param k the k value
     */
    public NearestNeighborQueue(int k) {
        this(k, Double.POSITIVE_INFINITY);
    }
    
    /**
     * Make a new search results with k = 1
     */
    public NearestNeighborQueue() {
        this(1);
    }
    
    /**
     * Add this object if the results are not at capacity
     * or if the object's distance is lower than the highest
     * distance in the results
     * @param o the object to add
     * @param distance the distance the object is from the target
     */
    public void add(Instance o, double distance) {
        if (distance <= getMaxDistance()) {
            queue.add(o, distance);
            if (queue.size() > k) {
                queue.extractMax();
            }
        }
    }
    
    /**
     * Get the maximum distance a new search result must have
     * @return the distance
     */
    public double getMaxDistance() {
        if (queue.size() < k) {
            return maxDistance;
        } else {
            return queue.getMaxKey();
        }
    }
    
    /**
     * Get the result objects
     * @return the results
     */
    public Instance[] getNearest() {
        Object[] data = queue.getData();
        Instance[] results = new Instance[queue.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = (Instance) data[i];
        }
        return results;
    }

}
