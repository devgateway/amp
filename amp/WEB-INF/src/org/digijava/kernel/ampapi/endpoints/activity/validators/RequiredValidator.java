/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;


import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Validates if required data is provided
 * 
 * @author Nadejda Mandrescu
 */
public class RequiredValidator extends InputValidator {

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_REQUIRED;
	}

	@Override
	public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent, 
			Map<String, Object> oldFieldParent, JsonBean fieldDescription, String fieldPath) {
		boolean isValid = true;
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		Object fieldValue = newFieldParent.get(fieldName);
		String requiredStatus = fieldDescription.getString(ActivityEPConstants.REQUIRED);
		// On insert or draft activities update...
		if (!importer.isUpdate() || isSaveAsDraft(importer.getOldActivity())) {
			if (isSaveAsDraft(importer.getOldActivity()) && !importer.isDraftFMEnabled()) {
				isValid = false;
			}
			if (ActivityEPConstants.FIELD_ALWAYS_REQUIRED.equals(requiredStatus) && fieldValue == null) {
				isValid = false;
			}
		}
		// on update of non-draft activities
		else {
			if (!importer.getAllowSaveAsDraftShift()
					&& (ActivityEPConstants.NON_DRAFT_REQUIRED.equals(requiredStatus) || ActivityEPConstants.FIELD_ALWAYS_REQUIRED
							.equals(requiredStatus)) && fieldValue == null) {
				isValid = false;
			}
		}

		return isValid;
	}

	private boolean isSaveAsDraft(AmpActivityVersion oldActivity) {
		return oldActivity != null && oldActivity.getDraft().equals(Boolean.TRUE);
	}

}
