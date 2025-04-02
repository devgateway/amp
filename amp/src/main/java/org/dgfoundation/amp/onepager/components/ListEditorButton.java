package org.dgfoundation.amp.onepager.components;

import java.util.List;

import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.fields.AmpLinkField;

public abstract class ListEditorButton extends AmpLinkField {
    private static final long serialVersionUID = 1L;
    private transient ListItem< ? > parent;

    public ListEditorButton(String id, String fmName, String question){
        super(id, fmName, new Model<String>(question));
    }

    public ListEditorButton(String id, String fmName){
        super(id, fmName);
    }

    protected final ListItem<?> getItem(){
        if (parent == null){
            parent = findParent(ListItem.class);
        }
        return parent;
    }

    protected final List<?> getList(){
        return getEditor().items;
    }

    protected final ListEditor< ? > getEditor(){
        return (ListEditor< ? >)getItem().getParent();
    }

    protected void onDetach(){
        parent = null;
        super.onDetach();
    }
}

