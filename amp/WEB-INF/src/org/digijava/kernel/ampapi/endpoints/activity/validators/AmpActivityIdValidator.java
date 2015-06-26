/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.ActivityVersionUtil;

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
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		if (InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ACTIVITY_ID).equals(fieldName)) {
			if (update) {
				Long latestAmpActivityId = ActivityVersionUtil.getLastVersionForVersion(Long.valueOf(ampActivityId));
				if (oldActivity == null || !latestAmpActivityId.equals(oldActivity.getAmpActivityId())) {
					isValid = false;
				}
			} else {
				if ((ampActivityId != null && Long.valueOf(ampActivityId).longValue() != 0l)) {
					isValid = false;
				}
			}
		} else if (InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID).equals(fieldName)) {
			if (update) {
				if (oldActivity == null || !oldActivity.getAmpId().equals(ampId)) {
					isValid = false;
				}
			} else {
				if (ampId != null) {
					isValid = false;
				}
			}
		}
		return isValid;
	}
}
