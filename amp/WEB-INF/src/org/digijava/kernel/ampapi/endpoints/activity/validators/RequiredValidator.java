/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Validates if required data is provided
 * 
 * @author Nadejda Mandrescu
 */
public class RequiredValidator extends InputValidator {

	private static String SAVE_AS_DRAFT_PATH = "/Activity Form/Save as Draft";

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_REQUIRED;
	}

	@Override
	public boolean isValid(ActivityImporter importer, JsonBean newFieldParent, JsonBean oldFieldParent,
			JsonBean fieldDescription, String fieldPath) {
		boolean isValid = true;
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		String fieldValue = newFieldParent.getString(fieldName);
		String requiredStatus = fieldDescription.getString(ActivityEPConstants.REQUIRED);
		// On insert or draft activities update...
		if (!importer.isUpdate() || isDraftActivityUpdate(importer.getOldActivity())) {
			// TODO: if Draft FM path is disabled during draft activity validation, do we
			// return invalid?
			if (isDraftActivityUpdate(importer.getOldActivity()) && !isDraftFMEnabled()) {
				isValid = false;
			}
			if (ActivityEPConstants.FIELD_ALWAYS_REQUIRED.equals(requiredStatus) && fieldValue == null) {
				isValid = false;
			}
		}
		// on update of non-draft activities
		else {
			if (ActivityEPConstants.NON_DRAFT_REQUIRED.equals(requiredStatus) && fieldValue == null) {
				isValid = false;
				// TODO: What to do with: Please define an internal flag (now
				// set to true), that will define if this change from
				// "save" to "save as draft" is allowed or not (to facilitate
				// new requirements in the future).
			}
		}

		return isValid;
	}

	private boolean isDraftActivityUpdate(AmpActivityVersion oldActivity) {
		return oldActivity != null && oldActivity.getDraft().equals(Boolean.TRUE);
	}

	private static boolean isDraftFMEnabled() {
		return FMVisibility.isFmPathEnabled(SAVE_AS_DRAFT_PATH);
	}

}
