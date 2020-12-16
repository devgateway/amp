package org.digijava.kernel.util;

import java.util.function.Supplier;

/**
 * A thread safe memoizer that can be invalidated at any time to perform the computation again.
 *
 * @author Octavian Ciubotaru
 */
public class ExpiringMemoizer<T> {

    private final Object lock = new Object();

    private final Supplier<T> loader;

    private T object;

    public ExpiringMemoizer(Supplier<T> loader) {
        this.loader = loader;
    }

    public T get() {
        synchronized (lock) {
            if (object == null) {
                object = loader.get();
            }
            return object;
        }
    }

    public void invalidate() {
        synchronized (lock) {
            object = null;
        }
    }
}
