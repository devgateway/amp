/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.*;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingGroupFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpOverviewSection;
import org.dgfoundation.amp.onepager.events.DonorFundingRolesEvent;
import org.dgfoundation.amp.onepager.events.GPINiSurveyListUpdateEvent;
import org.dgfoundation.amp.onepager.events.OrganisationUpdateEvent;
import org.dgfoundation.amp.onepager.models.AmpFundingGroupModel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

import java.util.*;
import java.util.function.Consumer;

/**
 * The donor funding section of the activity form. Includes selecting an org,
 * adding funding item, showing already added items
 * 
 * 
 * @author mpostelnicu@dgateway.org since Nov 3, 2010
 */
public class AmpDonorFundingFormSectionFeature extends
        AmpFormSectionFeaturePanel implements AmpRequiredComponentContainer {
    private static final long serialVersionUID = 1L;
    private Map<AmpOrgRole, AmpFundingGroupFeaturePanel> listItems = new TreeMap<AmpOrgRole, AmpFundingGroupFeaturePanel>();
    
    protected ListEditor<AmpOrgRole> orgRolelist;
    protected ListEditor<AmpOrgRole> tabsList;
    
    private IModel<Set<AmpOrgRole>> orgRoleModel;
    private IModel<Set<AmpOrgRole>> fundingOrgRoleModel;
    private AbstractReadOnlyModel<List<AmpFunding>> listModel;
    private PropertyModel<Set<AmpFunding>> fundingModel;
    private AmpAjaxLinkField addNewFunding;
    private AmpOrgRoleSelectorComponent orgRoleSelector;
    private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>>();
    private AmpSearchOrganizationComponent<String> originalSearchOrganizationSelector;
    private final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs;
    private final AmpSearchOrganizationComponent<String> searchOrganizationComponent;
    private boolean isTabsView;

    private final static String[] ACTIVITY_ROLE_FILTER = { Constants.FUNDING_AGENCY };
    private final static String[] SSC_ROLE_FILTER = { Constants.FUNDING_AGENCY,
            Constants.EXECUTING_AGENCY, Constants.BENEFICIARY_AGENCY };
    public final static String[] FUNDING_FLOW_ROLE_FILTER = new String[] {
            Constants.IMPLEMENTING_AGENCY, Constants.EXECUTING_AGENCY,
            Constants.BENEFICIARY_AGENCY };
    private final ValueWrapper<Boolean> isOverviewVisible=new ValueWrapper<Boolean>(false);

    public ListEditor<AmpOrgRole> getTabsList() {
        return tabsList;
    }

    public String[] getRoleFilter() {
        String[] roleFilter = ACTIVITY_ROLE_FILTER;
        if (ActivityUtil.ACTIVITY_TYPE_SSC
                .equals(((AmpAuthWebSession) getSession()).getFormType()))
            roleFilter = SSC_ROLE_FILTER;
        return roleFilter;
    }

    public void switchOrg(ListItem item, AmpFunding funding, AmpOrganisation newOrg, 
            AmpRole role, AjaxRequestTarget target) {

        AmpFundingGroupFeaturePanel existingFundGrp = getExistingFundingGroup(funding);
        
        if (existingFundGrp != null) {
            existingFundGrp.getList().remove(item);
            existingFundGrp.getList().updateModel();
        }

        funding.setAmpDonorOrgId(newOrg);
        funding.setSourceRole(role);
        AmpOrgRole ampOrgRole = findAmpOrgRole(newOrg, role);

        AmpFundingGroupFeaturePanel newFundingGroup = getExistingFundingGroup(funding);
        if (newFundingGroup != null) {
            newFundingGroup.getList().addItem(funding);
        } else {
            if (fundingModel.getObject() == null) {
                fundingModel.setObject(new LinkedHashSet<AmpFunding>());
            }
            fundingModel.getObject().add(funding);
            tabsList.addItem(ampOrgRole);
            orgRolelist.origAddItem(ampOrgRole);
        }
        tabsList.updateModel();
        orgRolelist.updateModel();
        //find the index
        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(orgRolelist.getParent()));

        if (isTabsView) {
            target.add(AmpDonorFundingFormSectionFeature.this);
            target.appendJavaScript("switchTabs();");
        } else {
            target.add(orgRolelist.getParent());
        }
    }
    
    /**
     * Removes an item from a list editor 
     * @param listEditor
     * @param orgRole
     * @param target
     * @return true if item was deleted
     */
    protected boolean deteleItem(ListEditor<AmpOrgRole> listEditor, AmpOrgRole orgRole, AjaxRequestTarget target) {
        int idx = -1;
        ListItem<AmpOrgRole> delItem = null;
        AmpOrgRole ampOrgRoleToDelete  = null;
        for (int i = 0; i < listEditor.size(); i++) {
            ListItem<AmpOrgRole> listItem = (ListItem<AmpOrgRole>) listEditor.get(i);
            ampOrgRoleToDelete = (AmpOrgRole) listItem.getModelObject();
            if (ampOrgRoleToDelete.getOrganisation().getIdentifier().equals(orgRole.getOrganisation().getIdentifier())
                    && ampOrgRoleToDelete.getRole().getIdentifier().equals(orgRole.getRole().getIdentifier())) {
                idx = listItem.getIndex();
                delItem = listItem;
                break;
            }
        }
        if (idx > -1) {
            for (int i = idx + 1; i < listEditor.size(); i++) {
                ListItem<?> item = (ListItem<?>) listEditor.get(i);
                item.setIndex(item.getIndex() - 1);
            }

            listEditor.items.remove(idx);
            listEditor.remove(delItem);
            listEditor.updateModel();
            target.add(listEditor.getParent());
            //item was removed, remove from orgRoles
            if(ampOrgRoleToDelete!=null){
                Set<AmpOrgRole> orgRoles = orgRoleModel.getObject();
                orgRoles.remove(ampOrgRoleToDelete);
            }
            return true;
        }
        return false;
    }

    public void deleteTab(AmpOrgRole ampOrgRole, AjaxRequestTarget target) {
        deteleItem(orgRolelist, ampOrgRole, target);
        listItems.remove(ampOrgRole);
        deteleItem(tabsList, ampOrgRole, target);
    }
    
    public void updateFundingGroups(AmpOrgRole ampOrgRole, AjaxRequestTarget target) {
        AmpFundingGroupFeaturePanel existingFundingGroup = getExistingFundingGroup(ampOrgRole);
        boolean found = existingFundingGroup != null && existingFundingGroup.getList().size() > 0;
        
        if (!found) {
            // cleanup tab related data
            deleteTab(ampOrgRole, target);
            
            Set<AmpOrgRole> roles = fundingOrgRoleModel.getObject();
            for (Iterator<AmpOrgRole> it2 = roles.iterator(); it2.hasNext();) {
                AmpOrgRole role = it2.next();
                if (role.getRole().getRoleCode().equals(Constants.FUNDING_AGENCY)
                        && role.compareTo(ampOrgRole) == 0) {                                                       
                    it2.remove();
                    send(getPage(), Broadcast.BREADTH, new DonorFundingRolesEvent(target));
                    
                    if(this.originalSearchOrganizationSelector != null) {
                        this.originalSearchOrganizationSelector.setVisibilityAllowed(true);
                        target.add(this.originalSearchOrganizationSelector);
                    }
                    break;
                }
            }
            
            send(getPage(), Broadcast.BREADTH, new OrganisationUpdateEvent(target));
            send(getPage(), Broadcast.BREADTH, new GPINiSurveyListUpdateEvent(target));
        }
    }
    public  void setTemplateFilter(AmpTemplatesVisibility template) {
        searchOrganizationComponent.setTemplateFilter(template);
    }

    /**
     * @param id
     * @param fmName
     * @param am
     * @throws Exception
     */
    public AmpDonorFundingFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        
        isTabsView = FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.ACTIVITY_FORM_FUNDING_SECTION_DESIGN);
        
         final String expandAllKey = TranslatorWorker.generateTrnKey("Expand all");
         final AjaxLink expandAllLink = new AjaxLink("expandDonorItems"){
            
             final String javascript = "$(this).parents('div:eq(2)').find('.collapsable').show();$('#expandDonorItems-editor').hide();$('#expandDonorItems').hide();$('#collapseDonorItems').show();";
             public void onClick(AjaxRequestTarget target) {
                   //we don't need any action to be prepended here  
             }
            
            @Override
                protected void onConfigure() {
                        super.onConfigure();
                        configureTranslationMode(this,expandAllKey, javascript);
            }
        };
        add(expandAllLink);
        
        
        final String collapseAllKey = TranslatorWorker.generateTrnKey("Collapse all");
        final AjaxLink collapseAllLink = new AjaxLink("collapseDonorItems"){
            final String javascript = "$(this).parents('div:eq(2)').find('.collapsable').hide();$('#expandDonorItems').show();$('#collapseDonorItems').hide();$('#collapseDonorItems-editor').hide();";
            public void onClick(AjaxRequestTarget target) {
            //we don't need any action to be prepended here  
           }
           
           @Override
               protected void onConfigure() {
                       super.onConfigure();
                       configureTranslationMode(this,collapseAllKey, javascript);
           }
       };
       add(collapseAllLink);
       
        // group fields in FM under "Proposed Project Cost"
       
        fundingModel = new PropertyModel<Set<AmpFunding>>(am, "funding");
        if (fundingModel.getObject() == null)
            fundingModel.setObject(new LinkedHashSet<AmpFunding>());

        orgRoleModel = new PropertyModel<Set<AmpOrgRole>>(am, "orgrole");
        fundingOrgRoleModel = new AmpFundingGroupModel(fundingModel, this);
        
        final WebMarkupContainer wmc = new WebMarkupContainer("container");
        wmc.setOutputMarkupId(true);

        final WebMarkupContainer overviewLinkContainer = new WebMarkupContainer("overviewLinkContainer");
        overviewLinkContainer.setOutputMarkupId(true);

        final ExternalLink overviewTab = new ExternalLink("overviewLink", "#tab0", TranslatorWorker.translateText("Overview")); 
        overviewTab.setOutputMarkupId(true);
        wmc.setOutputMarkupId(true);
        
        overviewLinkContainer.add(overviewTab);
        wmc.add(overviewLinkContainer);

        add(wmc);
        
        tabsList = new ListEditor<AmpOrgRole>("donorItemsForTabs", fundingOrgRoleModel) {
            private static final long serialVersionUID = -206108834217117807L;
            
            @Override
            protected void onPopulateItem(ListItem<AmpOrgRole> item) {
                AmpOrganisation org = item.getModel().getObject().getOrganisation();
                String roleCode = item.getModel().getObject().getRole().getRoleCode();
                
                ExternalLink l = new ExternalLink("linkForTabs", "#tab" + (item.getIndex() + 1));
                l.add(new AttributePrepender("title", new Model<String>(org.getName()), ""));

                String translatedRoleCode = TranslatorWorker.translateText(roleCode);
                
                Label label = new Label("tabsLabel", new Model<String>(org.getAcronym()));
                
                Label subScript = new Label("tabsOrgRole", new Model<String>(translatedRoleCode));
                subScript.add(new AttributePrepender("class", new Model<String>("subscript_role"), ""));
                l.add(label);
                l.add(subScript);
                
                item.add(l);
            }

        };
        tabsList.setVisibilityAllowed(isTabsView);
        wmc.add(tabsList);
        
        AmpOverviewSection overviewSection = new AmpOverviewSection("overviewSection", "Overview Section", am) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                //we only show the overview tab if we are in tabView and if the overview section is visible
                isOverviewVisible.value = this.isVisible();
                overviewLinkContainer.setVisible(isTabsView && this.isVisible());
            }
        };
        overviewSection.add(new AttributePrepender("data-is_tab", new Model<String>("true"), ""));
        add(overviewSection);
        
        getRequiredFormComponents().addAll(overviewSection.getRequiredFormComponents());
        
        orgRolelist = new ListEditor<AmpOrgRole>("listFunding", fundingOrgRoleModel) {
            @Override
            protected void onPopulateItem(ListItem<AmpOrgRole> item) {
                AmpOrgRole orgRole = item.getModelObject();
                AmpFundingGroupFeaturePanel fg = new AmpFundingGroupFeaturePanel(
                        "fundingItem", "Funding Group", new Model<AmpRole>(orgRole.getRole()), fundingModel,
                        new Model<AmpOrganisation>(orgRole.getOrganisation()), am,
                        AmpDonorFundingFormSectionFeature.this);
                fg.setTabIndex(item.getIndex());
                item.add(new AttributePrepender("data-is_tab", new Model<String>("true"), ""));
                listItems.put(orgRole, fg);

                FundingOrganization fo = new FundingOrganization();
                fo.setAmpOrgId(orgRole.getOrganisation().getAmpOrgId());
                //TODO We should probably need to find another way to pass the current organisation and to test
                //TODO if we shouldn't pass wht current role instead of FUNDING_AGENCY hardcoded
                //TODO this hardcoded value will probably affect funding flow, but this code is old,
                //TODO from permission first implementation AMP-28443 is the followup ticket for this
                //TODO Everything nested under AmpDonorFundingFormSectionFeature needs the current organisation
                //TODO to be able to test the gates
                //TODO Putting the scope here we guarantee that every object underneath receives the proper information
                //TODO and we avoid duplicate code
                PermissionUtil.putInScope(((AmpAuthWebSession) getSession()).getHttpSession(),
                        GatePermConst.ScopeKeys.CURRENT_ORG, fo);
                PermissionUtil.putInScope(((AmpAuthWebSession) getSession()).getHttpSession(),
                        GatePermConst.ScopeKeys.CURRENT_ORG_ROLE, Constants.FUNDING_AGENCY);
                item.add(fg);
                AmpAuthWebSession session = (AmpAuthWebSession) getSession();
                PermissionUtil.removeFromScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_ORG);
                PermissionUtil.removeFromScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_ORG_ROLE);

            }

            @Override
            public void addItem(AmpOrgRole orgRole) {
                addToOrganisationSection(orgRole.getOrganisation());
                addItemToList(orgRole.getOrganisation(), orgRole);
                
                orgRolelist.updateModel();
            }
        };
        wmc.add(orgRolelist);

        searchOrgs = new AmpAutocompleteFieldPanel<AmpOrganisation>(
                "searchAutocomplete", "Search Organizations", true,true,
                AmpOrganisationSearchModel.class,id) {
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
                addToOrganisationSection(choice);
                orgRolelist.addItem(findAmpOrgRole(choice, getSelectedAmpRole()));
                
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
                send(getPage(), Broadcast.BREADTH, new OrganisationUpdateEvent(target));

                target.add(AmpDonorFundingFormSectionFeature.this);
                 
                if (isTabsView){
                    int index = calculateTabIndex(choice, getSelectedAmpRole());
                    target.appendJavaScript("switchTabs("+ index +");");
                }
            }

            @Override
            public Integer getChoiceLevel(AmpOrganisation choice) {
                // TODO Auto-generated method stub
                return null;
            }
        };

        searchOrganizationComponent = new AmpSearchOrganizationComponent<String>(
                "searchFundingOrgs", new Model<String>(),
                "Search Funding Organizations", searchOrgs, null);
        add(searchOrganizationComponent);

        Consumer<AmpTemplatesVisibility> setTemplateFilterConsumer = this::setTemplateFilter;
        this.configureTemplate(Constants.FUNDING_AGENCY, setTemplateFilterConsumer);

        orgRoleSelector = new AmpOrgRoleSelectorComponent("orgRoleSelector", am, getRoleFilter());
        add(orgRoleSelector);

        // when the org select changes, update the status of the addNewFunding
        // button, enable it if there is a selection made, disable it otherwise
        orgRoleSelector.getOrgSelect().getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {

                    private static final long serialVersionUID = 2964092433905217073L;

                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        if (orgRoleSelector.getOrgSelect().getChoiceContainer().getModelObject() == null) {
                            addNewFunding.getButton().setEnabled(false);
                        } else {
                            addNewFunding.getButton().setEnabled(true);
                        }
                        target.add(addNewFunding);
                    }
                });

        // button used to add funding based on the selected organization and
        // role
        addNewFunding = new AmpAjaxLinkField("addNewFuding", "New Funding Group", "New Funding") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onClick(AjaxRequestTarget target) {
                AmpOrganisation choice = (AmpOrganisation) orgRoleSelector.getOrgSelect().getChoiceContainer()
                        .getModelObject();
                orgRolelist.addItem(findAmpOrgRole(choice, getSelectedAmpRole()));
                
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
                
                if (isTabsView) {
                    target.add(AmpDonorFundingFormSectionFeature.this);
                    int index = calculateTabIndex(choice, getSelectedAmpRole());

                    target.appendJavaScript("switchTabs(" + index + ");");
                } else {
                    target.add(wmc);
                }
            }
        };

        // by default this button is disabled, when the form first loads
        addNewFunding.getButton().setEnabled(false);
        add(addNewFunding);
    }

    public AmpOrgRoleSelectorComponent getOrgRoleSelector() {
        return orgRoleSelector;
    }

    public PropertyModel<Set<AmpFunding>> getFundingModel() {
        return fundingModel;
    }

    public List<FormComponent<?>> getRequiredFormComponents() {
        return requiredFormComponents;
    }
    
    public void addItemToList(AmpOrganisation org, AmpOrgRole ampOrgRole) {
        AmpFunding funding = new AmpFunding();
        if (ampOrgRole != null) {
            funding.setSourceRole(ampOrgRole.getRole());
        } else if (orgRoleSelector.getRoleSelect().getChoiceContainer().getModelObject() != null) {
            funding.setSourceRole((AmpRole) orgRoleSelector.getRoleSelect().getChoiceContainer().getModelObject());
        } else {
            funding.setSourceRole(DbUtil.getAmpRole(Constants.FUNDING_AGENCY));
        }

        funding.setAmpDonorOrgId(org);
        funding.setAmpActivityId(am.getObject());
        funding.setMtefProjections(new HashSet<AmpFundingMTEFProjection>());
        funding.setFundingDetails(new HashSet<AmpFundingDetail>());
        funding.setGroupVersionedFunding(System.currentTimeMillis());
        
        // if it is a ssc activity we set a default type of assistance
        if (ActivityUtil.ACTIVITY_TYPE_SSC.equals(((AmpAuthWebSession) getSession()).getFormType())) {
            Collection<AmpCategoryValue> categoryValues = CategoryManagerUtil
                    .getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
            funding.setTypeOfAssistance(categoryValues.iterator().next());
        }
        
        AmpFundingGroupFeaturePanel existingFundingGroup = getExistingFundingGroup(funding);
        if (existingFundingGroup != null) {
            funding.setIndex(existingFundingGroup.getMaxFundingItemIndexFromList() + 1);
            existingFundingGroup.getList().addItem(funding);
        } else {
            if (fundingModel.getObject() == null) {
                fundingModel.setObject(new LinkedHashSet<AmpFunding>());
                // we only add the new tab if the org didnt exists
            }
            int order = fundingModel.getObject().size();
            funding.setOrderNumber(order);
            funding.setIndex(0);
            fundingModel.getObject().add(funding);
            tabsList.addItem(ampOrgRole);
            orgRolelist.origAddItem(ampOrgRole);
        }
        orgRolelist.updateModel();
        tabsList.updateModel();
    }

    public void addToOrganisationSection(AmpOrganisation org) {
        // check if org has been added with the selected role to the Related
        // Organisation section, if not then add it
        AmpRole selectedRole = getSelectedAmpRole();
        if (selectedRole != null) {
            String roleCode = selectedRole.getRoleCode();
            boolean found = false;
            Set<AmpOrgRole> orgRoles = orgRoleModel.getObject();
            for (AmpOrgRole role : orgRoles) {
                if (role.getRole().getRoleCode().equals(roleCode)
                        && role.getOrganisation().getAmpOrgId().equals(org.getAmpOrgId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                AmpOrgRole role = new AmpOrgRole();
                role.setOrganisation(org);
                role.setActivity(am.getObject());
                role.setRole(DbUtil.getAmpRole(roleCode));
                orgRoles.add(role);
            }
        }

    }
    
     private void configureTranslationMode (AjaxLink link,String key, String javascript) {
    if (TranslatorUtil.isTranslatorMode(getSession())){
            link.setOutputMarkupId(true);
            link.add(new AttributeAppender("style", new Model<String>("text-decoration: underline; color: #0CAD0C;"), ""));
            link.add(new AttributeModifier("key", key));
            link.add(new AttributeModifier("onclick", "spawnEditBox(this.id,\""+javascript+"\")"));
            
            
    }
    else{
            link.add(AttributeModifier.remove("key"));
            link.add(AttributeModifier.remove("onclick"));
            link.add(new AttributeModifier("onclick", javascript));
        
    }
}
     
     public void setOriginalSearchOrganizationSelector(AmpSearchOrganizationComponent<String> selector) {
         this.originalSearchOrganizationSelector = selector;
     }

     public int calculateTabIndex(AmpOrganisation choice, AmpRole role) {
        int index = -1;
        int newIndex = 0;
        Iterator<AmpOrgRole> tabs = tabsList.items.iterator();
        while (tabs.hasNext()) {
            AmpOrgRole o = tabs.next();
            if (o.getOrganisation().getIdentifier().equals(choice.getAmpOrgId()) && o.getRole().equals(role)) {
                index = newIndex;
                break;
            }
            newIndex++;
        }
        //we only increment one if the overview tab is visible
        if(index !=-1 && isOverviewVisible.value){
            index ++; //oveview is the first tab
        }
        return index;
    }
     
    protected AmpFundingGroupFeaturePanel getExistingFundingGroup(AmpFunding funding) {
        return getExistingFundingGroup(findAmpOrgRole(funding.getAmpDonorOrgId(), funding.getSourceRole()));
    }
    
    protected AmpFundingGroupFeaturePanel getExistingFundingGroup(AmpOrgRole ampOrgRole) {
        return listItems.get(ampOrgRole);
    }
    
    public AmpOrgRole findAmpOrgRole(AmpOrganisation org, AmpRole role) {
        if(org == null || role == null) {
            return null;
        }
        for (AmpOrgRole ampOrgRole : orgRoleModel.getObject()) {
            if (ampOrgRole.getOrganisation().getIdentifier().equals(org.getIdentifier()) 
                    && ampOrgRole.getRole().getIdentifier().equals(role.getIdentifier())) {
                return (AmpOrgRole) ampOrgRole;
            }
        }
        return null;
    }
    
    public void addFundingItem(AmpFunding funding) {
        if (funding == null) return;
        orgRolelist.addItem(findAmpOrgRole(funding.getAmpDonorOrgId(), funding.getSourceRole()));
    }
    
    protected AmpRole getSelectedAmpRole() {
        AmpRole role = orgRoleSelector.getRoleSelect().getModel().getObject();
        if (role == null) {
            role = DbUtil.getAmpRole(Constants.FUNDING_AGENCY);
        }
        return role;
    }

    /**
     * @return the isTabsView
     */
    public boolean isTabsView() {
        return isTabsView;
    }

    public void setSearchOrgsComponentVisibility(boolean visible, AjaxRequestTarget target) {
        this.searchOrganizationComponent.setVisibilityAllowed(visible);
        if (target != null) {
            target.add(this.searchOrganizationComponent.getParent());
        }
    }
}
