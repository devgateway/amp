/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryFieldPanel;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Model for retrieving and writing {@link AmpCategoryValue}S of a specific {@link AmpCategoryClass} to a collection that can have values from multiple {@link AmpCategoryClass}S
 * The {@link AmpCategoryClass} filtering is done by using the {@link AmpCategoryClass#getKeyName()}  
 * Upon writing, the previous {@link AmpCategoryValue}S of that Class are retained and the fresh set received is added
 * @see AmpActivityVersion#getCategories()
 * @author mpostelnicu@dgateway.org
 * since Sep 24, 2010
 */
public class AmpCategoryValueByKeyModel implements
        IModel<Set<AmpCategoryValue>> {

    /**
     * 
     */
    private static final long serialVersionUID = -4083598609567863027L;
    private IModel<Set<AmpCategoryValue>> model;
    private String categoryKey;
    private transient Set<AmpCategoryValue> set = null;

    /**
     * 
     */
    public AmpCategoryValueByKeyModel(IModel<Set<AmpCategoryValue>> model, String categoryKey) {
        this.model=model;
        this.categoryKey= AmpCategoryFieldPanel.getAlternateKey(Session.get(), categoryKey);
    }

    @Override
    public void detach() {
        model.detach();
    }

    @Override
    public Set<AmpCategoryValue> getObject() {
        if (set == null)
            set = new TreeSet<AmpCategoryValue>();
        else
            set.clear();
        if(model.getObject()!=null) 
            set.addAll(CategoryManagerUtil.getAmpCategoryValuesFromListByKey(categoryKey, model.getObject()));
        return set;
    }

    @Override
    public void setObject(Set<AmpCategoryValue> newValues) {
        if (model.getObject() != null){
            Collection oldValues = CategoryManagerUtil.getAmpCategoryValuesFromListByKey(categoryKey, model.getObject());
            model.getObject().removeAll(oldValues);
        }
        else{
            model.setObject(new HashSet<AmpCategoryValue>());
        }
        model.getObject().addAll(newValues);
    }

}
