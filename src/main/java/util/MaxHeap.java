package util;

/**
 * A priority queue class
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MaxHeap {

    /**
     * The items in the queue
     */
    private HeapItem[] items;
    
    /**
     * Te current size of the heap
     */
    private int size;
       
    /**
     * Make a new priority queue
     * @param capacity the capacity of the queue
     */
    public MaxHeap(int capacity) {
        items = new HeapItem[capacity + 1];
    }
    
    /**
     * Enqueue an object
     * @param o the object
     * @param priority the priority of an object 
     */
    public void add(Object o, double priority) {
        if (size + 1 >= items.length) {
            HeapItem[] oldItems = items;
            items = new HeapItem[oldItems.length * 2];
            System.arraycopy(oldItems, 0, items, 0, oldItems.length);
        }
        size++;
        items[size] = new HeapItem(o, priority);
        int i = size;
        while (i > 1 && items[parent(i)].priority < items[i].priority) {
            HeapItem temp = items[i];
            items[i] = items[parent(i)];
            items[parent(i)] = temp;
            i = parent(i);
        }
    }
    
    /**
     * Get the item at the front of the queue
     * @return the item
     */
    public Object extractMax() {
        HeapItem item = items[1];
        items[1] = items[size];
        size--;
        heapify(1);
        return item.object;
    }
    
    /**
     * The heapify sub routine
     * @param items the items
     * @param i the index to heapify
     */
    private void heapify(int i) {
        int l = left(i);
        int r = right(i);
        int largest = i;
        if (l <= size && items[l].priority > items[largest].priority) {
            largest = l;
        }
        if (r <= size && items[r].priority > items[largest].priority) {
            largest = r;
        }
        if (largest != i) {
            HeapItem temp = items[i];
            items[i] = items[largest];
            items[largest] = temp;
            heapify(largest);
        }
    }
    
    /**
     * Get the left child of an index
     * @param i the index to get
     * @return the left child
     */
    private int left(int i) {
        return 2*i;
    }
    
    /**
     * Get the right child
     * @param i the index
     * @return the right child
     */
    private int right(int i) {
        return 2*i + 1;
    }
    
    /**
     * Get the parent
     * @param i the index
     * @return the parent
     */
    private int parent(int i) {
        return i / 2;
    }

    /**
     * Peek at the item at the front of the queue
     * @return the item at the front of the queue
     */
    public Object getMaxObject() {
        HeapItem item = items[1];
        return item.object;
    }
    
    /**
     * Get the item with the highest priority
     * @return the item
     */
    public double getMaxKey() {
        return items[1].priority;
    }
    
    /**
     * Get the current size of the queue
     * @return the size
     */
    public int size() {
        return size;
    }
    
    /**
     * Get the data
     * @return the data
     */
    public Object[] getData() {
        Object[] data = new Object[size];
        for (int i = 1; i <= size; i++) {
            data[i - 1] = items[i].object;
        }
        return data;
    }
    
    /**
     * A representation of a queued item
     */
    private class HeapItem {
        /**
         * The item itself
         */
        private Object object;
        /**
         * The priority of the item 
         */
        private double priority;
        
        /**
         * Create a new queued item
         * @param item the item queued
         * @param priority the priority
         */
        public HeapItem(Object item, double priority) {
            this.object = item;
            this.priority = priority;
        }          
    }
}
