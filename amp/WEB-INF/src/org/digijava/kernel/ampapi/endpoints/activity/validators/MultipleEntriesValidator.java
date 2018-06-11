/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Validates that multiple values are provided only when it is allowed
 * 
 * @author Nadejda Mandrescu, Emanuel Perez
 */
public class MultipleEntriesValidator extends InputValidator {
    
    private boolean isSizeLimit = false;

    @Override
    public ApiErrorMessage getErrorMessage() {
        if (isSizeLimit) {
            return ActivityErrors.FIELD_TOO_MANY_VALUES_NOT_ALLOWED;
        }
        
        return ActivityErrors.FIELD_MULTIPLE_VALUES_NOT_ALLOWED;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
                           Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
        boolean isValid = true;
        boolean multipleValuesAllowed = Boolean.TRUE.equals(fieldDescription.isMultipleValues());
        String fieldName = fieldDescription.getFieldName();
        Object fieldValue = newFieldParent.get(fieldName);
        Object sizeLimit = fieldDescription.getSizeLimit();
        if (multipleValuesAllowed) {
            if (sizeLimit != null && hasManyElements(fieldValue, getLong(sizeLimit))) {
                isSizeLimit = true;
                isValid = false;
            }
        } else if (hasManyElements(fieldValue, 1)) {
            isValid = false;
        }
        
        return isValid;
    }

    /**
     * Checks if an Object is a Collection with more than one element
     * 
     * @param fieldValue, the Object to check
     * @return true if the Object is a Collection with many elements, false otherwise
     */
    private boolean hasManyElements(Object fieldValue, long limit) {
        return fieldValue != null && fieldValue instanceof Collection && ((Collection) fieldValue).size() > limit;
    }

}
