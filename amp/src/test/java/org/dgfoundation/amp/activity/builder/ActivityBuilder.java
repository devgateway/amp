package org.dgfoundation.amp.activity.builder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * Utility class for creating activities for testing Please add more methods if
 * it is needed
 * 
 * @author Viorel Chihai
 *
 */
public class ActivityBuilder {

    private AmpActivityVersion activity;

    public ActivityBuilder() {
        activity = new AmpActivityVersion();
    }

    public ActivityBuilder withId(Long id) {
        activity.setAmpActivityId(id);

        return this;
    }

    public ActivityBuilder withTitle(String title) {
        activity.setName(title);

        return this;
    }

    public ActivityBuilder withCategories(Set<AmpCategoryValue> ampActivityCategories) {
        activity.setCategories(ampActivityCategories);

        return this;
    }

    public ActivityBuilder withFundings(Set<AmpFunding> ampFundings) {
        activity.setFunding(ampFundings);

        return this;
    }

    public ActivityBuilder addFunding(AmpFunding funding) {
        if (activity.getFunding() == null) {
            activity.setFunding(new HashSet<>());
        }

        activity.getFunding().add(funding);

        return this;
    }

    public ActivityBuilder addCategoryValue(AmpCategoryValue acv) {
        if (activity.getCategories() == null) {
            activity.setCategories(new HashSet<>());
        }

        activity.getCategories().add(acv);

        return this;
    }

    public ActivityBuilder withActualCompletionDate(Date completionDate) {
        activity.setActualCompletionDate(completionDate);

        return this;
    }
    
    public ActivityBuilder withAmpId(String ampId) {
        activity.setAmpId(ampId);
        
        return this;
    }

    public AmpActivityVersion getActivity() {
        return activity;
    }
}
