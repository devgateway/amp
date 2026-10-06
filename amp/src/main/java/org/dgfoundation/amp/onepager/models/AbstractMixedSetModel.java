/*
 * Copyright (c) 2013 Development Gateway (www.developmentgateway.org)
 */

package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Model Used for sets that have mixed values that
 * are distinguishable only by a field
 *
 * @author aartimon@developmentgateway.org
 * @since 30 June 2013
 */
public abstract class AbstractMixedSetModel<T> implements IModel<Set<T>> {


    private final IModel<Set<T>> originalModel;

    public AbstractMixedSetModel(IModel<Set<T>> model) {
        this.originalModel = model;
    }

    @Override
    public Set<T> getObject() {
        if (originalModel.getObject() == null)
            return null;

        Set<T> ret = new LinkedHashSet<T>();
        //retrieve from the original model only the items that respect the condition
        for (T item: originalModel.getObject()){
            if (condition(item))
                ret.add(item);
        }
        return ret;
    }

    @Override
    public void setObject(Set<T> object) {
        Set<T> set = originalModel.getObject();
        if (set != null){
            //remove from the original model the old items
            for (Iterator<T> iterator = originalModel.getObject().iterator(); iterator.hasNext(); ) {
                T item = iterator.next();
                if (condition(item))
                    iterator.remove();
            }
        }
        else{
            set = newSetInstance();
        }
        set.addAll(object);
        originalModel.setObject(set);
    }

    //gives the ability to use various set types
    protected Set<T> newSetInstance() {
        return new HashSet<T>(); //default implementation
    }

    @Override
    public void detach() {
    }

    public abstract boolean condition(T item);
}
