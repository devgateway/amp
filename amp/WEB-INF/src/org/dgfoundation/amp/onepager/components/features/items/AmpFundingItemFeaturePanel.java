/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.TreeSet;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpSearchOrganizationComponent;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorCommitmentsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorDisbOrdersSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorDisbursementsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorExpendituresSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpDonorFundingInfoSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpMTEFProjectionSubsectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCheckBoxFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;

/**
 * Represents visually one funding item {@link AmpFunding} The model here is
 * represented by a {@link CompoundPropertyModel} around an {@link AmpFunding}
 * item
 * 
 * @author mpostelnicu@dgateway.org since Nov 3, 2010
 */
public class AmpFundingItemFeaturePanel extends AmpFeaturePanel<AmpFunding> {
	

	
	private AmpDonorFundingInfoSubsectionFeature fundingInfo;

	/**
	 * @param id
	 * @param fmName
	 * @param ampDonorFundingFormSectionFeature 
	 * @throws Exception
	 */
	public AmpFundingItemFeaturePanel(String id, String fmName,
			final IModel<AmpFunding> fundingModel,final IModel<AmpActivityVersion> am, final AmpDonorFundingFormSectionFeature parent) throws Exception {
		super(id, fundingModel, fmName, true);
		
		if (fundingModel.getObject().getFundingDetails() == null)
			fundingModel.getObject().setFundingDetails(new TreeSet());
		
		
		final Label orgLabel = new Label("donorOrg", new PropertyModel<AmpOrganisation>(fundingModel, "ampDonorOrgId"));
		orgLabel.setOutputMarkupId(true);
		add(orgLabel);
		
		String translatedMessage = TranslatorUtil.getTranslation("Do you really want to delete this funding item?");
		
		add(new ListEditorRemoveButton("delFunding", "Delete Funding Item", translatedMessage){
			@Override
			protected void onClick(AjaxRequestTarget target) {
				super.onClick(target);
				target.add(parent);
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent));
			}
		});
		
		AmpAjaxLinkField addNewFunding= new AmpAjaxLinkField("addAnotherFunding","New Funding Item","New Funding Item") {			
			@Override
			protected void onClick(AjaxRequestTarget target) {
				AmpFunding funding = new AmpFunding();
				funding.setAmpDonorOrgId(fundingModel.getObject().getAmpDonorOrgId());
				funding.setAmpActivityId(am.getObject());

				funding.setMtefProjections(new TreeSet<AmpFundingMTEFProjection>());
				funding.setFundingDetails(new TreeSet<AmpFundingDetail>());
				funding.setGroupVersionedFunding(System.currentTimeMillis());
				
				parent.getList().addItem(funding);
				parent.getList().updateModel();
				target.add(parent);
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent));
			}
		};
		add(addNewFunding);
		
		
		final AmpAutocompleteFieldPanel<AmpOrganisation> newOrgSelect=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchAutocomplete", "Search Organizations", true, AmpOrganisationSearchModel.class) {			
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
				PropertyModel<AmpOrganisation> pm = new PropertyModel<AmpOrganisation>(fundingModel, "ampDonorOrgId");
				pm.setObject(choice);
				this.getParent().setVisible(false);
				
				target.add(parent);
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent));
			}
			@Override
			public Integer getChoiceLevel(AmpOrganisation choice) {
				return null;
			}
		};
//		newOrgSelect.setIgnoreFmVisibility(true);
//		newOrgSelect.setVisible(false);
//		newOrgSelect.setOutputMarkupId(true);
		
		
		final AmpSearchOrganizationComponent searchOrganization = new AmpSearchOrganizationComponent("searchFundingOrgs", new Model<String> (),
				"Replace Funding Organizations", newOrgSelect);
		searchOrganization.setIgnoreFmVisibility(true);
		searchOrganization.setVisible(false);
		searchOrganization.setOutputMarkupId(true);

		
		add(searchOrganization);

		AmpAjaxLinkField changeFundingOrg= new AmpAjaxLinkField("newOrgButton","Change Funding Organisation","Change Funding Organisation") {			
			@Override
			protected void onClick(AjaxRequestTarget target) {
				searchOrganization.setVisible(true);
				MarkupContainer tmpParent = searchOrganization.getParent();
				target.add(tmpParent);
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(tmpParent));
			}
		};
		add(changeFundingOrg);
		
		
		AmpCheckBoxFieldPanel active = new AmpCheckBoxFieldPanel("active",
				new PropertyModel<Boolean>(fundingModel, "active"), "Active");
		add(active);
		AmpCheckBoxFieldPanel delegatedCooperation = new AmpCheckBoxFieldPanel(
				"delegatedCooperation", new PropertyModel<Boolean>(
						fundingModel, "delegatedCooperation"),
				"Delegated Cooperation");
		add(delegatedCooperation);
		AmpCheckBoxFieldPanel delegatedPartner = new AmpCheckBoxFieldPanel(
				"delegatedPartner", new PropertyModel<Boolean>(fundingModel,
						"delegatedPartner"), "Delegated Partner");
		add(delegatedPartner);

		fundingInfo = new AmpDonorFundingInfoSubsectionFeature(
				"fundingInfoSubsection", fundingModel,"Funding Classification");
		add(fundingInfo);
		
		AmpMTEFProjectionSubsectionFeature mtefProjections = new AmpMTEFProjectionSubsectionFeature(
				"mtefProjectionsSubsection", fundingModel,"MTEF Projections");
		add(mtefProjections);
		
		AmpDonorCommitmentsSubsectionFeature commitments = new AmpDonorCommitmentsSubsectionFeature(
				"commitments", fundingModel,"Commitments",Constants.COMMITMENT);
		add(commitments);
		
		
		AmpDonorDisbursementsSubsectionFeature disbursements = new AmpDonorDisbursementsSubsectionFeature(
				"disbursements", fundingModel,"Disbursements",Constants.DISBURSEMENT);
		add(disbursements);
		
		AmpDonorDisbOrdersSubsectionFeature disbOrders = new AmpDonorDisbOrdersSubsectionFeature(
				"disbOrders", fundingModel,"Disbursement Orders",Constants.DISBURSEMENT_ORDER);
		disbOrders.setDisbursements(disbursements);
		add(disbOrders);
		
		AmpDonorExpendituresSubsectionFeature expenditures = new AmpDonorExpendituresSubsectionFeature(
				"expenditures", fundingModel,"Expenditures",Constants.EXPENDITURE);
		add(expenditures);
	}

	public AmpDonorFundingInfoSubsectionFeature getFundingInfo() {
		return fundingInfo;
	}

}
