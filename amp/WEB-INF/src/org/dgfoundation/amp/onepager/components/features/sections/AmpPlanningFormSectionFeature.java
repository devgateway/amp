/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.Date;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentSimpleWrapper;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Implements the planning section of the one pager
 * 
 * @author mpostelnicu@dgateway.org since Oct 5, 2010
 */
public class AmpPlanningFormSectionFeature extends AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = -6658492193242970618L;

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPlanningFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> actModel) throws Exception {
		super(id, fmName, actModel);
		this.fmType = AmpFMTypes.MODULE;
		
		RangeValidator<Integer> rankValidator = new RangeValidator<Integer>(1,5);
		AttributeModifier rankModifier = new AttributeModifier("size",new Model(2));
		
		AmpTextFieldPanel<Integer> lineMinistryRank = new AmpTextFieldPanel<Integer>(
				"lineMinistryRank", new PropertyModel<Integer>(actModel, "lineMinRank"), "Line Ministry Rank", AmpFMTypes.MODULE);
		lineMinistryRank.getTextContainer().add(rankValidator);
		lineMinistryRank.getTextContainer().add(rankModifier);
		add(lineMinistryRank);
		
		AmpTextFieldPanel<Integer> planningMinistryRank = new AmpTextFieldPanel<Integer>(
				"planningMinistryRank", new PropertyModel<Integer>(actModel, "planMinRank"), "Ministry of Planning Rank", AmpFMTypes.MODULE);
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
				
		AmpCommentSimpleWrapper acsw = new AmpCommentSimpleWrapper("finalDateDisbTabs", "Final Date for Disbursements", actModel);
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
	}

}
