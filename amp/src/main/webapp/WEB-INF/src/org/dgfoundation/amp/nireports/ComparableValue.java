package org.dgfoundation.amp.nireports;

import java.util.Objects;

/**
 * a class which pairs together a value (payload) and a comparable token (used for ordering values only). Useful for when it is not practical to specify a custom comparator in a containing collection
 * @author Dolghier Constantin
 *
 * @param <V> the type of the payload
 */
@SuppressWarnings("rawtypes")
public class ComparableValue<V> implements Comparable<ComparableValue<V>> {
    
    /** the payload */
    protected final V value;
    protected final Comparable comparable;
    
    public ComparableValue(V value, Comparable comp) {
        Objects.requireNonNull(comp);
        this.value = value;
        this.comparable = comp;
    }
    
    public V getValue() {
        return value;
    }
    
    public Comparable<?> getComparable() {
        return comparable;
    }
    
    @Override
    public int compareTo(ComparableValue<V> oth) {
        return this.comparable.compareTo(oth.comparable);
    }
    
    @Override
    public int hashCode() {
        return comparable.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ComparableValue))
            return false;
        return comparable.equals(((ComparableValue) obj).comparable);
    }

    public String toString() {
        return String.format("(<%s>: %s)", value, comparable);
    }
}
