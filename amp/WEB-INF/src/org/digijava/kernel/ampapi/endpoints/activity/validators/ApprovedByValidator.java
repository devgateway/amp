package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Date;
import java.util.Map;

import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.common.util.DateTimeUtil;


/**
 * @author Nadejda Mandrecsu
 */
public class ApprovedByValidator extends InputValidator {

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.FIELD_INVALID_VALUE;
    }

    @Override
    public boolean isValid(ObjectImporter objImporter, Map<String, Object> newFieldParent, APIField fieldDesc,
            String fieldPath) {
        ActivityImporter importer = (ActivityImporter) objImporter;
        if (importer.getImportRules().isProcessApprovalFields()) {
            if (FieldMap.underscorify(ActivityFieldsConstants.APPROVED_BY).equals(fieldDesc.getFieldName())) {
                return isValidApprovedBy(importer, newFieldParent, fieldDesc);
            }
        }
        return true;
    }

    private boolean isValidApprovedBy(ActivityImporter importer, Map<String, Object> newFieldParent,
            APIField fieldDesc) {
        Long asId = getLong(newFieldParent.get(FieldMap.underscorify(ActivityFieldsConstants.APPROVAL_STATUS)));
        Long approvedById = getLong(newFieldParent.get(fieldDesc.getFieldName()));
        if (asId == null) {
            return approvedById == null;
        }

        ApprovalStatus as;
        try {
            as = ApprovalStatus.fromId(asId.intValue());
        } catch (IllegalArgumentException ex) {
            return false;
        }

        if (Constants.ACTIVITY_NEEDS_APPROVAL_STATUS_SET.contains(as)) {
            // Not validating past approvedBy.
            // It may not match with the old one, since in AMP Offline could have been approved and then edited.
            return true;
        }

        AmpTeamMember modifiedBy = importer.getModifiedBy();
        AmpTeamMember approvedBy = TeamMemberUtil.getAmpTeamMember(approvedById);
        ApprovalStatus oas = importer.getOldActivity() == null ? null : importer.getOldActivity().getApprovalStatus();
        if (!modifiedBy.getAmpTeamMemId().equals(approvedById)) {
            if (oas != null && ActivityUtil.isProjectValidationForNewOnly(approvedBy)) {
                AmpTeamMember oa = importer.getOldActivity().getApprovedBy();
                Object approvalDate = getNewApprovalDate(newFieldParent);
                // New only validation: once approved, new edits do not update the approver (the old one is kept)
                // -> the "new" approved_by from JSON can be actually the correct initial approver,
                // but as lone as it wasn't reapproved (detectable through approval_date change)
                if (oa != null && importer.getOldActivity().getApprovalDate().equals(approvalDate)) {
                    return oa.getAmpTeamMemId().equals(approvedById);
                }
                // otherwise in AMP Offline: can be approved by user A, edited by user B and then synced ->
                // hence we'll validate below that "user A" is still allowed to be recorded as approver during import
            } else {
                return false;
            }
        }

        Long teamId = getLong(newFieldParent.get(FieldMap.underscorify(ActivityFieldsConstants.TEAM)));
        return ActivityUtil.canApprove(approvedBy, teamId, oas);
    }

    private Date getNewApprovalDate(Map<String, Object> newFieldParent) {
        try {
            String dateStr = (String) newFieldParent.get(FieldMap.underscorify(ActivityFieldsConstants.APPROVAL_DATE));
            return DateTimeUtil.parseISO8601Timestamp(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

}
