package org.digijava.kernel.ampapi.endpoints.activity.validators;

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
        if (importer.isProcessApprovalFields()) {
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
        if (!modifiedBy.getAmpTeamMemId().equals(approvedById)) {
            return false;
        }

        Long teamId = getLong(newFieldParent.get(FieldMap.underscorify(ActivityFieldsConstants.TEAM)));
        AmpTeamMember atm = TeamMemberUtil.getAmpTeamMember(approvedById);
        ApprovalStatus oas = importer.getOldActivity() == null ? null : importer.getOldActivity().getApprovalStatus();
        return ActivityUtil.canApprove(atm, teamId, oas);
    }

}
