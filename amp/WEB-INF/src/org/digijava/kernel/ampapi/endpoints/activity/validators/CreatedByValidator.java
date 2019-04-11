package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;

/**
 * @author Nadejda Mandrescu
 */
public class CreatedByValidator extends InputValidator {

    private static final String CREATED_BY_FIELD = FieldMap.underscorify(ActivityFieldsConstants.CREATED_BY);

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.FIELD_INVALID_VALUE;
    }

    @Override
    public boolean isValid(ObjectImporter objImporter, Map<String, Object> newFieldParent, APIField fieldDesc,
            String fieldPath) {
        ActivityImporter importer = (ActivityImporter) objImporter;
        if (CREATED_BY_FIELD.equals(fieldDesc.getFieldName())) {
            if (importer.getImportRules().isTrackEditors()) {
                return isValidCreatedBy(importer, newFieldParent);
            }
        }

        return true;
    }

    private boolean isValidCreatedBy(ActivityImporter importer, Map<String, Object> parent) {
        Long cId = getLong(parent.get(CREATED_BY_FIELD));
        if (importer.getOldActivity() == null) {
            return cId != null;
        }
        /* TODO AMP-28993: uncomment to also allow null created by
        return cId == null || importer.getOldActivity().getActivityCreator().getAmpTeamMemId().equals(cId);
         */
        return importer.getOldActivity().getActivityCreator().getAmpTeamMemId().equals(cId);
    }

}
