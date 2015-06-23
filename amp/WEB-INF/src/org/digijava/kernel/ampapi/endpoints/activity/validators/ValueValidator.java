/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Validates that field value is allowed
 * @author Nadejda Mandrescu
 */
public class ValueValidator extends InputValidator {

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_INVALID_VALUE;
	}

	@Override
	public boolean isValid(AmpActivityVersion oldActivity, JsonBean newFieldValue, JsonBean oldFieldValue,
			JsonBean fieldDescription, boolean update) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
