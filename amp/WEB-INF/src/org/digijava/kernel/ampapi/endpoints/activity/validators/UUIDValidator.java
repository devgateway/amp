package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;

/**
 * Validates that the activity document uuid is present in the user private node
 *
 * @author Viorel Chihai
 */
public class UUIDValidator extends InputValidator {

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.FIELD_INVALID_VALUE;
    }
    
    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           APIField fieldDescription, String fieldPath) {
        return isValid(importer, null, newFieldParent, fieldDescription, fieldPath);
    }

    @Override
    public boolean isValid(ObjectImporter importer, Object currentObject, Map<String, Object> newFieldParent,
            APIField fieldDescription, String fieldPath) {
        
        if (fieldDescription.getFieldName().equals(FieldMap.underscorify(ActivityFieldsConstants.UUID))) {
            String uuid = null;
            if (newFieldParent.containsKey(fieldDescription.getFieldName())) {
                uuid = StringUtils.trim((String) newFieldParent.get(fieldDescription.getFieldName()));
            } else {
                uuid = fieldDescription.getFieldAccessor().get(currentObject);
            }
            return ((ActivityImporter) importer).getResourceService().getPrivateUuids().contains(uuid);
        }

        return true;
    }
}
