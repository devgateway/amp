/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Validates if required data is provided
 * @author Nadejda Mandrescu
 */
public class RequiredValidator extends InputValidator {

	@Override
	public boolean isValid(AmpActivityVersion oldActivity, JsonBean newFieldValue, JsonBean oldFieldValue,
			JsonBean fieldDescription) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.REQUIRED;
	}

}
