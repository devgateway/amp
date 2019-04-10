package org.digijava.module.aim.validator.approval;

import static org.digijava.module.aim.dbentity.ApprovalStatus.REJECTED;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.validator.ActivityValidationContext;

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
        if (Constants.ACTIVITY_NEEDS_APPROVAL_STATUS_SET.contains(approvalStatus)) {
            if (!ActivityUtil.isProjectValidationOn()) {
                return false;
            }

            Long activityTeamId = activity.getTeam().getAmpTeamId();

            if (REJECTED.equals(approvalStatus)) {
                return ActivityUtil.canReject(activity.getModifiedBy(), activityTeamId, activity.getDraft());
            }

            ActivityValidationContext avc = activity.getActivityValidationContext();
            if (avc == null) {
                throw new RuntimeException("ActivityValidationContext not configured");
            }
            AmpActivityFields oldA = avc.getOldActivity();
            org.digijava.module.aim.dbentity.ApprovalStatus oas = oldA == null ? null : oldA.getApprovalStatus();
            return activity.getDraft() || !ActivityUtil.canApprove(activity.getModifiedBy(), activityTeamId, oas);

        } else {
            boolean isSubmitted = Boolean.FALSE.equals(activity.getDraft());
            boolean isNew = ActivityUtil.isNewActivity(activity);
            return isSubmitted && ActivityUtil.canApproveWith(approvalStatus, activity.getApprovedBy(), isNew);
        }
    }

}
