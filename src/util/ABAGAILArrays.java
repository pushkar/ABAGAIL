package util;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Utility functions
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ABAGAILArrays {
    /** Random number generator */
    public static final Random random = new Random();
    
    /**
     * Print out an array
     * @param array the array to print
     * @param digits the number of digits after
     * the decimal place to use
     */
    public static String toString(double[] array, int digits) {
        String pattern = "0.";
        for (int i = 0; i < digits; i++) {
            pattern += "0";
        }
        pattern += "E00";
        DecimalFormat df = new DecimalFormat(pattern);
        String result = "{";
        for (int i = 0; i < array.length - 1; i++) {
            result += df.format(array[i]) + ", ";
        }
        result += df.format(array[array.length - 1]) + "}";
        return result;
    }
    
    /**
     * Print out an array
     * @param array the array to print
     * @param digits the number of digits after
     * the decimal place to use
     */
    public static String toString(double[][] array, int digits) {
        String pattern = "0.";
        for (int i = 0; i < digits; i++) {
            pattern += "0";
        }
        pattern += "E00";
        DecimalFormat df = new DecimalFormat(pattern);
        String result = "{";
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                result += "\n ";
            } 
            result += " { ";
            for (int j = 0; j < array[i].length - 1; j++) {
                result += df.format(array[i][j]) + ", ";
            }
            result += df.format(array[i][array[i].length - 1]) + " },";
        }
        result += " }";
        return result;
    }
    
    /**
     * Print out an array
     * @param array the array to print
     * @param digits the number of digits after
     * the decimal place to use
     */
    public static String toString(double[] array) {
        return toString(array, 7);
    }
    
    /**
     * Print out an array
     * @param array the array to print
     * @param digits the number of digits after
     * the decimal place to use
     */
    public static String toString(double[][] array) {
        return toString(array, 7);
    }
    
    
    /**
     * Print out an array
     * @param array the array to print
     */
    public static String toString(int[][] array) {
        String result = "{";
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                result += "\n ";
            } 
            result += " { ";
            for (int j = 0; j < array[i].length - 1; j++) {
                result += array[i][j] + ", ";
            }
            result += array[i][array[i].length - 1] + " }";
        }
        result += " }";
        return result;
    }

    /**
     * Print an int array to a string
     * @param data the data
     * @return the string
     */
    public static String toString(int[] array) {
        String result = "{";
        for (int i = 0; i < array.length - 1; i++) {
            result += array[i] + ", ";
        }
        result += array[array.length - 1] + "}";
        return result;
    }

    /**
     * Return a to string for the array
     * @param objects the array
     * @return the string
     */
    public static String toString(Object[] objects) {
        String result = "{";
        for (int i = 0; i < objects.length - 1; i++) {
            result += objects[i] + ", ";
        }
        result += objects[objects.length - 1] + "}";
        return result;
    }   

    
    /**
     * Parition an array in place according to the
     * last element in the array
     * @param a the arary to partition
     * @param s the starting index inclusive
     * @param e the ending index exclusive
     * @return the index now containing the split value
     */
    public static int partition(double[] a, int s, int e) {
        double split = a[e - 1];
        int i = s - 1;
        for (int j = s; j < e - 1; j++) {
            if (a[j] < split) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i+1, e-1);
        return i + 1;
    }
    
    /**
     * Parition an array in place according to the
     * last element in the array
     * @param a the arary to partition
     * @param indices an array of indices
     * @param s the starting index inclusive
     * @param e the ending index exclusive
     * @return the index now containing the split value
     */
    public static int partition(double[] a, int[] indices, int s, int e) {
        double split = a[e - 1];
        int i = s - 1;
        for (int j = s; j < e - 1; j++) {
            if (a[j] < split) {
                i++;
                swap(a, i, j);
                swap(indices, i, j);
            }
        }
        swap(a, i+1, e-1);
        swap(indices, i+1, e-1);
        return i + 1;
    }
    
    /**
     * Perform a random partition
     * @param a the array
     * @param s the starting index inclusive
     * @param e the ending index exclusive
     * @return
     */
    public static int randomPartition(double[] a, int s, int e) {
        int i = random.nextInt(e - s) + s;
        swap(a, i, e-1);
        return partition(a, s, e);
    }
    
    
    /**
     * Perform a random partition
     * @param a the array
     * @param s the starting index inclusive
     * @param e the ending index exclusive
     * @return
     */
    public static int randomPartition(double[] a, int[] indices, int s, int e) {
        int i = random.nextInt(e - s) + s;
        swap(a, i, e-1);
        swap(indices, i, e-1);
        return partition(a, indices, s, e);
    }
    
    /**
     * Select the ith smallest number in an array
     * @param a the array to select out of
     * @param s the starting index
     * @param e the ending index
     * @param i the number to select 
     * @return the ith smallest number
     */
    public static double randomizedSelect(double[] a, int s, int e, int i) {
        if (s == e - 1) {
            return a[s];
        }
        int splitI = randomPartition(a, s, e);
        int orderOfSplit = splitI - s + 1;
        if (orderOfSplit == i) {
           return a[splitI]; 
        } else if (i < orderOfSplit) {
            return randomizedSelect(a, s, splitI, i);
        } else {
            return randomizedSelect(a, splitI + 1, e, i - orderOfSplit);
        }
    }
    
    /**
     * Get the ith smallest number in an array
     * @param a the array
     * @param i the number to select 
     * @return the ith smallest number
     */
    public static double randomizedSelect(double[] a, int i) {
        return randomizedSelect(a, 0, a.length, i);
    }
    
    /**
     * Swap two values in an array
     * @param a the array
     * @param i the first index
     * @param j the second index
     */
    public static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    /**
     * Search an array for a value
     * @param a the array
     * @param v the value you are searching for
     * @return the index of the value or the index
     * of the cloest value greater than it
     */
    public static int search(double[] a, double v) {
        int high = a.length;
        int low = -1;
        while (high - low > 1) {
            int mid = (high + low) / 2;
            if (a[mid] < v) {
                low = mid;
            } else {
                high = mid;
            }
            mid = low + (high - low) / 2;
        }
        return high;
    }
    
    /**
     * Swap two values in an array
     * @param a the array
     * @param i the first index
     * @param j the second index
     */
    public static void swap(double[] a, int i, int j) {
        double temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    /**
     * Perform quicksort on the given array
     * @param a the array to quicksort
     * @param i the start index
     * @param j the end index
     */
    public static void quicksort(double[] a, int i, int j) {
        if (i < j - 1) {
            int splitI = randomPartition(a, i, j);
            quicksort(a, i, splitI);
            quicksort(a, splitI + 1, j);
        }
    }
    
    /**
     * Sort an array
     * @param a the array to sort
     */
    public static void quicksort(double[] a) {
        quicksort(a, 0, a.length);
    }
    
    /**
     * Perform quicksort on the given array
     * @param a the array to quicksort
     * @param indices an array of indices
     * @param i the start index
     * @param j the end index
     */
    public static void quicksort(double[] a, int[] indices, int i, int j) {
        if (i < j - 1) {
            int splitI = randomPartition(a, indices, i, j);
            quicksort(a, indices, i, splitI);
            quicksort(a, indices, splitI + 1, j);
        }
    }
    
    /**
     * Sort an array
     * @param a the array to sort
     * @param indices the indices
     */
    public static void quicksort(double[] a, int[] indices) {
        quicksort(a, indices, 0, a.length);
    }
    
    /**
     * Permute the given array
     * @param a the array to permute
     */
    public static void permute(int[] a) {
        for (int i = a.length-1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(a, i, j);
        }
    }

    /**
     * Permute the given array
     * @param a the array to permute
     */
    public static void permute(double[] a) {
        for (int i = a.length-1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(a, i, j);
        }
    }
    
    /**
     * Get indices of a size
     * @param size the size of the indices to return
     * @return the indices array
     */
    public static int[] indices(int size) {
        int[] indices = new int[size];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        return indices;
    }
    
    /**
     * Get double indices of a size
     * @param size the size of the indices to return
     * @return the indices array
     */
    public static double[] dindices(int size) {
        double[] indices = new double[size];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        return indices;
    }

}
