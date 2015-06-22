/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Defines input validation chain and executes it 
 * @author Nadejda Mandrescu
 */
public class InputValidatorProcessor {
	private List<InputValidator> validators = new ArrayList<InputValidator>() {{
			add(new RequiredValidator());
			}};
	
	/**
	 * Executes validation chain for the new field values 
	 * @param newFieldValue new field json structure
	 * @param oldFieldValue old field json structure
	 * @param fieldDescription field description
	 * @param errors
	 * @return true if the current field passes the full validation chain
	 */
	public boolean isValid(AmpActivityVersion oldActivity, JsonBean newFieldValue, JsonBean oldFieldValue, 
			JsonBean fieldDescription, Map<Integer, ApiErrorMessage> errors) {
		boolean valid = true;
		
		for (InputValidator current : validators) {
			boolean currentValid = current.isValid(oldActivity, newFieldValue, oldFieldValue, fieldDescription);
			valid = currentValid && valid;
			
			if (!currentValid) {
				addError(newFieldValue, current.getErrorMessage(), errors);
			}
			
			if (!(currentValid && current.isContinueOnSuccess() || !currentValid && current.isContinueOnError())) {
				break;
			}
		}
		return valid;
	}
	
	protected void addError(JsonBean newFieldValue, ApiErrorMessage error, Map<Integer, ApiErrorMessage> errors) {
		String errorCode = "01" + error.id; // TODO: replace with utility method that will generate the error id
		// configure field level invalid flag
		Set<String> fieldErrors = (Set<String>) newFieldValue.get(EPConstants.INVALID); 
		if (fieldErrors == null) {
			fieldErrors = new HashSet<String>();
			newFieldValue.set(EPConstants.INVALID, fieldErrors);
		}
		fieldErrors.add(errorCode == null ? ActivityErrors.GENERIC_FIELD_ERROR_CODE : errorCode);
		
		// record general errors for the request
		// TODO: replace with constant ref once Enumeration Endpoint will define them
		String value = newFieldValue.getString("field_name");
		ApiErrorMessage generalError = errors.get(error.id);
		generalError = new ApiErrorMessage(generalError == null ? error : generalError, value);
		errors.put(error.id, generalError);
	}
	
}
