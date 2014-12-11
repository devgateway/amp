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
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpOrgRoleSelectorComponent;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.AmpSearchOrganizationComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingGroupFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpProposedProjectCost;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.DonorFundingRolesEvent;
import org.dgfoundation.amp.onepager.events.OrganisationUpdateEvent;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.models.AmpFundingGroupModel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
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
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.calendar.action.SearchOrganisation;
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

	public ListEditor<AmpOrganisation> getList() {
		return list;
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
		}

		target.add(list.getParent());
		target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(list
				.getParent()));
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
		AmpProposedProjectCost propProjectCost = new AmpProposedProjectCost(
				"propProjCost", "Proposed Project Cost", am);
		add(propProjectCost);
		getRequiredFormComponents().addAll(
				propProjectCost.getRequiredFormComponents());

		RangeValidator<Integer> rangeValidator = new RangeValidator<Integer>(1,
				10);
		AttributeModifier attributeModifier = new AttributeModifier("size",
				new Model(1));
		AmpTextFieldPanel<Integer> fundingSourcesNumberPanel = new AmpTextFieldPanel<Integer>(
				"fundingSourcesNumber", new PropertyModel<Integer>(am,
						"fundingSourcesNumber"),
				CategoryConstants.FUNDING_SOURCES_NUMBER_NAME,
				AmpFMTypes.MODULE);
		fundingSourcesNumberPanel.getTextContainer().add(rangeValidator);
		fundingSourcesNumberPanel.getTextContainer().add(attributeModifier);
		add(fundingSourcesNumberPanel);

		AmpCategorySelectFieldPanel typeOfCooperation = new AmpCategorySelectFieldPanel(
				"typeOfCooperation", CategoryConstants.TYPE_OF_COOPERATION_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"),
						CategoryConstants.TYPE_OF_COOPERATION_KEY),
				CategoryConstants.TYPE_OF_COOPERATION_NAME, true, false, null,
				AmpFMTypes.MODULE);
		add(typeOfCooperation);

		AmpCategorySelectFieldPanel typeOfImplementation = new AmpCategorySelectFieldPanel(
				"typeOfImplementation",
				CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"),
						CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY),
				CategoryConstants.TYPE_OF_IMPLEMENTATION_NAME, true, false,
				null, AmpFMTypes.MODULE);
		add(typeOfImplementation);

		AmpCategorySelectFieldPanel modalities = new AmpCategorySelectFieldPanel(
				"modalities",
				CategoryConstants.MODALITIES_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"), CategoryConstants.MODALITIES_KEY),
				CategoryConstants.MODALITIES_NAME, true, false, null,
				AmpFMTypes.MODULE);
		add(modalities);

		fundingModel = new PropertyModel<Set<AmpFunding>>(am, "funding");
		if (fundingModel.getObject() == null)
			fundingModel.setObject(new LinkedHashSet<AmpFunding>());

		setModel = new AmpFundingGroupModel(fundingModel);
		roleModel = new PropertyModel<Set<AmpOrgRole>>(am, "orgrole");

		final WebMarkupContainer wmc = new WebMarkupContainer("container");
		wmc.setOutputMarkupId(true);
		add(wmc);

		list = new ListEditor<AmpOrganisation>("listFunding", setModel) {
			@Override
			protected void onPopulateItem(ListItem<AmpOrganisation> item) {
				AmpFundingGroupFeaturePanel fg = new AmpFundingGroupFeaturePanel(
						"fundingItem", "Funding Group", fundingModel,
						item.getModel(), am,
						AmpDonorFundingFormSectionFeature.this);
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
				AmpOrganisationSearchModel.class) {
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
								
				target.add(wmc);
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
				list.addItem((AmpOrganisation) orgRoleSelector.getOrgSelect()
						.getChoiceContainer().getModelObject());
				target.appendJavaScript(OnePagerUtil
						.getToggleChildrenJS(AmpDonorFundingFormSectionFeature.this));
				target.add(wmc);
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
			if (fundingModel.getObject() == null)
				fundingModel.setObject(new LinkedHashSet<AmpFunding>());

			fundingModel.getObject().add(funding);
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

}
