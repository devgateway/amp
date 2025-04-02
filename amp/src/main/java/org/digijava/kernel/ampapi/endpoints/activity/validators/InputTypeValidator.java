package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.validator.routines.FloatValidator;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldType;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.common.util.DateTimeUtil;


/**
 * Verifies that data type matches the one defined in field description
 *
 * @author Nadejda Mandrescu
 */
public class InputTypeValidator extends InputValidator {

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.FIELD_INVALID_TYPE;
    }

    private boolean isStringValid(Object item, boolean translatable) {
        if (translatable) {
            if (Map.class.isAssignableFrom(item.getClass())) {
                return isTranslatableStringValid(item);
            }
            return false;
        }
        return String.class.isAssignableFrom(item.getClass());
    }

    private boolean isTranslatableStringValid(Object item) {
        Map map = (Map) item;
        
        for (Object key : ((Map) item).keySet()) {
            if (!String.class.isAssignableFrom(key.getClass())) {
                return false;
            }
        }
        
        for (Map.Entry<String, Object> castedEntry : ((Map<String, Object>) map).entrySet()) {
            if (castedEntry.getValue() != null && !String.class.isAssignableFrom(castedEntry.getValue().getClass())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
            APIField fieldDescription, String fieldPath) {
        FieldType fieldType = fieldDescription.getApiType().getFieldType();
        String fieldName = fieldDescription.getFieldName();
        Object item = newFieldParent.get(fieldName);

        if (item == null) {
            return true;
        }

        return isValidByType(importer, fieldDescription, fieldType, item);
    }

    private boolean isValidByType(ObjectImporter importer, APIField fieldDesc, FieldType fieldType, Object item) {
        switch (fieldType) {
            case STRING: return isStringValid(item, Boolean.TRUE.equals(fieldDesc.isTranslatable()));
            case DATE: return isValidDateTime(item, false);
            case TIMESTAMP: return isValidDateTime(item, true);
            case FLOAT: return isValidFloat(item);
            case BOOLEAN: return isValidBoolean(item);
            case LIST: return checkListFieldValidity(importer, item, fieldDesc);
            case OBJECT: return checkObjectFieldValidity(item);
            case LONG: return isValidLong(item);
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

    private boolean isValidDateTime(Object value, boolean isTimestamp) {
        try {
            return value == null
                    || value instanceof String
                    && DateTimeUtil.parseISO8601DateTimestamp((String) value, isTimestamp) != null;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private boolean checkListFieldValidity(ObjectImporter importer, Object item, APIField fieldDescription) {
        // for simple lists OR objects with sub-fields
        if (List.class.isAssignableFrom(item.getClass())) {
            if (fieldDescription.getApiType().isSimpleItemType()) {
                List<?> items = (List<?>) item;
                return items.stream().allMatch(elem ->
                    this.isValidByType(importer, fieldDescription, fieldDescription.getApiType().getItemType(), elem));
            }
            return true;
        }
        return Map.class.isAssignableFrom(item.getClass());
    }

    private boolean checkObjectFieldValidity(Object item) {
        return Map.class.isAssignableFrom(item.getClass());
    }

}
