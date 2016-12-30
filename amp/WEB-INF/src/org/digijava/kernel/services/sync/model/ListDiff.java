package org.digijava.kernel.services.sync.model;

import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class ListDiff<T> extends IncrementalListDiff<T> {

    @JsonProperty
    private boolean incremental;

    public ListDiff(List<T> removed, List<T> saved) {
        super(removed, saved);
        this.incremental = true;
    }

    public ListDiff() {
        super(Collections.emptyList(), Collections.emptyList());
        this.incremental = false;
    }
}
