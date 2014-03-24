package shared;

import java.util.Iterator;

import functional.Function;
import functional.FunctionalMap;
import functional.FunctionalZip;
import functional.Monoid;

/**
 * An abstract type to represent distance, error, similarity, etc. between two data sets.
 */
public abstract class MonoidOfDoublesForDataSets extends Monoid<Double> {

    /**
     * Calculate the required metric between two data sets.
     * @param a the first data set.
     * @param b the second data set.
     * @return the required metric.
     */
    public double value(DataSet a, DataSet b) {
        @SuppressWarnings("unchecked")
        Iterable<Iterable<Instance>> zippedDataSets = new FunctionalZip<Instance>(a, b);
        Function<Iterable<Instance>, Double> combine = new Function<Iterable<Instance>, Double>() {
            @Override
            public Double apply(Iterable<Instance> inputs) {
                Iterator<Instance> iterator = inputs.iterator();
                return value(iterator.next(), iterator.next());
            }
        };
        return this.op(new FunctionalMap<Iterable<Instance>, Double>(zippedDataSets, combine));
    }

    /**
     * Measure the required metric between two vectors.
     * @param a the first vector.
     * @param b the second vector.
     * @return the required metric.
     */
    public abstract double value(Instance a, Instance b);

    @Override
    public Double identity() {
        return 0.0;
    }

    /**
     * Defines how to combine two double-valued metrics from two pairs of data.
     * Here we simply add them.
     */
    @Override
    public Double op(Double a, Double b) {
        if (null == a || null == b) {
            throw new IllegalArgumentException(
                    "Combination operation can not be performed on null values.");
        }
        return a + b;
    }
}
