/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Validates that multiple values are provided only when it is allowed
 * @author Nadejda Mandrescu
 */
public class MultipleEntriesValidator extends InputValidator {

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_MULTIPLE_VALUES_NOT_ALLOWED;
	}

	@Override
	public boolean isValid(AmpActivityVersion oldActivity, JsonBean newFieldValue, JsonBean oldFieldValue,
			JsonBean fieldDescription, boolean update) {
		// TODO Auto-generated method stub
		return false;
	}

}
