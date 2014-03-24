package functional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Defines an efficient map collections operation in the style of functional programming.
 *
 * @param <X> input data type.
 * @param <Y> output data type.
 */
public class FunctionalZip<X> implements Iterable<Iterable<X>> {

    protected Iterator<Iterable<X>> zippingIterator;

    /**
     * Defines zip function in terms of map.
     *
     * @param source of input data.
     */
    public FunctionalZip(Iterable<Iterable<X>> source) {
        // Create a list of iterators.
        List<Iterator<X>> iterators = new LinkedList<Iterator<X>>();
        for (Iterable<X> column : source) {
            iterators.add(column.iterator());
        }
        zippingIterator = new ZippingIterator(iterators);;
    }

    /**
     * Defines zip function in terms of map.
     *
     * @param source of input data.
     */
    public FunctionalZip(Iterable<X>... source) {
        // Create a list of iterators.
        List<Iterator<X>> iterators = new LinkedList<Iterator<X>>();
        for (Iterable<X> column : source) {
            iterators.add(column.iterator());
        }
        zippingIterator = new ZippingIterator(iterators);;
    }

    @Override
    public Iterator<Iterable<X>> iterator() {
        return zippingIterator;
    }

    /**
     * Lazily wraps values of the underlying iterator with the specified function.
     *
     * @param <X> input data type.
     * @param <Y> output data type.
     */
    protected class ZippingIterator implements Iterator<Iterable<X>> {
        /**
         * Source iterator.
         */
        private final List<Iterator<X>> iterators;

        /**
         * Define zipping iterator.
         *
         * @param iterators of source iterables.
         */
        public ZippingIterator(List<Iterator<X>> iterators) {
            this.iterators = iterators;
        }

        @Override
        public boolean hasNext() {
            for (Iterator<X> iterator : iterators) {
                if (!iterator.hasNext()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public Iterable<X> next() {
            List<X> tuple = new LinkedList<X>();
            for (Iterator<X> iterator : iterators) {
                tuple.add(iterator.next());
            }
            return tuple;
        }

        @Override
        public void remove() {
            for (Iterator<X> iterator : iterators) {
                iterator.remove();
            }
        }
    }
}
