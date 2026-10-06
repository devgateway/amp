package org.dgfoundation.amp.algo;

import java.util.Collections;
import java.util.List;

/**
 * an opaque Comparable token
 * @author Dolghier Constantin
 *
 * @param <K>
 */
@SuppressWarnings("rawtypes")
public class ComparableList<K extends Comparable> implements Comparable<ComparableList<K>> {

    final List<K> data;
    public ComparableList(List<K> in) {
        this.data = Collections.unmodifiableList(in);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(ComparableList<K> o) {
        int minSz = Math.min(this.data.size(), o.data.size());
        for(int i = 0; i < minSz; i++) {
            int delta = this.data.get(i).compareTo(o.data.get(i));
            if (delta != 0) 
                return delta;
        }
        return Integer.compare(this.data.size(), o.data.size());
    }
    
}
