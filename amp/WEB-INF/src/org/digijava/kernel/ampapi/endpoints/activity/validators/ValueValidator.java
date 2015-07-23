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
		// input type, allowed input will be verified before, so nothing check here 
		if (!importable)
			return true;
		
		
		Integer maxLength = (Integer) fieldDescription.get(ActivityEPConstants.FIELD_LENGTH); 
		if (maxLength != null) {
			Object obj = newFieldParent.get(fieldDescription.getString(ActivityEPConstants.FIELD_NAME));
			if (obj != null && String.class.isAssignableFrom(obj.getClass())){
				if (maxLength < ((String) obj).length())
					return false;
			}
		}
		
		List<JsonBean> possibleValues = importer.getPossibleValuesForFieldCached(fieldPath, AmpActivityFields.class, null);
		
		if (possibleValues.size() != 0) {
			Object value = newFieldParent.get(fieldDescription.getString(ActivityEPConstants.FIELD_NAME));
			
			if (value != null) {
				boolean idOnly = Boolean.TRUE.equals(fieldDescription.get(ActivityEPConstants.ID_ONLY));
				//String idFieldName = idOnly ? ActivityImporterHelper.getIdFieldName(fieldDescription) : null;
				boolean isMap = value != null && Map.class.isAssignableFrom(value.getClass());
				
				// convert to string the ids to avoid long-integer comparison
				value = idOnly ? value.toString() : value;
				
				for (JsonBean option: possibleValues) {
					if (isMap) {
						if (option.any().equals(value))
							return true;
					} else if (idOnly) {
						if ( value.equals(option.getString(ActivityEPConstants.ID)))
							return true;
					}
				}
				// wrong value configured if it is not found in allowed options 
				return false;
			}
		}
		// nothing failed so far? then we are good to go
		return true;
	}
	
}
