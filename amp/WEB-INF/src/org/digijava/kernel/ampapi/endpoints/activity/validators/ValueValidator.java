package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
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
        
        //temporary debug
        if (!isValidLength(newFieldParent, fieldDescription))
            return false;
        if (!isValidPercentage(newFieldParent, fieldDescription)) 
            return false;

        // FIXME possible values are not always available for all fields, must check only for select fields (not all)
        List<PossibleValue> possibleValues = importer.getPossibleValuesForFieldCached(fieldPath);
        
        if (possibleValues.size() != 0) {
            Object value = newFieldParent.get(fieldDescription.getFieldName());
            
            if (value != null) {
                boolean idOnly = Boolean.TRUE.equals(fieldDescription.isIdOnly());
                // convert to string the ids to avoid long-integer comparison
                String valueStr = value.toString();
                if (idOnly) {
                    if (findById(possibleValues, valueStr) != null) {
                        return true;
                    }
                } else {
                    if (findByValue(possibleValues, valueStr) != null) {
                        return true;
                    }
                }
                // wrong value configured if it is not found in allowed options
                return false;
            }
        }
        // nothing failed so far? then we are good to go
        return true;
    }

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

    private PossibleValue findByValue(List<PossibleValue> possibleValues, String value) {
        for (PossibleValue possibleValue : possibleValues) {
            if (value.equals(possibleValue.getValue())) {
                return possibleValue;
            }
            PossibleValue childPossibleValue = findByValue(possibleValue.getChildren(), value);
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
