package org.digijava.kernel.services.sync.model;

import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class TranslationsDiff extends ListDiff<String> {

    @JsonProperty
    private boolean incremental;

    public TranslationsDiff(List<String> removed, List<String> saved) {
        super(removed, saved);
        this.incremental = true;
    }

    public TranslationsDiff() {
        super(Collections.emptyList(), Collections.emptyList());
        this.incremental = false;
    }
}
