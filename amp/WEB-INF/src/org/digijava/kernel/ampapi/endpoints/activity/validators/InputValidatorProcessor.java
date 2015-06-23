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
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
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
	 * @param newFieldValue new field json structure
	 * @param oldFieldValue old field json structure
	 * @param fieldDescription field description
	 * @param errors
	 * @return true if the current field passes the full validation chain
	 */
	public boolean isValid(AmpActivityVersion oldActivity, JsonBean newField, JsonBean oldField, 
			JsonBean fieldDescription, Map<Integer, ApiErrorMessage> errors, boolean update) {
		boolean valid = true;
		
		for (InputValidator current : validators) {
			boolean currentValid = current.isValid(oldActivity, newField, oldField, fieldDescription, update);
			valid = currentValid && valid;
			
			if (!currentValid) {
				addError(newField, current.getErrorMessage(), errors);
			}
			
			if (!(currentValid && current.isContinueOnSuccess() || !currentValid && current.isContinueOnError())) {
				break;
			}
		}
		return valid;
	}
	
	protected void addError(JsonBean newFieldValue, ApiErrorMessage error, Map<Integer, ApiErrorMessage> errors) {
		String errorCode = ApiError.getErrorCode(error);
		// configure field level invalid flag
		Set<String> fieldErrors = (Set<String>) newFieldValue.get(EPConstants.INVALID); 
		if (fieldErrors == null) {
			fieldErrors = new HashSet<String>();
			newFieldValue.set(EPConstants.INVALID, fieldErrors);
		}
		fieldErrors.add(errorCode);
		
		// record general errors for the request
		String value = newFieldValue.getString(ActivityEPConstants.FIELD_NAME);
		ApiErrorMessage generalError = errors.get(error.id);
		generalError = new ApiErrorMessage(generalError == null ? error : generalError, value);
		errors.put(error.id, generalError);
	}
	
}
