package functional;

import java.util.Iterator;

/**
 * Defines an efficient map collections operation in the style of functional programming.
 *
 * @param <X> input data type.
 * @param <Y> output data type.
 */
public class FunctionalMap<X, Y> implements Iterable<Y> {

    protected Iterator<Y> mappingIterator;

    /**
     * Define functional map.
     *
     * @param function to apply on each element.
     */
    public FunctionalMap(Iterable<X> source, Function<X, Y> function) {
        mappingIterator = new MappingIterator(source.iterator(), function);
    }

    @Override
    public Iterator<Y> iterator() {
        return mappingIterator;
    }

    /**
     * Lazily wraps values of the underlying iterator with the specified function.
     *
     * @param <X> input data type.
     * @param <Y> output data type.
     */
    protected class MappingIterator implements Iterator<Y> {
        /**
         * The function to apply to each element.
         */
        private final Function<X, Y> function;

        /**
         * Source iterator.
         */
        private final Iterator<X> iteratorX;

        /**
         * Define mapping iterator.
         *
         * @param iteratorX source iterator.
         * @param function to apply on each element.
         */
        public MappingIterator(Iterator<X> iteratorX, Function<X, Y> function) {
            this.function = function;
            this.iteratorX = iteratorX;
        }

        @Override
        public boolean hasNext() {
            return iteratorX.hasNext();
        }

        @Override
        public Y next() {
            return function.apply(iteratorX.next());
        }

        @Override
        public void remove() {
            iteratorX.remove();
        }
    }
}
