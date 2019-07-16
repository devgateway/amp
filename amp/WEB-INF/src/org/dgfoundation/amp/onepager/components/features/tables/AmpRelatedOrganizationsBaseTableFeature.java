/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerMessages;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpSearchOrganizationComponent;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpMaxSizeCollectionValidationField;
import org.dgfoundation.amp.onepager.components.fields.AmpMinSizeCollectionValidationField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.events.FundingOrgListUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author aartimon@dginternational.org
 * since Oct 26, 2010
 */
public class AmpRelatedOrganizationsBaseTableFeature extends AmpFormTableFeaturePanel<AmpActivityVersion, AmpOrgRole> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final AmpComponentPanel addFundingItemAutomatically;
    protected AmpUniqueCollectionValidatorField<AmpOrgRole> uniqueCollectionValidationField;
    protected AmpPercentageCollectionValidatorField<AmpOrgRole> percentageValidationField;
    protected AmpMinSizeCollectionValidationField<AmpOrgRole> minSizeCollectionValidationField;
    protected AmpMaxSizeCollectionValidationField<AmpOrgRole> maxSizeCollectionValidationField;
    protected final IModel<ListView<AmpOrgRole>> list = new Model<ListView<AmpOrgRole>>();
    protected IModel<List<AmpOrgRole>> listModel;
    protected IModel<Set<AmpOrgRole>> setModel;
    private TransparentWebMarkupContainer updateColSpan1;
    private TransparentWebMarkupContainer updateColSpan2;
    private AmpDonorFundingFormSectionFeature donorFundingSection;
    private AmpSearchOrganizationComponent<String> searchOrganization;
    protected boolean orgAddedOrRemoved = false;
    
    public void setDefaultOrgGroup(AmpOrgGroup orgGroup) {
        searchOrganization.setDefaultOrgGroup(orgGroup);
    }
    
    public AmpSearchOrganizationComponent<String> getSearchOrganization(){
        return searchOrganization;
    }
    
    /**
     * Override to notify of newly added roles, if you need to refresh/change other sections of the form
     * @param target 
     * @param ampOrgRole
     */
    public void roleAdded(AjaxRequestTarget target, AmpOrgRole ampOrgRole) {
        orgAddedOrRemoved = true;
        updateSearchVisibility(target);
        addFundingAutomatically(target, ampOrgRole);
    }

    /**
     * Override to notify of newly removed roles, if you need to refresh/change other sections of the form
     * @param ampOrgRole
     */
    public void roleRemoved(AjaxRequestTarget target,AmpOrgRole ampOrgRole) {
        orgAddedOrRemoved = true;
        updateSearchVisibility(target);
        removeFundingAutomatically(target, ampOrgRole);
    }

    private void addFundingAutomatically(AjaxRequestTarget target, AmpOrgRole ampOrgRole) {
        if (addFundingItemAutomatically.isVisible()){
            donorFundingSection.getOrgRoleSelector().getOrgSelect().getModel().setObject(ampOrgRole.getOrganisation());
            donorFundingSection.getOrgRoleSelector().getRoleSelect().getModel().setObject(ampOrgRole.getRole());
            donorFundingSection.addItemToList(ampOrgRole.getOrganisation(), ampOrgRole);
            if (ampOrgRole.getRole().getRoleCode().equals(Constants.FUNDING_AGENCY)) {
                donorFundingSection.setOriginalSearchOrganizationSelector(searchOrganization);
            }
            target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(donorFundingSection));
            //If we are in tabView
            if (donorFundingSection.isTabsView()) {
                target.appendJavaScript("switchTabs();");
            }

            target.add(donorFundingSection);
        }
    }
    private void removeFundingAutomatically(AjaxRequestTarget target, AmpOrgRole ampOrgRole) {
        if (addFundingItemAutomatically.isVisible()) {
            Set<AmpFunding> set = donorFundingSection.getFundingModel().getObject();
            Iterator<AmpFunding> it = set.iterator();
            while (it.hasNext()){
                AmpFunding funding = it.next();
                AmpOrganisation fundingOrg = funding.getAmpDonorOrgId();
                if (fundingOrg.getAmpOrgId().equals(ampOrgRole.getOrganisation().getAmpOrgId())){
                    it.remove();
                }
            }
            donorFundingSection.updateFundingGroups(ampOrgRole, target);
            target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(donorFundingSection));
            if (donorFundingSection.isTabsView()) {
                target.appendJavaScript("switchTabs();");
            }
            target.add(donorFundingSection);
        }
    }
    
    private void updateSearchVisibility() {
        updateSearchVisibility(null);
    }
    
    private boolean listContainsDonorOrg(List<AmpOrgRole> roles) {
        for (AmpOrgRole role : roles) {
            if (role.getRole().getRoleCode().equals(Constants.FUNDING_AGENCY))
                return true;
        }
        return false;
    }
    
    private void updateSearchVisibility(AjaxRequestTarget target ) {
        if (maxSizeCollectionValidationField.isVisible()){
            List<AmpOrgRole> tmpList = listModel.getObject();
            if (tmpList != null && tmpList.size() > 0 ) {
                searchOrganization.setVisibilityAllowed(false);
                if (this.getId().equals("donorOrganization")) {
                    donorFundingSection.setSearchOrgsComponentVisibility(false);
                }
                    
            }
            else{
                searchOrganization.setVisibilityAllowed(true);
                if (this.getId().equals("donorOrganization")) {
                    donorFundingSection.setSearchOrgsComponentVisibility(true);
                }
            }
            if(target!=null){
                target.add(searchOrganization);
            }
        }
    }

    /**
     * @param id
     * @param fmName
     * @param am
     * @throws Exception
     */
    protected AmpRelatedOrganizationsBaseTableFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am, final String roleName, AmpDonorFundingFormSectionFeature donorFundingSection,
            final List<AmpOrgGroup> availableOrgGroupChoices) throws Exception {
        super(id, am, fmName);
        setModel = new PropertyModel<Set<AmpOrgRole>>(am,"orgrole");
        this.donorFundingSection=donorFundingSection;
        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpOrgRole>());
        
        
        final AmpRole specificRole = DbUtil.getAmpRole(roleName);

        listModel = new AbstractReadOnlyModel<List<AmpOrgRole>>() {
            private static final long serialVersionUID = 3706184421459839210L;

            @Override
            public List<AmpOrgRole> getObject() {
                ArrayList<AmpOrgRole> ret = new ArrayList<AmpOrgRole>();
                Set<AmpOrgRole> allOrgRoles = setModel.getObject();
                Set<AmpOrgRole> specificOrgRoles = new HashSet<AmpOrgRole>();  
                
                if(allOrgRoles != null){
                    for(AmpOrgRole ampOrgRole:allOrgRoles){
                        if (ampOrgRole.getRole().getAmpRoleId().compareTo(specificRole.getAmpRoleId()) == 0)
                            specificOrgRoles.add(ampOrgRole);
                    }
                }
                
                if (allOrgRoles == null)
                    return ret;
                
                ret = new ArrayList<AmpOrgRole>(specificOrgRoles);
                Collections.sort(ret, AmpOrgRole.BY_ACRONYM_AND_NAME_COMPARATOR);
                return ret;
            }
        };
        
        updateColSpan1 = new TransparentWebMarkupContainer("updateColSpan1");
        add(updateColSpan1);
        updateColSpan2 = new TransparentWebMarkupContainer("updateColSpan2");
        add(updateColSpan2);

        
        uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpOrgRole>(
                "uniqueOrgsValidator", listModel, "Unique Orgs Validator") {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getIdentifier(AmpOrgRole t) {
                return t.getOrganisation().getName();
            }
        };
        add(uniqueCollectionValidationField);

        minSizeCollectionValidationField = new AmpMinSizeCollectionValidationField<AmpOrgRole>("minSizeValidator", listModel, "Required Validator"){
            @Override
            protected void onConfigure() {
                super.onConfigure();
                reqStar.setVisible(isVisible());
            }
        };
        add(minSizeCollectionValidationField);

        maxSizeCollectionValidationField = new AmpMaxSizeCollectionValidationField<AmpOrgRole>("maxSizeValidator", listModel, "Max Size Validator");
        maxSizeCollectionValidationField.setOutputMarkupPlaceholderTag(true);
        add(maxSizeCollectionValidationField);
        
        addFundingItemAutomatically = new AmpComponentPanel("addFundingItemAutomatically", "Add Funding Item Automatically") {};
        add(addFundingItemAutomatically);


        WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
        add(wmc);
        AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
        wmc.add(iValidator);
        percentageValidationField = new AmpPercentageCollectionValidatorField<AmpOrgRole>(
                "relOrgPercentageTotal", listModel, "percentage") {
            private static final long serialVersionUID = 1L;

            @Override
            public Number getPercentage(AmpOrgRole item) {
                return item.getPercentage();
            }
        };
        percentageValidationField.setIndicatorAppender(iValidator);
        add(percentageValidationField);

        add(new AmpDividePercentageField<AmpOrgRole>("dividePercentage", "Divide Percentage", "Divide Percentage", setModel, list){
            private static final long serialVersionUID = 1L;

            @Override
            public void setPercentage(AmpOrgRole loc, int val) {
                loc.setPercentage((float) val);
            }

            @Override
            public int getPercentage(AmpOrgRole loc) {
                return (int)((double)loc.getPercentage());
            }

            @Override
            public boolean itemInCollection(AmpOrgRole item) {
                return item.getRole().getAmpRoleId().compareTo(specificRole.getAmpRoleId()) == 0;
            //  return true; //all items displayed in the same list
            }

        });

        final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs = new AmpAutocompleteFieldPanel<AmpOrganisation>("searchAutocomplete","Search Organizations",true,true,AmpOrganisationSearchModel.class,id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected String getChoiceValue(AmpOrganisation choice) {
                return DbUtil.filter(choice.getName());
            }
            
            @Override
            protected boolean showAcronyms() {
                return true;
            }
            
            @Override
            protected String getAcronym(AmpOrganisation choice) {
                return choice.getAcronym();
            }

            @Override
            public void onSelect(AjaxRequestTarget target, AmpOrganisation choice) {
                send(getPage(), Broadcast.BREADTH, new FundingOrgListUpdateEvent(target));

                AmpOrgRole ampOrgRole = new AmpOrgRole();
                ampOrgRole.setOrganisation(choice);
                ampOrgRole.setActivity(am.getObject());
                ampOrgRole.setRole(specificRole);
                //ampOrgRole.setAmpOrgRoleId(choice.getAmpOrgId());
                if (percentageValidationField.isVisible()) {
                    if(list.getObject().size()>0)
                        ampOrgRole.setPercentage(0f);
                    else
                        ampOrgRole.setPercentage(100f);
                }
                
                if (setModel.getObject() == null)
                    setModel.setObject(new HashSet<AmpOrgRole>());
                
                list.getObject().removeAll();
                target.add(list.getObject().getParent());
                
                uniqueCollectionValidationField.reloadValidationField(target);
                minSizeCollectionValidationField.reloadValidationField(target);
                maxSizeCollectionValidationField.reloadValidationField(target);
                
                if(!existOrganization(setModel.getObject(), ampOrgRole,specificRole)){
                    setModel.getObject().add(ampOrgRole);
                    roleAdded(target, ampOrgRole);
                } else {
                    String translatedMessage = TranslatorUtil.getTranslation("Organization already selected.");
                    target.appendJavaScript("alert ('"+translatedMessage+"')");
                    return;
                }                                       
            }

            @Override
            public Integer getChoiceLevel(AmpOrganisation choice) {
                return null;
            }
        };
        searchOrgs.add(UpdateEventBehavior.of(FundingOrgListUpdateEvent.class));
        searchOrganization = new AmpSearchOrganizationComponent<String>("search", new Model<String> (), "Search Organizations", searchOrgs, availableOrgGroupChoices){
            protected void onConfigure() {
                super.onConfigure();
                updateSearchVisibility();
            }
        };
        
        add(searchOrganization);
    }
//  private boolean existOrganization(Set<AmpOrgRole> set, AmpOrgRole ampOrgRole) {
//      return existOrganization( set, ampOrgRole,null);
//  }
    private boolean existOrganization(Set<AmpOrgRole> set, AmpOrgRole ampOrgRole,AmpRole newRole) {
        boolean ret = false;
        Iterator<AmpOrgRole> iter = set.iterator();
        while(iter.hasNext()) {
            AmpOrgRole or=iter.next();
            if(or.getOrganisation().getAmpOrgId().equals(ampOrgRole.getOrganisation().getAmpOrgId())) {
                if(newRole.equals(or.getRole()) || "false".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ALLOW_SAME_ORG_IN_DIFF_ROLES))){
                    ret = true;
                }
            }
        }
        return ret;
    }
    
    @Override
    public void setTitleHeaderColSpan(Integer colspan) {
        super.setTitleHeaderColSpan(colspan);
        updateColSpan1.add(new AttributeModifier("colspan", new Model<Integer>(colspan)));
        updateColSpan2.add(new AttributeModifier("colspan", new Model<Integer>(colspan)));
    }

    protected void onDeleteOrg(AjaxRequestTarget target, ListItem<AmpOrgRole> item, IModel<AmpActivityVersion> am) {
        AmpActivityVersion activity = am.getObject();
        AmpOrgRole ampOrgRole = item.getModelObject();
        AmpOrganisation org = ampOrgRole.getOrganisation();
        
        boolean hasFundings = ActivityUtil.hasOrgRoleFundingsInActivity(activity, ampOrgRole);
        if (hasFundings) {
            String message = TranslatorUtil.getTranslation(OnePagerMessages.HAS_FUNDINGS_ALERT_MSG);
            target.appendJavaScript(OnePagerUtil.createJSAlert(message));
            return;
        }
        
        boolean hasComponentFundings = ActivityUtil.hasOrgComponentFundingsInActivity(activity, org);
        if (hasComponentFundings) {
            String message = TranslatorUtil.getTranslation(OnePagerMessages.HAS_COMP_FUNDINGS_ALERT_MSG);
            target.appendJavaScript(OnePagerUtil.createJSAlert(message));
            return;
        }
        
        setModel.getObject().remove(ampOrgRole);
        uniqueCollectionValidationField.reloadValidationField(target);
        
        MarkupContainer listParent = list.getObject().getParent();
        target.add(listParent);
        
        //do not move the roleRemoved method above the listParent refresh
        roleRemoved(target, ampOrgRole);
        list.getObject().removeAll();
    }
}
