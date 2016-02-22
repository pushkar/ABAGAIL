package func;

/**
 * A supplier for creating {@link FunctionApproximater} instances used to
 * pass customized classifiers into {@link AdaBoostClassifier}
 */
public interface FunctionApproximaterSupplier {
    FunctionApproximater get();
}
