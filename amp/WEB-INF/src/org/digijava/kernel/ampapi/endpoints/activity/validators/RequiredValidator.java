package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.SaveMode;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Validates if required data is provided
 *
 * @author Nadejda Mandrescu
 */
public class RequiredValidator extends InputValidator {

    private boolean draftDisabled = false;

    @Override
    public ApiErrorMessage getErrorMessage() {
        if (this.draftDisabled) {
            return ActivityErrors.SAVE_AS_DRAFT_FM_DISABLED;
        }

        return ActivityErrors.FIELD_REQUIRED;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
            APIField fieldDescription, String fieldPath) {
        String fieldName = fieldDescription.getFieldName();
        Object fieldValue = newFieldParent.get(fieldName);
        String requiredStatus = fieldDescription.getUnconditionalRequired();
        boolean importable = fieldDescription.isImportable();
        // don't care if value has something
        if (importable && isEmpty(fieldValue)) {

            if (ActivityEPConstants.FIELD_ALWAYS_REQUIRED.equals(requiredStatus)) {
                // field is always required -> can't save it even as a draft
                return false;
            } else if (ActivityEPConstants.FIELD_NON_DRAFT_REQUIRED.equals(requiredStatus)) {
                if (!(importer instanceof ActivityImporter)) {
                    throw new RuntimeException("Draft save not supported for " + importer.getClass());
                }
                ActivityImporter activityImporter = (ActivityImporter) importer;
                // field required for submitted activities, but we can save it as a draft
                // unless it's disabled in FM
                if (activityImporter.getRequestedSaveMode() != SaveMode.DRAFT) {
                    if (activityImporter.getImportRules().isCanDowngradeToDraft()) {
                        if (activityImporter.isDraftFMEnabled()) {
                            activityImporter.downgradeToDraftSave();
                        } else {
                            this.draftDisabled = true;
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        // field value != null, it's fine from this validator's POV
        return true;
    }

    private boolean isEmpty(Object fieldValue) {
        return fieldValue == null || isEmptyString(fieldValue) || isEmptyCollection(fieldValue);
    }

    private boolean isEmptyString(Object fieldValue) {
        return fieldValue instanceof String && StringUtils.isBlank((String) fieldValue);
    }

    private boolean isEmptyCollection(Object fieldValue) {
        return Collection.class.isAssignableFrom(fieldValue.getClass()) && ((Collection<?>) fieldValue).size() == 0;
    }
}
