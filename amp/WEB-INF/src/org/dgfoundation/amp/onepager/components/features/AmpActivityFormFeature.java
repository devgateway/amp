/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.sections.AmpComponentsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpContactsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpContractingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpCrossCuttingIssuesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpInternalIdsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIssuesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpLocationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpMEFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpPIFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpPlanningFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpProgramFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalObservationsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRelatedOrganizationsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpResourcesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpSectorsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Main component hub for all activity form subcomponents.
 * This is {@link AmpFeaturePanel} as such it supports
 * {@link OnePagerUtil#cascadeFmEnabled(AjaxRequestTarget, boolean, org.apache.wicket.Component)}
 * and
 * {@link OnePagerUtil#cascadeFmVisible(AjaxRequestTarget, boolean, org.apache.wicket.Component)}
 * @author mpostelnicu@dgateway.org
 * @since Jun 7, 2011
 */
public class AmpActivityFormFeature extends AmpFeaturePanel<AmpActivityVersion> {
	protected Form<AmpActivityVersion> activityForm;
	private AmpIdentificationFormSectionFeature identificationFeature;
	private AmpInternalIdsFormSectionFeature internalIdsFeature;
	private AmpPlanningFormSectionFeature planningFeature;
	private AmpRegionalFundingFormSectionFeature regionalFundingFeature;
	private AmpLocationFormSectionFeature locationFeature;
	private AmpProgramFormSectionFeature programFeature;
	private AmpCrossCuttingIssuesFormSectionFeature crossCuttingIssues;
	private AmpSectorsFormSectionFeature sectorsFeature;
	private AmpDonorFundingFormSectionFeature donorFundingFeature;
	private AmpRelatedOrganizationsFormSectionFeature relatedOrganizations;
	private AmpComponentsFormSectionFeature components;
	private AmpStructuresFormSectionFeature structures;
	private AmpIssuesFormSectionFeature issues;
	private AmpRegionalObservationsFormSectionFeature regionalObs;
	private AmpContactsFormSectionFeature contacts;
	private AmpContractingFormSectionFeature contracts;
	private AmpMEFormSectionFeature me;
	private AmpPIFormSectionFeature pi;
	private AmpResourcesFormSectionFeature resources;
	

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 * @throws Exception 
	 */
	public AmpActivityFormFeature(String id, final IModel<AmpActivityVersion> am,
			String fmName) throws Exception {
		super(id, am, fmName, true);
		
		activityForm=new Form<AmpActivityVersion>("activityForm") ;
		activityForm.setOutputMarkupId(true);
		
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		
        //int[] filteredErrorLevels = new int[]{FeedbackMessage.ERROR};
        //feedbackPanel.setFilter(new ErrorLevelsFeedbackMessageFilter(filteredErrorLevels));

		activityForm.add(feedbackPanel);
		add(activityForm);
		
		//add ajax submit button
		AmpButtonField saveAndSubmit = new AmpButtonField("saveAndSubmit","Save and Submit", AmpFMTypes.FEATURE) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				am.setObject(am.getObject());
				info("Activity saved successfully");
				target.addComponent(feedbackPanel);
			}
		};
		saveAndSubmit.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		activityForm.add(saveAndSubmit);
		
		initializeFormComponents(am);
	}
	
	public void initializeFormComponents(IModel<AmpActivityVersion> am) throws Exception {
		identificationFeature = new AmpIdentificationFormSectionFeature(
				"identification", "Identification", am);
		activityForm.add(identificationFeature);

		internalIdsFeature = new AmpInternalIdsFormSectionFeature(
				"internalIds", "Activity Internal IDs", am);
		activityForm.add(internalIdsFeature);

		planningFeature = new AmpPlanningFormSectionFeature(
				"planning", "Planning", am);
		activityForm.add(planningFeature);

		regionalFundingFeature = new AmpRegionalFundingFormSectionFeature(
				"regionalFunding", "Regional Funding", am);
		activityForm.add(regionalFundingFeature);

		
		locationFeature = new AmpLocationFormSectionFeature(
				"location", "Location", am,regionalFundingFeature);
		activityForm.add(locationFeature);

		programFeature = new AmpProgramFormSectionFeature(
				"program", "Program", am);
		activityForm.add(programFeature);

		crossCuttingIssues = new AmpCrossCuttingIssuesFormSectionFeature(
				"crossCuttingIssues", "Cross Cutting Issues", am);
		activityForm.add(crossCuttingIssues);

		sectorsFeature = new AmpSectorsFormSectionFeature(
				"sectors", "Sectors", am);
		activityForm.add(sectorsFeature);

		donorFundingFeature = new AmpDonorFundingFormSectionFeature(
				"donorFunding", "Donor Funding", am);
		activityForm.add(donorFundingFeature);

		relatedOrganizations = new AmpRelatedOrganizationsFormSectionFeature(
				"relatedOrganizations", "Related Organizations", am);
		activityForm.add(relatedOrganizations);
		components = new AmpComponentsFormSectionFeature("components", "Components", am);
		activityForm.add(components);

		structures = new AmpStructuresFormSectionFeature("structures", "Structures", am);
		activityForm.add(structures);
		
		issues = new AmpIssuesFormSectionFeature("issues", "Issues Section", am);
		activityForm.add(issues);
		
		regionalObs = new AmpRegionalObservationsFormSectionFeature("regionalObs", "Regional Observations", am);
		activityForm.add(regionalObs);

		contacts = new AmpContactsFormSectionFeature("contacts", "Contacts", am);
		activityForm.add(contacts);
		
		contracts = new AmpContractingFormSectionFeature("contracts", "Contracts", am);
		activityForm.add(contracts);
		
		me = new AmpMEFormSectionFeature("me", "M&E", am);
		activityForm.add(me);

		pi = new AmpPIFormSectionFeature("pi", "Paris Indicators", am);
		activityForm.add(pi);
		
		resources = new AmpResourcesFormSectionFeature("resources", "Related Documents", am);
		activityForm.add(resources);
	}

}
