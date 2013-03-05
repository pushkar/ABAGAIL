package util.linalg;

import java.io.Serializable;
import java.text.DecimalFormat;

import shared.Copyable;

/**
 * A class representing a vector with linear algebra
 * operations.
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class Vector implements Serializable, Copyable {
    

    /**
     * Get the size of the vector (the number of rows)
     * @return the number of rows
     */
    public abstract int size();
    
    /**
     * Get an element from the vecotr
     * @param i the element to get
     * @return the element
     */
    public abstract double get(int i);
    
    /**
     * Get some sub portion of the vector
     * @param ia the starting index (inclusive)
     * @param ib the ending index (exclusive)
     * @return the result
     */
    public Vector get(int ia, int ib) {
        double[] result = new double[ib - ia];
        for (int i = 0; i < result.length; i++) {
            result[i] = get(ia + i);
        }
        return new DenseVector(result);
    }
    
    /**
     * Set a portion of the vector
     * @param i the starting porition
     * @param values the values
     */
    public void set(int i, Vector values) {
        for (int row = i; row-i < values.size(); row++) {
            set(row, values.get(row-i));
        }
    }
    
    /**
     * Set an element in the vector
     * @param i the element to set
     * @param d the new value
     */
    public abstract void set(int i, double d);
    
    /**
     * Get the maximum of this vector with the given vector
     * @param v the other vector
     * @return the max
     */
    public Vector max(Vector v) {
        double[] result = new double[size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = Math.max(get(i), v.get(i));
        }
        return new DenseVector(result);
    }
    
    /**
     * Set this vector equal to the max of itself with 
     * the given vector
     * @param v the given vector
     */
    public void maxEquals(Vector v) {
        for (int i = 0; i < size(); i++) {
            set(i, Math.max(get(i), v.get(i)));
        }
    }
 
    /**
     * Set this vector equal to the min of itself with 
     * the given vector
     * @param v the given vector
     */
    public void minEquals(Vector v) {
        for (int i = 0; i < size(); i++) {
            set(i, Math.min(get(i), v.get(i)));
        }
    } 
    
    /**
     * Get the minimum of this vector with the given vector
     * @param v the other vector
     * @return the min
     */
    public Vector min(Vector v) {
        double[] result = new double[size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = Math.min(get(i), v.get(i));
        }
        return new DenseVector(result);
    }
    
    /**
     * Get the indice arg max of this vector
     * @return the arg max inidice
     */
    public int argMax() {
        int max = 0;
        for (int i = 1; i < size(); i++) {
            if (get(i) > get(max)) {
                max = i;
            }
        }
        return max;
    }
    
    /**
     * Dot product this vector with another vector
     * @param vector the other vector
     * @return the dot product
     */
    public double dotProduct(Vector vector) {
        double result = 0;
        for (int i = 0; i < size(); i++) {
            result += get(i) * vector.get(i);   
        }
        return result;
    }

    /**
     * Outer product this vector with another vector
     * @param vector the other vector
     * @return the outer product
     */    
    public Matrix outerProduct(Vector vector) {
        double[][] result = new double[size()][size()];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = get(i) * vector.get(j);
            }
        }
        return new RectangularMatrix(result);
    }
    
    /**
     * Multiply this vector by a scale
     * @param scale the scale
     * @return the multiplied vector
     */
    public Vector times(double scale) {
        Vector result = (Vector) copy();
        result.timesEquals(scale);
        return result;
    }
    
    /**
     * Multiply this vector by a scale in place
     * @param scale the scale
     */
    public void timesEquals(double scale) {
    	for (int i = 0; i < size(); i++) {
    		set(i, get(i) * scale);
    	}
    }
    
    /**
     * Add this vector to another vector
     * @param vector the other vector
     * @return the result
     */
    public Vector plus(Vector vector) {
      	Vector result = (Vector) copy();
      	result.plusEquals(vector);
      	return result;
    }
    
    /**
     * Get the sum of all of the entries
     * @return the sum
     */
    public double sum() {
        double sum = 0;
        for (int i = 0; i < size(); i++) {
            sum += get(i);
        }
        return sum;
    }
	
	/**
	 * Add a vector onto this vector in place
	 * @param vector the vector to add
	 */
	public void plusEquals(Vector vector) {
		for (int i = 0; i < size(); i++) {
			set(i, get(i) + vector.get(i));
		}
	}

    /**
     * Subtract a vector from this vector
     * @param vector the other vector
     * @return the result
     */
    public Vector minus(Vector vector) {
    	Vector result = (Vector) copy();
    	result.minusEquals(vector);
    	return result;
    }

	/**
	 * Subtract a vector from this vector in place
	 * @param vector the vector to subtract
	 */
	public void minusEquals(Vector vector) {
		for (int i = 0; i < size(); i++) {
			set(i, get(i) - vector.get(i));
		}
	}
    
    /**
     * Get the two norm squared of this vector
     * @return the two norm squared
     */
    public double normSquared() {
        return this.dotProduct(this);
    }

    /**
     * Get the two norm of this vector
     * @return the two norm 
     */
    public double norm() {
        return Math.sqrt(normSquared());
    }


	/**
	 * Make a copy of this vector
	 * @return the copy
	 */
	public Copyable copy() {
		double[] copy = new double[size()];
		for (int i = 0; i < copy.length; i++) {
			copy[i] = get(i);
		}
		return new DenseVector(copy);
	}

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
       Vector v = (Vector) o;
       if (v.size() != size()) {
           return false;
       }
       for (int i = 0; i < v.size(); i++) {
            if (get(i) != v.get(i)) {
                return false;
            }
       }
       return true;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.000000");
        String result = "";
        for (int i = 0; i < size(); i++) {
            result += df.format(get(i));
            if (i + 1 < size()) {
                result += ", ";
            }
        }
        return result;
    }

}


