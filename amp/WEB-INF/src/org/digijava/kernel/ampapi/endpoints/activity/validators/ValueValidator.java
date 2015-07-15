/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityFields;

/**
 * Validates that field value is allowed
 * 
 * @author Nadejda Mandrescu
 */
public class ValueValidator extends InputValidator {

	@Override
	public ApiErrorMessage getErrorMessage() {
		return ActivityErrors.FIELD_INVALID_VALUE;
	}

	@Override
	public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent, 
			Map<String, Object> oldFieldParent, JsonBean fieldDescription, String fieldPath) {
		//TODO: validate ranges for integers, dates, percentages etc. 
		
		boolean importable = (boolean) fieldDescription.get(ActivityEPConstants.IMPORTABLE);
		if (importable) {
			Long maxLength = (Long) fieldDescription.get(ActivityEPConstants.FIELD_LENGTH); 
			if (maxLength != null) {
				Object obj = newFieldParent.get(fieldDescription.getString(ActivityEPConstants.FIELD_NAME));
				if (String.class.isAssignableFrom(obj.getClass())){
					if (maxLength < ((String)obj).length())
						return false;
				}
			}
		}
		
		List<JsonBean> possibleValues = importer.getPossibleValuesForFieldCached(fieldPath, AmpActivityFields.class, null);
		
		if (possibleValues.size() == 0) {
			return true;
		} else {
			for (JsonBean value: possibleValues) {
				if (value.equals(newFieldParent.get((String)fieldDescription.get(ActivityEPConstants.FIELD_NAME))))
					return true;
			}
			return false;
		}
	}
	
}
