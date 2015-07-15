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

/**
 * Checks "changed" fields that they are importable  
 * @author Nadejda Mandrescu
 */
public class AllowedInputValidator extends InputValidator {

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_READ_ONLY;
	}

	@Override
	public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent, 
			Map<String, Object> oldFieldParent, JsonBean fieldDescription, String fieldPath) {
		//importable, by default, is true
		if (fieldDescription.get(ActivityEPConstants.IMPORTABLE) == null)
			return true; 
		Boolean importable = (Boolean) fieldDescription.get(ActivityEPConstants.IMPORTABLE);

		Object newField = newFieldParent.get(fieldDescription.getString(ActivityEPConstants.FIELD_NAME));
		Object oldField = null;
		if (oldFieldParent != null)
			oldField = oldFieldParent.get(fieldDescription.getString(ActivityEPConstants.FIELD_NAME));
		if (!importable) {
			if (newField != null && !newField.equals(oldField))
				return false;
			if (oldField != null && !oldField.equals(newField))
				return false;
			//otherwise: newfield & oldfield are both null -> true
			//		 or: newfield & oldfield are equal and not null -> true
		}
		return true;
		

	}

}
