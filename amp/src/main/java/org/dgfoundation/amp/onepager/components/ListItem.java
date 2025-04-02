package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class ListItem<T> extends Item<T>{
    public ListItem(String id, int index){
        super(id, index);
        setDefaultModel(new ListItemModel());
    }

    private class ListItemModel extends AbstractReadOnlyModel<T>{
        public T getObject(){
            return ((ListEditor<T>)ListItem.this.getParent())
            .items.get(getIndex());
        }
    }
}
