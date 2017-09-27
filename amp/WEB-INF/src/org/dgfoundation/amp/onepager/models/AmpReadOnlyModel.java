package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.AbstractReadOnlyModel;

public class AmpReadOnlyModel<T> extends AbstractReadOnlyModel<T> {
    private transient T object;
    
    public AmpReadOnlyModel(T o) {
        this.object = o;
    }

    @Override
    public T getObject() {
        return object;
    }

}
