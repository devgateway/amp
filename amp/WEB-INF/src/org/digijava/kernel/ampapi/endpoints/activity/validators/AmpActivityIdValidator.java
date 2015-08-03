/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.util.ActivityVersionUtil;

/**
 * Validates amp_activity_id and amp_id to be valid
 * 
 * @author Nadejda Mandrescu
 */
public class AmpActivityIdValidator extends InputValidator {
	private boolean isOldActivityId = false;

    public AmpActivityIdValidator() {
	}

	@Override
	public ApiErrorMessage getErrorMessage() {
		if (isOldActivityId) {
            return ActivityErrors.UPDATE_ID_IS_OLD;
        }
		return ActivityErrors.FIELD_INVALID_VALUE;
	}

	@Override
	public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent, 
			Map<String, Object> oldFieldParent, JsonBean fieldDescription, String fieldPath) {
		boolean isValid = true;
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		if (ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME.equals(fieldPath)) {
			String internalId = String.valueOf(newFieldParent.get(ActivityEPConstants.AMP_ACTIVITY_ID_FIELD_NAME));
			Long ampActivityId = NumberUtils.isNumber(internalId) ? Long.valueOf(internalId) : null; 
			if (importer.isUpdate()) {
				Long latestAmpActivityId = ampActivityId == null ? null :  
						ActivityVersionUtil.getLastVersionForVersion(Long.valueOf(ampActivityId.toString()));
				if (importer.getOldActivity() == null || latestAmpActivityId == null
						|| !latestAmpActivityId.equals(importer.getOldActivity().getAmpActivityId())) {
					isValid = false;
					isOldActivityId = importer.getOldActivity() != null;
                    if (isOldActivityId) {
                        importer.setLatestActivityId(latestAmpActivityId);
                    }
				}
			} else {
				if ((ampActivityId != null)) { //must not be configurable even to 0 (updated design) && Long.valueOf(ampActivityId).longValue() != 0l)) {
					isValid = false;
				}
			}
		} else if (InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID).equals(fieldName)) {
			String ampId = (String) newFieldParent.get(InterchangeUtils.underscorify(ActivityFieldsConstants.AMP_ID));
			if (importer.isUpdate()) {
				if (importer.getOldActivity() == null || !importer.getOldActivity().getAmpId().equals(ampId)) {
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
