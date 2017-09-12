package org.dgfoundation.amp.activity.builder;

import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class CategoryValueBuilder {

    private AmpCategoryValue categoryValue;

    public CategoryValueBuilder() {
        categoryValue = new AmpCategoryValue();
    }
    
    public CategoryValueBuilder withId(Long id) {
        categoryValue.setId(id);
        
        return this;
    }

    public CategoryValueBuilder withLabel(String label) {
        categoryValue.setValue(label);

        return this;
    }

    public CategoryValueBuilder withValue(String value) {
        categoryValue.setValue(value);

        return this;
    }

    public CategoryValueBuilder withIndex(int index) {
        categoryValue.setIndex(index);

        return this;
    }

    public CategoryValueBuilder withCategoryClass(AmpCategoryClass categoryClass) {
        categoryValue.setAmpCategoryClass(categoryClass);

        return this;
    }

    public AmpCategoryValue getCategoryValue() {
        return categoryValue;
    }

}
