package org.digijava.module.aim.validator.approval;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.helper.Constants;

/**
 * @author Nadejda Mandrescu
 */
public class ApprovalStatusConstraint implements ConstraintValidator<ApprovalStatus, AmpActivityFields> {

    @Override
    public void initialize(ApprovalStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(AmpActivityFields activity, ConstraintValidatorContext context) {
        org.digijava.module.aim.dbentity.ApprovalStatus approvalStatus =  activity.getApprovalStatus();
        if (approvalStatus == null) {
            return false;
        }
        if (!Constants.ACTIVITY_NEEDS_APPROVAL_STATUS_SET.contains(activity.getApprovalStatus())) {
            boolean isSubmitted = Boolean.FALSE.equals(activity.getDraft());
            boolean isNew = ActivityUtil.isNewActivity(activity);
            return isSubmitted && ActivityUtil.canApproveWith(approvalStatus, activity.getApprovedBy(), isNew);
        }
        return true;
    }

}
