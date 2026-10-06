package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class ListEditorRemoveButton extends ListEditorButton {
    public ListEditorRemoveButton(String id, String fmName){
        super(id, fmName);
    }

    public ListEditorRemoveButton(String id, String fmName, String question){
        super(id, fmName, question);
    }

    @Override
    protected void onClick(AjaxRequestTarget target) {
        int idx = getItem().getIndex();
        
        for (int i = idx + 1; i < getItem().getParent().size(); i++){
            ListItem< ? > item = (ListItem< ? >)getItem().getParent().get(i);
            item.setIndex(item.getIndex() - 1);
        }
 
        getList().remove(idx);
        getEditor().updateModel();
        target.add(getEditor().getParent());
        getEditor().remove(getItem());
    
       
    }
}
