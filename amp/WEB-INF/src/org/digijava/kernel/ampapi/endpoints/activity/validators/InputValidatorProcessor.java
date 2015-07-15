/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Defines input validation chain and executes it 
 * @author Nadejda Mandrescu
 */
public class InputValidatorProcessor {
	/** defines validators list and their execution order */
	private List<InputValidator> validators = new ArrayList<InputValidator>() {{
			add(new ValidFieldValidator());
			add(new RequiredValidator());
			add(new AllowedInputValidator());
			add(new InputTypeValidator());
			add(new ActivityTitleValidator());
			add(new AmpActivityIdValidator());
			add(new MultipleEntriesValidator());
			add(new UniqueValidator());
			add(new ValueValidator());
			}};
	
	/**
	 * Executes validation chain for the new field values 
	 * 
	 * @param importer 			Activity Importer instance that holds other import information
	 * @param newFieldValue 	new field JSON structure
	 * @param oldFieldValue 	old field JSON structure
	 * @param fieldDescription 	field description
	 * @param errors 			map to store errors 
	 * @return true if the current field passes the full validation chain
	 */
	public boolean isValid(ActivityImporter importer, Map<String, Object> newParent, Map<String, Object> oldParent, 
			JsonBean fieldDef, String fieldPath, Map<Integer, ApiErrorMessage> errors) {
		boolean valid = true;
		String fieldName = fieldPath.substring(fieldPath.lastIndexOf("~") + 1);
		
		for (InputValidator current : validators) {
			boolean currentValid = current.isValid(importer, newParent, oldParent, fieldDef, fieldPath);
			valid = currentValid && valid;
			
			if (!currentValid) {
				addError(newParent, fieldName, current.getErrorMessage(), errors);
			}
			
			if (!(currentValid && current.isContinueOnSuccess() || !currentValid && current.isContinueOnError())) {
				break;
			}
		}
		return valid;
	}
	
	protected void addError(Map<String, Object> newParent, String fieldName, ApiErrorMessage error, 
			Map<Integer, ApiErrorMessage> errors) {
		String errorCode = ApiError.getErrorCode(error);
		JsonBean newField = new JsonBean();
		Object newFieldValue = newParent.get(fieldName);
		// if some errors where already reported, use new field storage
		if (newFieldValue instanceof JsonBean && ((JsonBean) newFieldValue).get(ActivityEPConstants.INVALID) != null) {
			newField = (JsonBean) newFieldValue;
		} else {
			// store original input
			newField.set(ActivityEPConstants.INPUT, newFieldValue);
			// and initialize fields errors list
			newField.set(ActivityEPConstants.INVALID, new HashSet<String>());
			// record new wrapped field value
			newParent.put(fieldName, newField);
		}
		
		// register field level invalid errors
		((Set<String>) newField.get(ActivityEPConstants.INVALID)).add(errorCode);
		
		// record general errors for the request
		ApiErrorMessage generalError = errors.get(error.id);
		generalError = new ApiErrorMessage(generalError == null ? error : generalError, fieldName);
		errors.put(error.id, generalError);
	}
	
}
