/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Verifies if the field is found under the current level of fields enumeration
 * 
 * @author Nadejda Mandrescu
 */
public class ValidFieldValidator extends InputValidator {

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_INVALID;
	}

	@Override
	public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent,
						   Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
		boolean isValid = true;
		if (fieldDescription == null) {
			isValid = false;
		}
		return isValid;
	}

}
