package org.digijava.module.aim.validator.approval;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.validator.ActivityValidationContext;

/**
 * @author Nadejda Mandrescu
 */
public class AllowedApproverConstraint implements ConstraintValidator<AllowedApprover, AmpTeamMember> {

    @Override
    public void initialize(AllowedApprover constraintAnnotation) {
    }

    @Override
    public boolean isValid(AmpTeamMember value, ConstraintValidatorContext context) {
        ActivityValidationContext avc = ActivityValidationContext.getOrThrow();
        AmpActivityFields newA = avc.getNewActivity();
        AmpActivityFields oldA = avc.getOldActivity();

        if (oldA != null && oldA.getApprovedBy() != null && value == null) {
            return false;
        }

        ApprovalStatus approvalStatus = newA.getApprovalStatus();
        if (approvalStatus == null) {
            return value == null;
        }
        if (Constants.ACTIVITY_NEEDS_APPROVAL_STATUS_SET.contains(approvalStatus)) {
            // Not validating past approvedBy.
            // It may not match with the old one, since in AMP Offline could have been approved and then edited.
            return true;
        }

        AmpTeamMember modifiedBy = newA.getModifiedBy();
        AmpTeamMember approvedBy = newA.getApprovedBy();
        ApprovalStatus oldApprovalStatus = oldA == null ? null : oldA.getApprovalStatus();
        if (!modifiedBy.equals(approvedBy)) {
            if (oldApprovalStatus != null && ActivityUtil.isProjectValidationForNewOnly(modifiedBy)) {
                AmpTeamMember oldApprovedBy = oldA.getApprovedBy();
                // New only validation: once approved, new edits do not update the approver (the old one is kept)
                // -> the "new" approved_by from JSON can be actually the correct initial approver,
                // but as lone as it wasn't reapproved (detectable through approval_date change)
                if (oldApprovedBy != null && oldA.getApprovalDate().equals(newA.getApprovalDate())) {
                    return oldApprovedBy.equals(approvedBy);
                }
                // otherwise in AMP Offline: can be approved by user A, edited by user B and then synced ->
                // hence we'll validate below that "user A" is still allowed to be recorded as approver during import
            } else {
                return false;
            }
        }

        Long teamId = newA.getTeam().getAmpTeamId();
        return approvedBy != null && ActivityUtil.canApprove(approvedBy, teamId, oldApprovalStatus);
    }

}
