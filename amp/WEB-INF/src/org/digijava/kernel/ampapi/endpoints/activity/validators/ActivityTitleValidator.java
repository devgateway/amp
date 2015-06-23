/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Validates that project title field value is unique across AMP
 * @author Nadejda Mandrescu
 */
public class ActivityTitleValidator extends InputValidator {
	
	public ActivityTitleValidator() {
		this.continueOnSuccess = false;
	}

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_UNQUE_VALUES;
	}

	@Override
	public boolean isValid(AmpActivityVersion oldActivity, JsonBean newFieldValue, JsonBean oldFieldValue,
			JsonBean fieldDescription, boolean update) {
		// TODO Auto-generated method stub
		return false;
	}

}
