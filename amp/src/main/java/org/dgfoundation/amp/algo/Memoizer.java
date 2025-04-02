package org.dgfoundation.amp.algo;

import java.util.function.Supplier;

/**
 * a lazy synchronized memoizer for an expensive-to-calculate value
 * @author Dolghier Constantin
 *
 * @param <V>
 */
public class Memoizer<V> {
    final protected Supplier<V> src;
    protected V value;
    protected boolean calculated = false;
    
    public Memoizer(Supplier<V> src) {
        this.src = src;
    }
    
    public synchronized V get() {
        if (!calculated) {
            value = src.get();
            calculated = true;
        }
        return value;
    }
}
