package org.dgfoundation.amp.algo;

import java.util.Set;

/**
 * interface which does a "wave" (typically database selects)
 * generally wave(in) shouldn't contain elements from in
 * also wave(wave(...wave(in))) shouldn't either
 * @author Dolghier Constantin
 *
 */
public interface Waver<K> {
    public Set<K> wave(Set<K> input);
}
