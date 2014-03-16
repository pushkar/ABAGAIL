package util.linalg;

/**
 * An implementation of a vector that is dense
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DenseVector extends Vector {
    
    /**
     * The data
     */
    private double[] data;
    
    /**
     * Make a new dense vector
     * @param data the data
     */
    public DenseVector(double[] data) {
        this.data = data;
    }
    
    /**
     * Make a new dense vector of the given size
     * @param size the size to make it
     */
    public DenseVector(int size) {
        data = new double[size];
    }

    /**
     * @see linalg.Vector#size()
     */
    public int size() {
        return data.length;
    }

    /**
     * @see linalg.Vector#get(int)
     */
    public double get(int i) {
        return data[i];
    }
    
    /**
     * @see linalg.Vector#set(int, double)
     */
    public void set(int i, double value) {
        data[i] = value;
    }
    
    /**
     * Make an identity vector 
     * @param i the dimension of identity
     * @param size the size of the vector
     * @return the identity vector
     */
    public static Vector e(int i, int size) {
        double[] result = new double[size];
        result[i] = 1;
        return new DenseVector(result);
    }
    
    /**
     * Get the identity 1 vector of the given size
     * @param size the size
     * @return the identity vector
     */
    public static Vector e(int size) {
        return e(0, size);
    }


}
