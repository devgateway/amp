/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.validator.routines.FloatValidator;
import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;


/**
 * Verifies that data type matches the one defined in field description
 * 
 * @author Nadejda Mandrescu
 */
public class InputTypeValidator extends InputValidator {

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.FIELD_INVALID_TYPE;
    }
    
    private boolean isStringValid(Object item, boolean translatable, Collection<String> supportedLocaleCodes) {
        if (translatable) {
            if (Map.class.isAssignableFrom(item.getClass())) {
                return isTranslatableStringValid(item, supportedLocaleCodes);
            }
        }
        if (String.class.isAssignableFrom(item.getClass()))
            return true;
        return false;
    }
    
    private boolean isTranslatableStringValid(Object item, Collection<String> supportedLocaleCodes) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) item;
        for (Map.Entry<String, Object> castedEntry : map.entrySet()) {
            if (!supportedLocaleCodes.contains(castedEntry.getKey()))
                return false;           
            if (castedEntry.getValue() != null && !String.class.isAssignableFrom(castedEntry.getValue().getClass()))
                return false;
        }
        return true;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
        String fieldType = fieldDescription.getFieldType();
        String fieldName = fieldDescription.getFieldName();
        Object item = newFieldParent.get(fieldName);
        
        if (item == null) {
            return true;
        }
        
        switch (fieldType) {
        case ActivityEPConstants.FIELD_TYPE_STRING :
            return isStringValid(item, Boolean.TRUE.equals(fieldDescription.isTranslatable()),
                    importer.getTrnSettings().getAllowedLangCodes());
        case ActivityEPConstants.FIELD_TYPE_DATE: return isValidDate(item);
        case ActivityEPConstants.FIELD_TYPE_FLOAT: return isValidFloat(item);
        case ActivityEPConstants.FIELD_TYPE_BOOLEAN : return isValidBoolean(item);
        case ActivityEPConstants.FIELD_TYPE_LIST: return checkListFieldValidity(item, fieldDescription);
        case ActivityEPConstants.FIELD_TYPE_LONG: return isValidLong(item);
        default: return false; 
        }
    }

    private boolean isValidLong(Object item) {
        if (Long.class.isAssignableFrom(item.getClass()))
            return true;
        if (Integer.class.isAssignableFrom(item.getClass()))
            return true;
        return false;
    }

    /**
     * Adding Locale.US to explicitly say that 100.28 values are processed, not 100,28
     * @param item
     * @return
     */
    private boolean isValidFloat(Object item) {
        return FloatValidator.getInstance().isValid(item.toString(), Locale.US);
    }

    private boolean isValidBoolean(Object value) {
        if (Boolean.class.isAssignableFrom(value.getClass()))
            return true;
        return false;
    }

    private boolean isValidDate(Object value) {
        return value == null || 
                value instanceof String 
                && InterchangeUtils.parseISO8601Date((String) value) != null;
    }

    private boolean checkListFieldValidity(Object item, APIField fieldDescription) {
        // for simple lists OR objects with sub-fields
        if (List.class.isAssignableFrom(item.getClass()) || Map.class.isAssignableFrom(item.getClass())) 
            return true;
        return false;
    }

}
