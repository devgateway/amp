package org.digijava.kernel.services.sync.model;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class ListDiff<T> {

    private List<T> removed;

    private List<T> saved;

    public ListDiff(List<T> removed, List<T> saved) {
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
