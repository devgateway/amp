package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

import java.util.*;

public abstract class ListEditor<T> extends RepeatingView implements IFormModelUpdateListener{
    private static final long serialVersionUID = 1L;
    public transient List<T> items = new ArrayList<T>();
    IModel<Set<T>> model;
    private transient Comparator<T> comparator;

    public ListEditor(String id, IModel<Set<T>> model){
        this(id, model, null);
    }
    public ListEditor(String id, IModel<Set<T>> model, Comparator<T> comparator){
        super(id, model);
        setOutputMarkupId(true);
        this.model = model;
        this.comparator = comparator;
    }

    protected abstract void onPopulateItem(ListItem<T> item);

    public int getCount(){
        int ret = 0;
        if (items != null)
            ret = items.size();
        return ret;
    }
    
    public void addItem(T value){
        origAddItem(value);
    }
    
    public final void origAddItem(T value){
        
        if(items == null)
            items = new ArrayList<T>();
        items.add(value);
        renderItem(items.size() - 1);
        updateModel();
    }

    protected final void prepareItemsForRendering(){
        items = new ArrayList<T>((Set<T>)model.getObject());
        if (comparator != null)
            Collections.sort(items, comparator);
    }

    protected final void renderItem(int index){
        ListItem<T> li = new ListItem<T>(newChildId(), index);
        add(li);
        onPopulateItem(li);
    }

    protected void editorOnBeforeRender(){
        if (!hasBeenRendered()) {
            prepareItemsForRendering();
            for (int i = 0; i < items.size(); i++){
                renderItem(i);
            }
        }
    }

    protected final void onBeforeRender(){
        super.onBeforeRender();
        editorOnBeforeRender();
    }

    @Override
    public void updateModel(){
        Set<T> set = model.getObject();
        if (set == null)
            set = new LinkedHashSet<T>();
        set.clear();
        if(items != null)
           set.addAll(items);
        //setObject last, in order to work with custom set models
        model.setObject(set);
    }

    public IModel<Set<T>> getModel() {
        return model;
    }
    
}
