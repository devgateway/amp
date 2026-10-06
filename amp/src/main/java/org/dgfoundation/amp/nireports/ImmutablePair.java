package org.dgfoundation.amp.nireports;

/**
 * an immutable Tuple(2) of unrelated values, conventionally called "k" and "v". <br />
 * null values are not supported for either.
 * {@link #equals(Object)} and {@link #hashCode()} are defined in terms of both {@link #k} and {@link #v}
 * 
 * @author Dolghier Constantin
 */
public class ImmutablePair<K, V> {
    public final K k;
    public final V v;
    
    public final int __hashCode;
    
    public ImmutablePair(K k, V v) {
        NiUtils.failIf(k == null, "k should not be null");
        NiUtils.failIf(v == null, "v should not be null");
        
        this.k = k;
        this.v = v;
        
        __hashCode = 19 * this.k.hashCode() + this.v.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("(k = %s, v = %s)", k, v);
    }
    
    @Override
    public boolean equals(Object oth) {
        if (!(oth instanceof ImmutablePair))
            return false;
        
        ImmutablePair<K, V> other = (ImmutablePair<K, V>) oth;
        return this.k.equals(other.k) && this.v.equals(other.v); 
    }
    
    @Override
    public int hashCode() {
        return __hashCode;
    }
}
