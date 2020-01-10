package org.digijava.kernel.ampapi.endpoints.activity.validators;

import static org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils.getDoubleFromJsonNumber;
import static org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils.getLongOrNullOnError;
import static org.digijava.kernel.ampapi.endpoints.activity.SaveMode.DRAFT;

import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.common.values.PossibleValuesCache;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;

/**
 * Validates that field value is allowed
 *
 * @author Nadejda Mandrescu
 */
public class ValueValidator extends InputValidator {

    private static final double MAXIMUM_PERCENTAGE = 100;

    private boolean isValidLength = true;
    private boolean isValidPercentage = true;
    private boolean draftDisabled = false;

    @Override
    public ApiErrorMessage getErrorMessage() {
        if (!isValidLength)
            return ValidationErrors.FIELD_INVALID_LENGTH;
        if (!isValidPercentage)
            return ValidationErrors.FIELD_INVALID_PERCENTAGE;
        if (draftDisabled) {
            return ActivityErrors.SAVE_AS_DRAFT_FM_DISABLED;
        }
        return ValidationErrors.FIELD_INVALID_VALUE;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
            APIField fieldDescription, String fieldPath) {

        boolean importable = fieldDescription.isImportable();
        // input type, allowed input will be verified before, so nothing check here
        if (!importable)
            return true;

        if (!isValidLength(newFieldParent, fieldDescription))
            return false;
        if (!isValidPercentage(newFieldParent, fieldDescription)) {
            return false;
        }
        if (!isValidDraft(importer, newFieldParent, fieldDescription)) {
            return false;
        }

        PossibleValuesCache pvc = importer.getPossibleValuesCache();
        Object value = newFieldParent.get(fieldDescription.getFieldName());
        String cPVPath = fieldDescription.getCommonPossibleValuesPath();

        if (pvc.hasPossibleValues(fieldPath, cPVPath) && value != null) {
            if (fieldDescription.getApiType().getFieldType().isList()) {
                if (fieldDescription.getApiType().isSimpleItemType()) {
                    return ((List<?>) value).stream().allMatch(
                            v -> pvc.isAllowed(getLongOrNullOnError(v), fieldPath, cPVPath));
                }
                // possible values definition allowed at simple type list level only
                return false;
            } else {
                return pvc.isAllowed(getLongOrNullOnError(value), fieldPath, cPVPath);
            }
        }
        return true;
    }

    private boolean isValidPercentage(Map<String, Object> newFieldParent,
            APIField fieldDescription) {
        this.isValidPercentage  = true;
        if (!Boolean.TRUE.equals(fieldDescription.getPercentage())) {
            return true; //this doesn't contain a percentage-based field
        }

        //attempt to get the number out of this one
        Double val = getDoubleFromJsonNumber(newFieldParent.get(fieldDescription.getFieldName()));
        if (val != null
                && (val < ActivityEPConstants.EPSILON || val - MAXIMUM_PERCENTAGE > ActivityEPConstants.EPSILON)) {
            this.isValidPercentage = false;
            return false;
        }

        return true;
    }

    private boolean isValidLength(Map<String, Object> newFieldParent, APIField fieldDescription) {
        isValidLength = true;
        Integer maxLength = fieldDescription.getFieldLength();
        if (maxLength != null) {
            Object obj = newFieldParent.get(fieldDescription.getFieldName());
            if (obj != null) {
                if (!Boolean.TRUE.equals(fieldDescription.isTranslatable())) {
                    isValidLength = isValidLength(obj, maxLength);
                } else if (Map.class.isAssignableFrom(obj.getClass())) {
                    for (Object trn : ((Map) obj).values()) {
                        if (!isValidLength(trn, maxLength)) {
                            isValidLength = false;
                            break;
                        }
                    }
                    // translatable means its input must be a map, otherwise invalid input was provided, so we cannot say it's invalid
                }
            }
        }
        return isValidLength;
    }

    private boolean isValidLength(Object obj, Integer maxLength) {
        if (obj == null)
            return true;
        if (String.class.isAssignableFrom(obj.getClass())){
            if (maxLength < ((String) obj).length())
                return false;
        }
        return true;
    }

    private boolean isValidDraft(ObjectImporter importer, Map<String, Object> newFieldParent,
            APIField fieldDescription) {
        String draftFieldName = FieldMap.underscorify(ActivityFieldsConstants.IS_DRAFT);
        if (draftFieldName.equals(fieldDescription.getFieldName())) {
            if (!(importer instanceof ActivityImporter)) {
                throw new RuntimeException("Draft flag not supported for " + importer.getClass());
            }
            ActivityImporter activityImporter = (ActivityImporter) importer;
            if (activityImporter.getRequestedSaveMode() == DRAFT && !activityImporter.isDraftFMEnabled()) {
                this.draftDisabled = true;
                return false;
            }
        }
        return true;
    }
}
