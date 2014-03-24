package functional;

/**
 * A monoid is a semigroup with an identity and an associative binary operation.
 * The set of integers under addition forms a monoid.
 * Monoid<Double> provides a neat abstraction for distance, error, similarity, etc.
 *
 * @param <T> type of object on which we define an identity and a binary operation.
 */
public abstract class Monoid<T> {
    /**
     * The identity of the monoid.
     *
     * @return the identity of the monoid.
     */
    public abstract T identity();

    /**
     * The operation on the monoid.
     * Do not mutate arguments.
     *
     * @param a of type T.
     * @param b of type T.
     * @return a combined value of type T.
     */
    public abstract T op(T a, T b);

    /**
     * The operation on the monoid applied in bulk on an iterable of required type.
     * Since Java 1.6 and 1.7 do not optimize tail calls, we must iterate over the parameters
     * while updating a mutable result.
     * Do not mutate arguments.
     *
     * @param iterable containing elements of type T.
     * @return a combined value of type T.
     */
    public T op(Iterable<T> iterable) {
        T result = identity();
        for (T element : iterable) {
            result = op(result, element);
        }
        return result;
    }
}
