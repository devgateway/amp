package org.digijava.kernel.services.sync.model;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class IncrementalListDiff<T> {

    private List<T> removed;

    private List<T> saved;

    public IncrementalListDiff(List<T> removed, List<T> saved) {
        this.removed = removed;
        this.saved = saved;
    }

    public List<T> getRemoved() {
        return removed;
    }

    public List<T> getSaved() {
        return saved;
    }
}
