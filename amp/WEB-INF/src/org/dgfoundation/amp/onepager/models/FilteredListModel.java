package org.dgfoundation.amp.onepager.models;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

public abstract class FilteredListModel<T> extends LoadableDetachableModel<List<T>> {
    private static final long serialVersionUID = 1L;
    
    private IModel<List<? extends T>> list;

    @Override
    protected void onDetach() {
        list.detach();
    }

    public FilteredListModel(IModel<List<? extends T>> inner) {
        this.list = inner;
    }

    public FilteredListModel(List<? extends T> inner) {
        this.list = Model.ofList(inner);
    }

    @Override
    protected final List<T> load() {
        List<? extends T> input = list.getObject();
        List<T> result = input.stream()
                        .filter(o -> accept(o))
                        .collect(Collectors.toList());
        
        return result;
    }

    protected abstract boolean accept(T o);
}
