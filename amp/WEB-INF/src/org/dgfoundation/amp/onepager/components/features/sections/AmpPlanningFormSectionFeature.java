/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentSimpleWrapper;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentTab;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Implements the planning section of the one pager
 * 
 * @author mpostelnicu@dgateway.org since Oct 5, 2010
 * @see OnePager
 */
public class AmpPlanningFormSectionFeature extends AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = -6658492193242970618L;

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPlanningFormSectionFeature(String id, String fmName,
			final IModel<AmpActivity> actModel) throws Exception {
		super(id, fmName, actModel);

		RangeValidator<Integer> rankValidator = new RangeValidator<Integer>(1,5);
		AttributeModifier rankModifier = new AttributeModifier("size",new Model(2));
		
		AmpTextFieldPanel<Integer> lineMinistryRank = new AmpTextFieldPanel<Integer>(
				"lineMinistryRank", new PropertyModel<Integer>(actModel, "lineMinRank"), "Line Ministry Rank");
		lineMinistryRank.getTextContainer().add(rankValidator);
		lineMinistryRank.getTextContainer().add(rankModifier);
		add(lineMinistryRank);
		
		AmpTextFieldPanel<Integer> planningMinistryRank = new AmpTextFieldPanel<Integer>(
				"planningMinistryRank", new PropertyModel<Integer>(actModel, "planMinRank"), "Ministry of Planning Rank");
		planningMinistryRank.getTextContainer().add(rankValidator);
		planningMinistryRank.getTextContainer().add(rankModifier);
		add(planningMinistryRank);
		
		
		AmpDatePickerFieldPanel dateOfActualCompletion = new AmpDatePickerFieldPanel(
				"dateOfActualCompletion", new PropertyModel<Date>(actModel,
						"actualCompletionDate"), null,
				"Date of Actual Completion");
		add(dateOfActualCompletion);

		AmpDatePickerFieldPanel dateOfSignedAgreement = new AmpDatePickerFieldPanel(
				"dateOfSignedAgreement", new PropertyModel<Date>(actModel,
						"actualApprovalDate"), dateOfActualCompletion,
				"Date of Signed Agreement");
		add(dateOfSignedAgreement);

		AmpDatePickerFieldPanel proposedStartDate = new AmpDatePickerFieldPanel(
				"proposedStartDate", new PropertyModel<Date>(actModel,
						"proposedStartDate"), null, "Proposed Start Date");
		add(proposedStartDate);

		AmpDatePickerFieldPanel dateOfEffectiveAgreement = new AmpDatePickerFieldPanel(
				"dateOfEffectiveAgreement", new PropertyModel<Date>(actModel,
						"actualStartDate"), dateOfActualCompletion,
				"Date of Effective Agreement");
		add(dateOfEffectiveAgreement);

		AmpDatePickerFieldPanel finalDateForContracting = new AmpDatePickerFieldPanel(
				"finalDateForContracting", new PropertyModel<Date>(actModel,
						"contractingDate"), null, "Final Date for Contracting");
		add(finalDateForContracting);

		AmpDatePickerFieldPanel finalDateForDisbursements = new AmpDatePickerFieldPanel(
				"finalDateForDisbursements", new PropertyModel<Date>(actModel,
						"disbursmentsDate"), null,
				"Final Date for Disbursements");
		add(finalDateForDisbursements);
				
		AmpCommentSimpleWrapper acsw = new AmpCommentSimpleWrapper("finalDateDisbTabs", "Final Date For Disbursements", actModel);
		acsw.setOutputMarkupId(true);
		add(acsw);

		AmpDatePickerFieldPanel dateOfPlannedCompletion = new AmpDatePickerFieldPanel(
				"dateOfPlannedCompletion", new PropertyModel<Date>(actModel,
						"originalCompDate"), null, "Date of Planned Completion");
		add(dateOfPlannedCompletion);

		AmpDatePickerFieldPanel revisedCompletionDate = new AmpDatePickerFieldPanel(
				"revisedCompletionDate", new PropertyModel<Date>(actModel,
						"actualCompletionDate"), null,
				"Revised Completion Date");
		add(revisedCompletionDate);

		AmpCommentSimpleWrapper acsw2 = new AmpCommentSimpleWrapper("revisedComplDateTabs", "current completion date", actModel);
		acsw2.setOutputMarkupId(true);
		add(acsw2);

		AmpCategorySelectFieldPanel status = new AmpCategorySelectFieldPanel(
				"status", CategoryConstants.ACTIVITY_STATUS_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(actModel,"categories"),
						CategoryConstants.ACTIVITY_STATUS_KEY),
						CategoryConstants.ACTIVITY_STATUS_NAME, true, false, null);
		add(status);
		
		add(new AmpTextAreaFieldPanel<String>("statusReason",
				new PropertyModel<String>(actModel, "statusReason"), "Status Reason", true));
		

	
	}

}
