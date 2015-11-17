/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpOrgRoleSelectorComponent;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorCommitmentsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorDisbOrdersSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorDisbursementsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorExpendituresSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorFundingInfoSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpEstimatedDonorDisbursementsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpMTEFProjectionSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpReleaseOfFundsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCheckBoxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpFundingSummaryPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.events.FundingSectionSummaryEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Represents visually one funding item {@link AmpFunding} The model here is
 * represented by a {@link CompoundPropertyModel} around an {@link AmpFunding}
 * item
 * 
 * @author mpostelnicu@dgateway.org since Nov 3, 2010
 */
public class AmpFundingItemFeaturePanel extends AmpFeaturePanel<AmpFunding> {
	private static final long serialVersionUID = 1L;

	private AmpDonorFundingInfoSubsectionFeature fundingInfo;
	private AmpDonorDisbursementsSubsectionFeature disbursements;
	private Integer item;
	public AmpFundingItemFeaturePanel(String id, String fmName,
			final IModel<AmpFunding> fundingModel,final IModel<AmpActivityVersion> am, final AmpDonorFundingFormSectionFeature parent,Integer item) throws Exception {
		super(id, fundingModel, fmName, true);
		this.item=item;
		final Boolean isTabView=FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.ACTIVITY_FORM_FUNDING_SECTION_DESIGN);
		if (fundingModel.getObject().getFundingDetails() == null)
			fundingModel.getObject().setFundingDetails(new TreeSet<AmpFundingDetail>());
		//this should be changed to a propertyModel
		Label itemNumber=new Label("itemNumber", new Model(item + 1));
		add(itemNumber);
		Label itemNumberLabel=new TrnLabel("itemNumberLabel", new Model<String>("Funding Item"));
		add(itemNumberLabel);
		AmpFundingSummaryPanel fundingSummary = new AmpFundingSummaryPanel(
				"fundingSumary", "Funding Section Summary", fundingModel);
		
		fundingSummary.add(UpdateEventBehavior.of(FundingSectionSummaryEvent.class));
		fundingSummary.setOutputMarkupId(true);
		
		final WebMarkupContainer wmc = new WebMarkupContainer("fundingContainer");
		
		wmc.setOutputMarkupId(true);
		
		add(wmc);
		
		if (isTabView) {
			wmc.add(new AttributePrepender("style", new Model<String>("display: none;"), ""));
		} 

		add(fundingSummary);

		
		final WebMarkupContainer wmcLabelContainer = new WebMarkupContainer("labelContainer");
		wmcLabelContainer.setOutputMarkupId(true);
		
		Label orgLabel = new Label("donorOrg", new PropertyModel<AmpOrganisation>(fundingModel, "groupVersionedFunding"));
		orgLabel.add(new AttributePrepender("style", new Model<String>("font-weight: bold;"), ""));
        orgLabel.setVisible("true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SHOW_FUNDING_GROUP_ID)));
		orgLabel.setOutputMarkupId(true);
		wmcLabelContainer.add(orgLabel);
		
		AmpLabelFieldPanel<AmpOrganisation> sourceOrg = new AmpLabelFieldPanel<AmpOrganisation>(
				"sourceOrg", new PropertyModel<AmpOrganisation>(fundingModel, "ampDonorOrgId"), "Source Organisation", true);
		sourceOrg.add(new AttributeModifier("style", "display:inline-block"));
		
		wmcLabelContainer.add(sourceOrg);

		AmpLabelFieldPanel<AmpRole> sourceRoleLabel = new AmpLabelFieldPanel<AmpRole>(
				"sourceRoleLabel", new PropertyModel<AmpRole>(fundingModel, "sourceRole"), "Source Role", true);
		sourceRoleLabel.add(new AttributeModifier("style", "display:inline-block"));
		wmcLabelContainer.add(sourceRoleLabel);
		//this will be moved
		wmcLabelContainer.setVisible(!isTabView);
		add(wmcLabelContainer);
		String translatedMessage = TranslatorUtil.getTranslation("Do you really want to delete this funding item?");
		
		add(new ListEditorRemoveButton("delFunding", "Delete Funding Item", translatedMessage){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target) {
				AmpOrganisation org = fundingModel.getObject().getAmpDonorOrgId();
				AmpRole role = fundingModel.getObject().getSourceRole();
				super.onClick(target);
				parent.updateFundingGroups(parent.findAmpOrgRole(org, role), target);
				target.add(parent);
				if(isTabView){
					target.appendJavaScript("switchTabs();");
				}
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent));
			}
		});
		
		AmpAjaxLinkField addNewFunding= new AmpAjaxLinkField("addAnotherFunding","New Funding Item","New Funding Item") {			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target) {
				parent.addFundingItem(fundingModel.getObject());
				target.add(parent);
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent));
				if (isTabView) {
					//when adding a new funding search for the correct index
					//parent.getTabsList()
					int index = parent.calculateTabIndex(fundingModel.getObject().getAmpDonorOrgId(),
							fundingModel.getObject().getSourceRole());
					
					target.appendJavaScript("switchTabs("+ index +");");
				}
			}
		};
		
		
		AmpTextAreaFieldPanel donorObjective = new AmpTextAreaFieldPanel("donorObjective", new PropertyModel<String>(fundingModel,"donorObjective"), "Donor Objective", false);
		wmc.add(donorObjective);
        
        AmpTextAreaFieldPanel conditions = new AmpTextAreaFieldPanel("conditions", new PropertyModel<String>(fundingModel,"conditions"), "Conditions", false);
        wmc.add(conditions);
		
//		final AmpAutocompleteFieldPanel<AmpOrganisation> newOrgSelect=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchAutocomplete", "Search Organizations", true, AmpOrganisationSearchModel.class) {			
//			private static final long serialVersionUID = 1L;
//			@Override
//			protected String getChoiceValue(AmpOrganisation choice) {
//				return DbUtil.filter(choice.getName());
//			}
//			
//			@Override
//			protected boolean showAcronyms() {
//				return true;
//			}
//			
//			@Override
//			protected String getAcronym(AmpOrganisation choice) {
//				return choice.getAcronym();
//			}
//
//			@Override
//			public void onSelect(AjaxRequestTarget target,
//					AmpOrganisation choice) {
//				this.getParent().setVisible(false);
//				
//				ListItem listItem = findParent(ListItem.class);
//				AmpDonorFundingFormSectionFeature fundingSection = findParent(AmpDonorFundingFormSectionFeature.class);
//				AmpFunding funding = fundingModel.getObject();
//				AmpOrganisation oldOrg = funding.getAmpDonorOrgId();
//				fundingSection.switchOrg(listItem, funding, choice, target);
//				fundingSection.updateFundingGroups(oldOrg, target);
//			}
//			@Override
//			public Integer getChoiceLevel(AmpOrganisation choice) {
//				return null;
//			}
//		};
//		newOrgSelect.setIgnoreFmVisibility(true);
//		newOrgSelect.setVisible(false);
//		newOrgSelect.setOutputMarkupId(true);
		
		
//		final AmpSearchOrganizationComponent searchOrganization = new AmpSearchOrganizationComponent("searchFundingOrgs", new Model<String> (),
//				"Replace Funding Organizations", newOrgSelect);
//		searchOrganization.setIgnoreFmVisibility(true);
//		searchOrganization.setVisible(false);
//		searchOrganization.setOutputMarkupId(true);
//		add(searchOrganization);
	
		
		final AmpOrgRoleSelectorComponent orgRoleSelector = new AmpOrgRoleSelectorComponent("orgRoleSelector", am, 
				parent.getRoleFilter());
		wmc.add(orgRoleSelector);
		
		// button used to add funding based on the selected organization and role
		final AmpAjaxLinkField changeOrg = new AmpAjaxLinkField("changeOrg", "Change Org", "Change Org") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target) {
				
				ListItem listItem = findParent(ListItem.class);
				AmpDonorFundingFormSectionFeature fundingSection = findParent(AmpDonorFundingFormSectionFeature.class);
				AmpFunding funding = fundingModel.getObject();
				AmpOrganisation oldOrg = funding.getAmpDonorOrgId();
				AmpRole oldRole = funding.getSourceRole();
				fundingSection.switchOrg(listItem, funding,
						(AmpOrganisation) orgRoleSelector.getOrgSelect().getChoiceContainer().getModelObject(),
						(AmpRole) orgRoleSelector.getRoleSelect().getChoiceContainer().getModelObject(), target);
				fundingSection.updateFundingGroups(fundingSection.findAmpOrgRole(oldOrg, oldRole), target);
						
			}
		};

		// by default this button is disabled, when the form first loads
		changeOrg.getButton().setEnabled(false);
		wmc.add(changeOrg);

		
		orgRoleSelector.getOrgSelect().getChoiceContainer().add(
				new AjaxFormComponentUpdatingBehavior("onchange") {
					private static final long serialVersionUID = 2964092433905217073L;
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						if (orgRoleSelector.getOrgSelect().getChoiceContainer().getModelObject() == null)
							changeOrg.getButton().setEnabled(false);
							else
								changeOrg.getButton().setEnabled(true);
						target.add(changeOrg);
					}
				});


		
//		AmpAjaxLinkField changeFundingOrg= new AmpAjaxLinkField("newOrgButton","Change Funding Organisation","Change Funding Organisation") {			
//			private static final long serialVersionUID = 1L;
//			@Override
//			protected void onClick(AjaxRequestTarget target) {
//				orgRoleSelector.setVisible(true);
//				MarkupContainer tmpParent = orgRoleSelector.getParent();
//				target.add(tmpParent);
//				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(tmpParent));
//			}
//		};
//		add(changeFundingOrg);
		

		
		AmpCheckBoxFieldPanel active = new AmpCheckBoxFieldPanel("active",
				new PropertyModel<Boolean>(fundingModel, "active"), "Active");
		wmc.add(active);
		AmpCheckBoxFieldPanel delegatedCooperation = new AmpCheckBoxFieldPanel(
				"delegatedCooperation", new PropertyModel<Boolean>(
						fundingModel, "delegatedCooperation"),
				"Delegated Cooperation");
		wmc.add(delegatedCooperation);
		AmpCheckBoxFieldPanel delegatedPartner = new AmpCheckBoxFieldPanel(
				"delegatedPartner", new PropertyModel<Boolean>(fundingModel,
						"delegatedPartner"), "Delegated Partner");
		wmc.add(delegatedPartner);

		fundingInfo = new AmpDonorFundingInfoSubsectionFeature(
				"fundingInfoSubsection", fundingModel,"Funding Classification",fundingSummary);
		wmc.add(fundingInfo);
		
		AmpMTEFProjectionSubsectionFeature mtefProjections = new AmpMTEFProjectionSubsectionFeature(
				"mtefProjectionsSubsection", fundingModel,"MTEF Projections");
		wmc.add(mtefProjections);
		
		AmpDonorCommitmentsSubsectionFeature commitments = new AmpDonorCommitmentsSubsectionFeature(
				"commitments", fundingModel,"Commitments",Constants.COMMITMENT);
		wmc.add(commitments);
		
		
		disbursements = new AmpDonorDisbursementsSubsectionFeature(
				"disbursements", fundingModel,"Disbursements",Constants.DISBURSEMENT);
		wmc.add(disbursements);
		
		AmpDonorDisbOrdersSubsectionFeature disbOrders = new AmpDonorDisbOrdersSubsectionFeature(
				"disbOrders", fundingModel,"Disbursement Orders",Constants.DISBURSEMENT_ORDER);
		disbOrders.setDisbursements(disbursements);
		wmc.add(disbOrders);
		
		AmpEstimatedDonorDisbursementsSubsectionFeature edd = new AmpEstimatedDonorDisbursementsSubsectionFeature(
					"estimatedDisbursements", fundingModel,"Estimated Disbursements",Constants.ESTIMATED_DONOR_DISBURSEMENT);
			wmc.add(edd);
		
		AmpReleaseOfFundsSubsectionFeature rof = new AmpReleaseOfFundsSubsectionFeature(
					"releaseOfFunds", fundingModel,"Release of Funds",Constants.RELEASE_OF_FUNDS);
			wmc.add(rof);
		
			
		AmpDonorExpendituresSubsectionFeature expenditures = new AmpDonorExpendituresSubsectionFeature(
				"expenditures", fundingModel,"Expenditures",Constants.EXPENDITURE);
		wmc.add(expenditures);


		add(addNewFunding);
	}

	public AmpDonorFundingInfoSubsectionFeature getFundingInfo() {
		return fundingInfo;
	}

	public AmpDonorDisbursementsSubsectionFeature getDisbursements() {
		return disbursements;
	}
	
}
