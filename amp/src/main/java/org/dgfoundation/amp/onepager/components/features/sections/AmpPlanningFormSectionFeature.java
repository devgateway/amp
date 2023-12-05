/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentSimpleWrapper;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implements the planning section of the one pager
 * 
 * @author mpostelnicu@dgateway.org since Oct 5, 2010
 */
public class AmpPlanningFormSectionFeature extends AmpFormSectionFeaturePanel implements AmpRequiredComponentContainer {

    private static final long serialVersionUID = -6658492193242970618L;
    private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>>();
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
        
                
        
        AmpDatePickerFieldPanel actualCompletionDate = new AmpDatePickerFieldPanel(
                "actualCompletionDate", new PropertyModel<Date>(actModel,
                        "actualCompletionDate"), null,
                "Actual Completion Date");
        add(getRequiredDateValidator(actualCompletionDate, "actualCompletionDateRequired", "Actual Completion Date"));
        add(actualCompletionDate);
        

        AmpDatePickerFieldPanel proposedCompletionDate = new AmpDatePickerFieldPanel(
                "proposedCompletionDate", new PropertyModel<Date>(actModel,
                        "proposedCompletionDate"), null,
                "Proposed Completion Date");
        add(proposedCompletionDate);

        AmpDatePickerFieldPanel proposedApprovalDate = new AmpDatePickerFieldPanel(
                "proposedApprovalDate", new PropertyModel<Date>(actModel,
                "proposedApprovalDate"), null, "Proposed Approval Date");
        
        add(proposedApprovalDate);

        AmpDatePickerFieldPanel actualApprovalDate = new AmpDatePickerFieldPanel(
                "actualApprovalDate", new PropertyModel<Date>(actModel,
                        "actualApprovalDate"), proposedApprovalDate,
                "Actual Approval Date");
        
        add(getRequiredDateValidator(actualApprovalDate, "actualApprovalDateRequired", "Actual Approval Date"));
        add(actualApprovalDate);
        
        proposedApprovalDate.setDuplicateFieldOnChange(actualApprovalDate);
        
        final AmpDatePickerFieldPanel proposedStartDate = new AmpDatePickerFieldPanel(
                "proposedStartDate", new PropertyModel<Date>(actModel,
                        "proposedStartDate"), null, "Proposed Start Date");
        add(getRequiredDateValidator(proposedStartDate, "proposedStartDateRequired", "Proposed Start Date"));
        add(proposedStartDate);
        

                

        AmpDatePickerFieldPanel dateOfEffectiveAgreement = new AmpDatePickerFieldPanel(
                "actualStartDate", new PropertyModel<Date>(actModel,
                        "actualStartDate"), proposedStartDate,
                "Actual Start Date");
        add(dateOfEffectiveAgreement);
        proposedStartDate.setDuplicateFieldOnChange(dateOfEffectiveAgreement);
        
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

        final AmpDatePickerFieldPanel dateOfPlannedCompletion = new AmpDatePickerFieldPanel(
                "originalCompDate", new PropertyModel<Date>(actModel,
                        "originalCompDate"), null, "Original Completion Date");
        add(getRequiredDateValidator(dateOfPlannedCompletion, "originalCompDateRequired", "Original Completion Date"));
        add(dateOfPlannedCompletion);
        
        AmpCommentSimpleWrapper acsw2 = new AmpCommentSimpleWrapper("revisedComplDateTabs", "current completion date", actModel);
        acsw2.setOutputMarkupId(true);
        add(acsw2);
        
        /*
        * Removed 'in days' from 'Proposed Project Life (in days)' because it appears in Global FM in this way,
        * and wen doing a check FeaturesUtil.isVisibleModule("/Activity Form/Planning/Proposed Project Life")
        * */
        AmpTextFieldPanel<Integer> proposedProjectLife = new AmpTextFieldPanel<Integer>("proposedProjectLife", new PropertyModel<Integer>(actModel, "proposedProjectLife"),
                "Proposed Project Life", AmpFMTypes.MODULE);
        RangeValidator<Integer> proposedProjectLifeValidator = new RangeValidator<Integer>(0, 9999);
        proposedProjectLife.getTextContainer().add(proposedProjectLifeValidator);
        add(proposedProjectLife);
    }

    private AmpComponentPanel getRequiredDateValidator(AmpDatePickerFieldPanel dateObjectToValidate, String wicketId,
            String fmName) {
        return new AmpComponentPanel(wicketId, "Required Validator for " + fmName) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (this.isVisible()) {
                    dateObjectToValidate.getDate().setRequired(true);
                    requiredFormComponents.add(dateObjectToValidate.getDate());

                }
            }
        };
    }

    @Override
    public List<FormComponent<?>> getRequiredFormComponents() {
        // TODO Auto-generated method stub
        return requiredFormComponents;
    }

}
