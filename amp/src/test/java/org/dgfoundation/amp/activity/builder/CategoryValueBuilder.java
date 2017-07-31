package org.dgfoundation.amp.activity.builder;

import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class CategoryValueBuilder {

    AmpCategoryValue caetgoryValue;

    public CategoryValueBuilder() {
        caetgoryValue = new AmpCategoryValue();
    }

    public CategoryValueBuilder withLabel(String label) {
        caetgoryValue.setValue(label);

        return this;
    }

    public CategoryValueBuilder withValue(String value) {
        caetgoryValue.setValue(value);

        return this;
    }

    public CategoryValueBuilder withIndex(int index) {
        caetgoryValue.setIndex(index);

        return this;
    }

    public CategoryValueBuilder withCategoryClass(AmpCategoryClass categoryClass) {
        caetgoryValue.setAmpCategoryClass(categoryClass);

        return this;
    }

    public AmpCategoryValue getCategoryValue() {
        return caetgoryValue;
    }
}
