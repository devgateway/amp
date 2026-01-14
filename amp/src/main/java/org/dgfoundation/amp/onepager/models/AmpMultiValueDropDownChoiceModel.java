/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Model used to interface with the {@link DropDownChoice} component
 * This model receives a collection model at its constructor that has to have one or no elements. 
 * This is used to simulate the same input model as 
 * the multiselect ampcategory (which receives a collection as its input model) 
 * @see AmpCategorySelectFieldPanel
 * @author mpostelnicu@dgateway.org
 * since Sep 23, 2010
 */
public class AmpMultiValueDropDownChoiceModel<T> implements IModel<T> {

    /**
     * 
     */
    private static final long serialVersionUID = -8226533355734399322L;
    private IModel<Collection<T>> model;

    /**
     * 
     */
    public AmpMultiValueDropDownChoiceModel(IModel<? extends Collection<T>> model2)  {
        if(model2.getObject()==null) throw new RuntimeException(this.getClass()+" cannot construct itself using a null collection");    
        this.model=(IModel<Collection<T>>) model2;
    }

    @Override
    public void detach() {
        model.detach();
    }

    @Override
    public T getObject() {
        if(model.getObject().size()==0) return null;
        if(model.getObject().size()>1) throw new RuntimeException(this.getClass()+" only can receive collections with one or no elements");
        return model.getObject().iterator().next();
    }

    @Override
    public void setObject(T arg0) {
        Collection<T> s=new TreeSet<T>();
        if(arg0!=null) s.add(arg0);
        model.setObject(s);
    }

}
