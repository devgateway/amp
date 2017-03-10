/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityFields;

/**
 * Validates that field value is allowed
 * 
 * @author Nadejda Mandrescu
 */
public class ValueValidator extends InputValidator {

	protected boolean isValidLength = true;
	protected boolean isValidPercentage = true;
	@Override
	public ApiErrorMessage getErrorMessage() {
		if (!isValidLength)
			return ActivityErrors.FIELD_INVALID_LENGTH;
		if (!isValidPercentage)
			return ActivityErrors.FIELD_INVALID_PERCENTAGE;
		return ActivityErrors.FIELD_INVALID_VALUE;
	}

	@Override
	public boolean isValid(ActivityImporter importer, Map<String, Object> newFieldParent,
						   Map<String, Object> oldFieldParent, APIField fieldDescription, String fieldPath) {
		
		boolean importable = fieldDescription.isImportable();
		// input type, allowed input will be verified before, so nothing check here 
		if (!importable)
			return true;
		
		//temporary debug
		if (!isValidLength(newFieldParent, fieldDescription))
			return false;
		if (!isValidPercentage(newFieldParent, fieldDescription)) 
			return false;
		
		List<JsonBean> possibleValues = importer.getPossibleValuesForFieldCached(fieldPath, AmpActivityFields.class, null);
		
		if (possibleValues.size() != 0) {
			Object value = newFieldParent.get(fieldDescription.getFieldName());
			
			if (value != null) {
				boolean idOnly = fieldDescription.isIdOnly();
				// convert to string the ids to avoid long-integer comparison
				value = idOnly ? value.toString() : value;
				
				for (JsonBean option: possibleValues) {
					if (idOnly) {
						if (value.equals(option.getString(ActivityEPConstants.ID)))
							return true;
					} else {
						if (value.equals(option.get(ActivityEPConstants.VALUE)))
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
	



	private boolean isValidPercentage(Map<String, Object> newFieldParent,
			APIField fieldDescription) {
		this.isValidPercentage  = true;
		if (!fieldDescription.getPercentage()) {
			return true; //this doesn't contain a percentage-based field
		}

		//attempt to get the number out of this one
		Double val = InterchangeUtils.getDoubleFromJsonNumber(newFieldParent.get(fieldDescription.getFieldName()));
		if (val == null || val < ActivityEPConstants.EPSILON || val - 100.0 > ActivityEPConstants.EPSILON) {
			this.isValidPercentage = false;
			return false;
		}
		
		return true;
	}

	protected boolean isValidLength(Map<String, Object> newFieldParent, APIField fieldDescription) {
		isValidLength = true;
		Integer maxLength = fieldDescription.getFieldLength();
		if (maxLength != null) {
			Object obj = newFieldParent.get(fieldDescription.getFieldName());
			if (obj != null) {
				if (fieldDescription.isTranslatable()) {
					isValidLength = isValidLength(obj, maxLength);
				} else if (Map.class.isAssignableFrom(obj.getClass())) {
					for (Object trn : ((Map) obj).values()) {
						if (!isValidLength(trn, maxLength)) {
							isValidLength = false;
							break;
						}
					}
					// translatable means its input must be a map, otherwise invalid input was provided, so we cannot say it's invalid
				}
			}
		}
		return isValidLength;
	}
	
	protected boolean isValidLength(Object obj, Integer maxLength) {
		if (obj == null)
			return true;
		if (String.class.isAssignableFrom(obj.getClass())){
			if (maxLength < ((String) obj).length())
				return false;
		}
		return true;
	}
}
