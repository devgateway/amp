package org.digijava.module.aim.validator.approval;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
        if (activity.getApprovalStatus() == null) {
            return false;
        }
        if (!Constants.ACTIVITY_NEEDS_APPROVAL_STATUS_SET.contains(activity.getApprovalStatus())) {
            return Boolean.FALSE.equals(activity.getDraft());
        }
        return true;
    }

}
