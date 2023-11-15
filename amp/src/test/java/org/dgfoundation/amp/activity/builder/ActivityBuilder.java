package org.dgfoundation.amp.activity.builder;

import com.google.common.collect.ImmutableSet;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for creating activities for testing Please add more methods if
 * it is needed
 * 
 * @author Viorel Chihai
 *
 */
public class ActivityBuilder {

    AmpActivityVersion activity;

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

    public ActivityBuilder withCategories(AmpCategoryValue... ampActivityCategories) {
        activity.setCategories(ImmutableSet.copyOf(ampActivityCategories));

        return this;
    }

    public ActivityBuilder addLocation(AmpCategoryValueLocations location, Float percentage) {
        AmpActivityLocation activityLocation = new AmpActivityLocation();
        activityLocation.setLocation(location);
        activityLocation.setLocationPercentage(percentage);

        return addLocation(activityLocation);
    }

    public ActivityBuilder addLocation(AmpActivityLocation activityLocation) {
        activityLocation.setActivity(activity);
        activity.getLocations().add(activityLocation);

        return this;
    }

    public ActivityBuilder withFundings(Set<AmpFunding> ampFundings) {
        activity.setFunding(ampFundings);

        return this;
    }

    public ActivityBuilder addFunding(AmpFunding funding) {
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

    public ComponentBuilder buildComponent() {
        AmpComponent component = new AmpComponent();
        component.setActivity(activity);
        return new ComponentBuilder(this, component);
    }

    public ActivityBuilder addOrgRole(AmpRole role, AmpOrganisation organisation) {
        return addOrgRole(role, organisation, 100f);
    }

    public ActivityBuilder addOrgRole(AmpRole role, AmpOrganisation organisation, Float percentage) {
        AmpOrgRole orgRole = new AmpOrgRole();
        orgRole.setRole(role);
        orgRole.setOrganisation(organisation);
        orgRole.setActivity(activity);
        orgRole.setPercentage(percentage);
        activity.getOrgrole().add(orgRole);
        return this;
    }

    public ActivityBuilder withDraft(boolean draft) {
        activity.setDraft(draft);
        return this;
    }
    
    public ActivityBuilder withGroup(AmpActivityGroup group) {
        activity.setAmpActivityGroup(group);
        return this;
    }
    
    public ActivityBuilder withActivityCreator(AmpTeamMember creator) {
        activity.setActivityCreator(creator);
        return this;
    }
    
    
    public ActivityBuilder withApprovalStatus(ApprovalStatus approvalStatus) {
        activity.setApprovalStatus(approvalStatus);
        return this;
    }
    
    public ActivityBuilder withTeam(AmpTeam ampTeam) {
        activity.setTeam(ampTeam);
        return this;
    }

    public ActivityBuilder withMultiStakeholderPartnership(boolean multiStakeholderPartnership) {
        activity.setMultiStakeholderPartnership(multiStakeholderPartnership);
        return this;
    }

	public ActivityBuilder addRegionalFunding(int transactionType, AmpCategoryValue adjustmentType,
            Date transactionDate, Double transactionAmount, AmpCurrency currency, AmpCategoryValueLocations location) {

        AmpRegionalFunding funding = new AmpRegionalFunding();
        funding.setTransactionType(transactionType);
        funding.setAdjustmentType(adjustmentType);
        funding.setTransactionDate(transactionDate);
        funding.setTransactionAmount(transactionAmount);
        funding.setCurrency(currency);
        funding.setRegionLocation(location);

        activity.getRegionalFundings().add(funding);
        return this;
    }
}
