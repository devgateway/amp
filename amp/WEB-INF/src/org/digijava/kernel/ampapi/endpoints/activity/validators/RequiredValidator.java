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
	private boolean draftDisabled = false;
	
	@Override
	public ApiErrorMessage getErrorMessage() {
		if (this.draftDisabled)
			return ActivityErrors.SAVE_AS_DRAFT_FM_DISABLED;
		else
			return ActivityErrors.FIELD_REQUIRED;
	}
	
	@Override
	public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent, 
			Map<String, Object> oldFieldParent, JsonBean fieldDescription, String fieldPath) {
		String fieldName = (String) fieldDescription.get(ActivityEPConstants.FIELD_NAME);
		Object fieldValue = newFieldParent.get(fieldName);
		String requiredStatus = fieldDescription.getString(ActivityEPConstants.REQUIRED);

		//don't care if value has something 
		if (fieldValue == null) {
			if (ActivityEPConstants.FIELD_ALWAYS_REQUIRED.equals(requiredStatus)) {
				//field is always required -> can't save it even as a draft
				return false;
			} else if (ActivityEPConstants.FIELD_NON_DRAFT_REQUIRED.equals(requiredStatus)) {
				//field required for submitted activities, but we can save it as a draft
				
				//unless it's disabled in FM
				if (!importer.isDraftFMEnabled()) {
					this.draftDisabled = true;
					return false;
				}
				//ok, it's enabled, save as draft
				importer.setSaveAsDraft(true);
				return true;
			}
			
		} 
		//field value != null, it's fine from this validator's POV
		return true;	
	
	}


}
