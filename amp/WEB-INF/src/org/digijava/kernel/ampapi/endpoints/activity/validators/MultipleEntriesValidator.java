/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Collection;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Validates that multiple values are provided only when it is allowed
 * 
 * @author Nadejda Mandrescu,Emanuel Perez
 */
public class MultipleEntriesValidator extends InputValidator {

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_MULTIPLE_VALUES_NOT_ALLOWED;
	}

	@Override
	public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent, 
			Map<String, Object> oldFieldParent, JsonBean fieldDescription, String fieldPath) {
		boolean isValid = true;
		boolean multipleValuesAllowed = Boolean.valueOf(fieldDescription.getString(ActivityEPConstants.MULTIPLE_VALUES));
		String fieldName = fieldDescription.getString(ActivityEPConstants.FIELD_NAME);
		Object fieldValue = newFieldParent.get(fieldName);
		if (!multipleValuesAllowed && hasManyElements(fieldValue)) {
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
	private boolean hasManyElements(Object fieldValue) {
		return fieldValue != null && fieldValue instanceof Collection && ((Collection) fieldValue).size() > 1;
	}

}
