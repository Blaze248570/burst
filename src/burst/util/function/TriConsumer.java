package burst.util.function;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Functional interface that accepts three arguments and returns none.
 * <p> Modeled from java {@link BiConsumer}.
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> 
{
    /**
     * Represents an operation that accepts three input arguments and returns no
     * result. 
     */
    void accept(T t, U u, V v);

    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) 
    {
        Objects.requireNonNull(after);

        return (l, m, r) -> {
            accept(l, m, r);
            after.accept(l, m, r);
        };
    }
}
