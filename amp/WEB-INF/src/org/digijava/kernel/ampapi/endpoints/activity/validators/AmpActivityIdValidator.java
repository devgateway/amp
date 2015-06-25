/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * Validates amp_activity_id and amp_id to be valid
 * 
 * @author Nadejda Mandrescu
 */
public class AmpActivityIdValidator extends InputValidator {

	public AmpActivityIdValidator() {
		this.continueOnSuccess = false;
	}

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_INVALID_VALUE;
	}

	@Override
	public boolean isValid(AmpActivityVersion oldActivity, JsonBean newFieldParent, JsonBean oldFieldParent,
			JsonBean fieldDescription, String fieldPath, boolean update) {
		boolean isValid = true;
		String ampId = (String) newFieldParent.get(ActivityFieldsConstants.AMP_ID);
		String ampActivityId = (String) newFieldParent.get(ActivityFieldsConstants.AMP_ACTIVITY_ID);
		if (update) {
			Long latestAmpActivityId = ActivityUtil.getLastActivityId(ampId);
		
			//if oldActivity's amp_id doesn't match the one received or it is not the latest amp_activity_id for that activity 
			// -> then it is invalid
			if (!oldActivity.getAmpId().equals(ampId)
					|| !latestAmpActivityId.equals(oldActivity.getAmpActivityId())) {
				isValid = false;
			}

		} else {
			
			//On insert,if amp_id was sent OR amp_activity_id was sent and it was not 0  - > them it is invalid
			if ((ampActivityId != null && Long.valueOf(ampActivityId).longValue() != 0l) || ampId != null) {
				isValid = false;
			}
		}
		return isValid;
	}
}
