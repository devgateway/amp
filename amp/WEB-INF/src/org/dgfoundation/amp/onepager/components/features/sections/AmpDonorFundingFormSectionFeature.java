/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

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
import org.dgfoundation.amp.onepager.components.AmpOrgRoleSelectorComponent;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.AmpSearchOrganizationComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingGroupFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpOverviewSection;
import org.dgfoundation.amp.onepager.events.DonorFundingRolesEvent;
import org.dgfoundation.amp.onepager.events.OrganisationUpdateEvent;
import org.dgfoundation.amp.onepager.models.AmpFundingGroupModel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

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
	private TreeMap<AmpOrganisation, AmpFundingGroupFeaturePanel> listItems = new TreeMap<AmpOrganisation, AmpFundingGroupFeaturePanel>();
	protected ListEditor<AmpOrganisation> list;
	
	protected ListEditor<AmpOrganisation> tabsList;
	
	private IModel<Set<AmpOrganisation>> setModel;
	private IModel<Set<AmpOrgRole>> roleModel;
	private AbstractReadOnlyModel<List<AmpFunding>> listModel;
	private PropertyModel<Set<AmpFunding>> fundingModel;
	private AmpAjaxLinkField addNewFunding;
	private AmpOrgRoleSelectorComponent orgRoleSelector;
	private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>>();
	private AmpSearchOrganizationComponent<String> originalSearchOrganizationSelector;

	private final static String[] ACTIVITY_ROLE_FILTER = { Constants.FUNDING_AGENCY };
	private final static String[] SSC_ROLE_FILTER = { Constants.FUNDING_AGENCY,
			Constants.EXECUTING_AGENCY, Constants.BENEFICIARY_AGENCY };
	public final static String[] DISBURSEMENTS_ROLE_FILTER = new String[] {
			Constants.IMPLEMENTING_AGENCY, Constants.EXECUTING_AGENCY,
			Constants.BENEFICIARY_AGENCY };
	private final ValueWrapper<Boolean> isOverviewVisible=new ValueWrapper<Boolean>(false);
	public ListEditor<AmpOrganisation> getList() {
		return list;
	}

	public ListEditor<AmpOrganisation> getTabsList() {
		return tabsList;
	}

	public IModel<Set<AmpOrganisation>> getSetModel() {
		return setModel;
	}

	public String[] getRoleFilter() {
		String[] roleFilter = ACTIVITY_ROLE_FILTER;
		if (ActivityUtil.ACTIVITY_TYPE_SSC
				.equals(((AmpAuthWebSession) getSession()).getFormType()))
			roleFilter = SSC_ROLE_FILTER;
		return roleFilter;
	}

	public void switchOrg(ListItem item, AmpFunding funding,
			AmpOrganisation newOrg, AmpRole role, AjaxRequestTarget target) {

		AmpFundingGroupFeaturePanel existingFundGrp = listItems.get(funding
				.getAmpDonorOrgId());

		existingFundGrp.getList().remove(item);
		existingFundGrp.getList().updateModel();

		funding.setAmpDonorOrgId(newOrg);
		funding.setSourceRole(role);

		if (listItems.containsKey(newOrg)) {
			AmpFundingGroupFeaturePanel fg = listItems.get(newOrg);
			fg.getList().addItem(funding);
		} else {
			fundingModel.getObject().add(funding);
			list.origAddItem(newOrg);
			tabsList.addItem(newOrg);
		}
		//find the idex
//		System.out.println("Switch orgs El indice es : "+
//		list.items.indexOf(newOrg));
		target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(list
				.getParent()));

		if( FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.ACTIVITY_FORM_FUNDING_SECTION_DESIGN)){
			target.add(AmpDonorFundingFormSectionFeature.this);
			target.appendJavaScript("switchTabs();");
		}else{
			target.add(list.getParent());
		}
	}

	public void deleteTab(AmpOrganisation missing, AjaxRequestTarget target) {
		int idx = -1;
		ListItem<AmpOrganisation> delItem = null;
		for (int i = 0; i < tabsList.size(); i++) {
			ListItem<AmpOrganisation> item = (ListItem<AmpOrganisation>) tabsList
					.get(i);
			AmpOrganisation org = item.getModelObject();
			if (missing.getAmpOrgId().equals(org.getAmpOrgId())) {
				idx = item.getIndex();
				delItem = item;
			}
		}
		if (idx > -1) {
			for (int i = idx + 1; i < tabsList.size(); i++) {
				ListItem<?> item = (ListItem<?>) tabsList.get(i);
				item.setIndex(item.getIndex() - 1);
			}

			tabsList.items.remove(idx);
			tabsList.updateModel();
//			target.add(tabsList.getParent());
			tabsList.remove(delItem);
			//listItems.remove(missing);check if thiss needs to be done
		}
	}
	public void updateFundingGroups(AmpOrganisation missing,
			AjaxRequestTarget target) {
		Iterator<AmpFunding> it = fundingModel.getObject().iterator();
		boolean found = false;
		while (it.hasNext()) {
			AmpFunding funding = (AmpFunding) it.next();
			AmpOrganisation org = funding.getAmpDonorOrgId();
			if (missing.getAmpOrgId().equals(org.getAmpOrgId())) {
				found = true;
				break;
			}
		}

		if (!found) {
			deleteTab(missing,target);
			// remove the org group
			int idx = -1;
			ListItem<AmpOrganisation> delItem = null;
			for (int i = 0; i < list.size(); i++) {
				ListItem<AmpOrganisation> item = (ListItem<AmpOrganisation>) list
						.get(i);
				AmpOrganisation org = item.getModelObject();
				if (missing.getAmpOrgId().equals(org.getAmpOrgId())) {
					idx = item.getIndex();
					delItem = item;
				}
			}
			if (idx > -1) {
				for (int i = idx + 1; i < list.size(); i++) {
					ListItem<?> item = (ListItem<?>) list.get(i);
					item.setIndex(item.getIndex() - 1);
				}

				list.items.remove(idx);
				list.updateModel();
				target.add(list.getParent());
				list.remove(delItem);
				listItems.remove(missing);
			}
			// also remove the org role
			Set<AmpOrgRole> roles = roleModel.getObject();
			for (Iterator<AmpOrgRole> it2 = roles.iterator(); it2.hasNext();) {
				AmpOrgRole role = it2.next();
				if (role.getRole().getRoleCode()
						.equals(Constants.FUNDING_AGENCY)
						&& role.getOrganisation().getAmpOrgId()
								.equals(missing.getAmpOrgId())) {														
					it2.remove();
					send(getPage(), Broadcast.BREADTH,
							new DonorFundingRolesEvent(target));								
					
					if(this.originalSearchOrganizationSelector != null) {
						this.originalSearchOrganizationSelector.setVisibilityAllowed(true);
						target.add(this.originalSearchOrganizationSelector);
					}
					
					break;
				}
			}

		}				
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
		
		final Boolean isTabsView = FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.ACTIVITY_FORM_FUNDING_SECTION_DESIGN);
		
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

		setModel = new AmpFundingGroupModel(fundingModel);
		roleModel = new PropertyModel<Set<AmpOrgRole>>(am, "orgrole");

		final WebMarkupContainer wmc = new WebMarkupContainer("container");
		wmc.setOutputMarkupId(true);

		final WebMarkupContainer overviewLinkContainer = new WebMarkupContainer("overviewLinkContainer");
		overviewLinkContainer.setOutputMarkupId(true);

		final ExternalLink overviewTab = new ExternalLink("overviewLink","#tab0","Overview"); 
		
		overviewTab.setOutputMarkupId(true);
		
		wmc.setOutputMarkupId(true);
		
		
		overviewLinkContainer.add(overviewTab);
		wmc.add(overviewLinkContainer);

		add(wmc);

		 tabsList = new ListEditor<AmpOrganisation>(
				"donorItemsForTabs", setModel) {
					private static final long serialVersionUID = -206108834217117807L;
			@Override
			protected void onPopulateItem(ListItem<AmpOrganisation> item) {
//				ExternalLink l = new ExternalLink("linkForTabs", "#tab"
//						+ (item.getIndex()+1), item.getModel().getObject()
//						.getAcronym() );
				ExternalLink l = new ExternalLink("linkForTabs", "#tab"
						+ (item.getIndex()+1));
				l.add(new AttributePrepender("title", new Model<String>(item.getModel().getObject().getName()), ""));

				PropertyModel<AmpRole>sourceRoleMOdel=null;
				for (AmpFunding f:fundingModel.getObject()){ 
					if(f.getAmpDonorOrgId().getAmpOrgId().equals(item.getModel().getObject().getAmpOrgId())){
						sourceRoleMOdel=new PropertyModel<>(f.getSourceRole(), "roleCode");
					}
					
				}
				Label label=new Label("tabsLabel", new Model<String>(item.getModel().getObject().getAcronym()));

				Label subScript=new Label("tabsOrgRole",sourceRoleMOdel); 
				subScript.add(new AttributePrepender("class", new Model<String>("subscript_role"), ""));
				l.add(label);
				l.add(subScript);
				
				item.add(l);
			}
			public void addItem(AmpOrganisation org) {
				tabsList.origAddItem(org);
				tabsList.updateModel();
			}

		};
		tabsList.setVisibilityAllowed(isTabsView);
		wmc.add(tabsList);

		
		
		AmpOverviewSection overviewSection = new AmpOverviewSection("overviewSection", "Overview Section", am) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				//we only show the overview tab if we are in tabView and if the overview section is visible
				isOverviewVisible.value=this.isVisible();
				overviewLinkContainer.setVisible(isTabsView && this.isVisible());
			}
		};
        overviewSection.add(new AttributePrepender("data-is_tab", new Model<String>("true"), ""));
		add(overviewSection);
		
		getRequiredFormComponents().addAll(
				overviewSection.getRequiredFormComponents());



		
		
		list = new ListEditor<AmpOrganisation>("listFunding", setModel) {
			@Override
			protected void onPopulateItem(ListItem<AmpOrganisation> item) {
				AmpFundingGroupFeaturePanel fg = new AmpFundingGroupFeaturePanel(
						"fundingItem", "Funding Group", fundingModel,
						item.getModel(), am,
						AmpDonorFundingFormSectionFeature.this);
				//we decorete 
					item.add(new AttributePrepender("data-is_tab", new Model<String>("true"), ""));
				listItems.put(item.getModelObject(), fg);
				item.add(fg);
			}

			@Override
			public void addItem(AmpOrganisation org) {
				addItemToList (org);
				addToOrganisationSection (org);
			}
		};
		wmc.add(list);

		final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs = new AmpAutocompleteFieldPanel<AmpOrganisation>(
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
			public void onSelect(AjaxRequestTarget target,
					AmpOrganisation choice) {
				list.addItem(choice);
				

				target.appendJavaScript(OnePagerUtil
						.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
				send(getPage(), Broadcast.BREADTH,
						new OrganisationUpdateEvent(target));
				if(isTabsView){
					//if in tabs view we should refresh the whole section so 
					//tabs are inplace and recreated
					target.add(AmpDonorFundingFormSectionFeature.this);
					int index = calculateTabIndex(choice);

					target.appendJavaScript("switchTabs("+ index +");");	
					
				}else{
					target.add(wmc);	
				}
				
			}

			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		AmpSearchOrganizationComponent<String> searchOrganization = new AmpSearchOrganizationComponent<String>(
				"searchFundingOrgs", new Model<String>(),
				"Search Funding Organizations", searchOrgs, null);
		wmc.add(searchOrganization);

		orgRoleSelector = new AmpOrgRoleSelectorComponent("orgRoleSelector",
				am, getRoleFilter());
		add(orgRoleSelector);

		// when the org select changes, update the status of the addNewFunding
		// button, enable it if there is a selection made, disable it otherwise
		orgRoleSelector.getOrgSelect().getChoiceContainer()
				.add(new AjaxFormComponentUpdatingBehavior("onchange") {

					private static final long serialVersionUID = 2964092433905217073L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						if (orgRoleSelector.getOrgSelect().getChoiceContainer()
								.getModelObject() == null)
							addNewFunding.getButton().setEnabled(false);
						else
							addNewFunding.getButton().setEnabled(true);
						target.add(addNewFunding);
					}
				});

		// button used to add funding based on the selected organization and
		// role
		addNewFunding = new AmpAjaxLinkField("addNewFuding",
				"New Funding Group", "New Funding") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target) {
				AmpOrganisation choice=(AmpOrganisation) orgRoleSelector.getOrgSelect()
						.getChoiceContainer().getModelObject();
				list.addItem(choice);
				target.appendJavaScript(OnePagerUtil
						.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
				
				if (FeaturesUtil
						.getGlobalSettingValueBoolean(GlobalSettingsConstants.ACTIVITY_FORM_FUNDING_SECTION_DESIGN)) {
					target.add(AmpDonorFundingFormSectionFeature.this);
					int index = calculateTabIndex(choice);

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
	
	public void addItemToList (AmpOrganisation org) {
		AmpFunding funding = new AmpFunding();
		if (orgRoleSelector.getRoleSelect().getChoiceContainer()
				.getModelObject() != null)
			funding.setSourceRole((AmpRole) orgRoleSelector
					.getRoleSelect().getChoiceContainer()
					.getModelObject());
		else
			funding.setSourceRole(DbUtil
					.getAmpRole(Constants.FUNDING_AGENCY));

		funding.setAmpDonorOrgId(org);
		funding.setAmpActivityId(am.getObject());
		funding.setMtefProjections(new HashSet<AmpFundingMTEFProjection>());
		funding.setFundingDetails(new HashSet<AmpFundingDetail>());
		funding.setGroupVersionedFunding(System.currentTimeMillis());
		// if it is a ssc activity we set a default type of assistance
		if (ActivityUtil.ACTIVITY_TYPE_SSC
				.equals(((AmpAuthWebSession) getSession())
						.getFormType())) {
			Collection<AmpCategoryValue> categoryValues = CategoryManagerUtil
					.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
			funding.setTypeOfAssistance(categoryValues.iterator()
					.next());
		}
		list.updateModel();

		if (listItems.containsKey(org)) {
			AmpFundingGroupFeaturePanel fg = listItems.get(org);
			fg.getList().addItem(funding);
		} else {
			if (fundingModel.getObject() == null) {
				fundingModel.setObject(new LinkedHashSet<AmpFunding>());
				// we only add the new tab if the org didnt exists
			}
			fundingModel.getObject().add(funding);
			tabsList.addItem(org);

			list.origAddItem(org);
		}
	}

	private void addToOrganisationSection(AmpOrganisation org) {
		// check if org has been added with the selected role to the Related
		// Organisation section, if not then add it
		// Only for non-ssc activities
		AmpRole selectedRole = (AmpRole) orgRoleSelector.getRoleSelect().getChoiceContainer().getModelObject();
		if (selectedRole != null) {
			String roleCode = selectedRole.getRoleCode();
			// Constants.FUNDING_AGENCY;
			if (!ActivityUtil.ACTIVITY_TYPE_SSC.equals(((AmpAuthWebSession) getSession()).getFormType())) {
				boolean found = false;
				Set<AmpOrgRole> orgRoles = roleModel.getObject();
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

	 public int calculateTabIndex(AmpOrganisation choice) {
		int index = -1;
		int newIndex = 0;
		Iterator<AmpOrganisation> tabs = tabsList.items.iterator();
		while (tabs.hasNext()) {
			AmpOrganisation o = tabs.next();
			if (o.getAmpOrgId().equals(choice.getAmpOrgId())) {
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
}
