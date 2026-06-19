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
            ListEditor<T> editor = (ListEditor<T>) ListItem.this.getParent();
            if (editor == null || editor.items == null) {
                return null;
            }
            int idx = getIndex();
            if (idx < 0 || idx >= editor.items.size()) {
                return null;
            }
            return editor.items.get(idx);
        }
    }
}
