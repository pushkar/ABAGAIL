package functional;

/**
 * Interface for a function in a functional programming sense.
 *
 * @param <X> input data type.
 * @param <Y> output data type.
 */
public interface Function<X, Y> {
    /**
     * Applies the function.
     *
     * @param x input
     * @return output of type Y
     */
    public Y apply(X x);
}
