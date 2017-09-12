package org.dgfoundation.amp.activity.builder;

import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class CategoryClassBuilder {

    private AmpCategoryClass categoryClass;

    public CategoryClassBuilder() {
        categoryClass = new AmpCategoryClass();
    }

    public CategoryClassBuilder withKey(String key) {
        categoryClass.setKeyName(key);

        return this;
    }

    public AmpCategoryClass getCategoryClass() {
        return categoryClass;
    }
}
