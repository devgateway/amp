package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.List;
import java.util.Map;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Validates that field value is allowed
 * 
 * @author Nadejda Mandrescu
 */
public class ValueValidator extends InputValidator {

    private static final double MAXIMUM_PERCENTAGE = 100;

    private boolean isValidLength = true;
    private boolean isValidPercentage = true;

    @Override
    public ApiErrorMessage getErrorMessage() {
        if (!isValidLength)
            return ActivityErrors.FIELD_INVALID_LENGTH;
        if (!isValidPercentage)
            return ActivityErrors.FIELD_INVALID_PERCENTAGE;
        return ActivityErrors.FIELD_INVALID_VALUE;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
        
        boolean importable = fieldDescription.isImportable();
        // input type, allowed input will be verified before, so nothing check here 
        if (!importable)
            return true;
        
        if (!isValidLength(newFieldParent, fieldDescription))
            return false;
        if (!isValidPercentage(newFieldParent, fieldDescription)) 
            return false;

        // FIXME possible values are not always available for all fields, must check only for select fields (not all)
        List<PossibleValue> possibleValues = importer.getPossibleValuesForFieldCached(fieldPath);
        Object value = newFieldParent.get(fieldDescription.getFieldName());
        
        if (possibleValues.size() != 0 && value != null) {
            if (fieldDescription.getApiType().getFieldType().isList()) {
                if (fieldDescription.getApiType().isSimpleItemType()) {
                    return ((List<?>) value).stream().allMatch(v -> isAllowedValue(possibleValues, v));
                }
                // possible values definition allowed at simple type list level only
                return false;
            } else {
                return isAllowedValue(possibleValues, value);
            }
        }
        return true;
    }
    
    private boolean isAllowedValue(List<PossibleValue> possibleValues, Object value) {
        // convert to string the ids to avoid long-integer comparison
        return findById(possibleValues, String.valueOf(value)) != null;
    }

    // TODO it would be nice if possible values could be extended to retrieve one single possible value by id.
    // this will reduce this operation from O(n) to O(log N) or O(1)
    // reason: fields can repeat and may have thousands of possible values
    private PossibleValue findById(List<PossibleValue> possibleValues, String id) {
        for (PossibleValue possibleValue : possibleValues) {
            if (id.equals(possibleValue.getId().toString())) {
                return possibleValue;
            }
            PossibleValue childPossibleValue = findById(possibleValue.getChildren(), id);
            if (childPossibleValue != null) {
                return childPossibleValue;
            }
        }
        return null;
    }

    private boolean isValidPercentage(Map<String, Object> newFieldParent,
            APIField fieldDescription) {
        this.isValidPercentage  = true;
        if (!Boolean.TRUE.equals(fieldDescription.getPercentage())) {
            return true; //this doesn't contain a percentage-based field
        }

        //attempt to get the number out of this one
        Double val = InterchangeUtils.getDoubleFromJsonNumber(newFieldParent.get(fieldDescription.getFieldName()));
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
}
