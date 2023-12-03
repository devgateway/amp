package org.digijava.module.aim.validator.approval;

import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.validator.ActivityValidationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.digijava.module.aim.dbentity.ApprovalStatus.rejected;

/**
 * @author Nadejda Mandrescu
 */
public class ApprovalStatusConstraint implements ConstraintValidator<AllowedApprovalStatus, ApprovalStatus> {

    @Override
    public void initialize(AllowedApprovalStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(ApprovalStatus approvalStatus, ConstraintValidatorContext context) {

        if (approvalStatus == null) {
            return false;
        }

        ActivityValidationContext avc = ActivityValidationContext.getOrThrow();
        AmpActivityFields oldA = avc.getOldActivity();
        AmpActivityFields activity = avc.getNewActivity();

        boolean isNew = ActivityUtil.isNewActivity(activity);
        boolean oldDraft = oldA != null ? oldA.getDraft() : false;

        if (Constants.ACTIVITY_NEEDS_APPROVAL_STATUS_SET.contains(approvalStatus)) {
            if (!ActivityUtil.isProjectValidationOn()) {
                return false;
            }

            Long activityTeamId = activity.getTeam().getAmpTeamId();

            if (rejected.equals(approvalStatus)) {
                return activity.getDraft() && ActivityUtil.canReject(activity.getModifiedBy(), oldDraft, isNew);
            }

            ApprovalStatus oas = oldA == null ? null : oldA.getApprovalStatus();
            return activity.getDraft() || !ActivityUtil.canApprove(activity.getModifiedBy(), activityTeamId, oas);
        } else {
            return ActivityUtil.canApproveWith(approvalStatus, activity.getApprovedBy(), isNew, activity.getDraft());
        }
    }

}
