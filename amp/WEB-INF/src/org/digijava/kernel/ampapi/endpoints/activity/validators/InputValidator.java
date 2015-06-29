/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityImporter;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Defines common base class for input validation  
 * @author Nadejda Mandrescu
 */
public abstract class InputValidator {
	protected boolean continueOnError = false;
	protected boolean continueOnSuccess = true;
	
	/**
	 * Validates new field value configuration
	 * @param importer or null if none existed
	 * @param newFieldParent parent input JSON definition of the field
	 * @param oldFieldParent existing parent JSON definition of the field (can be null if not present)
	 * @param fieldDescription description of the current field (type, required, edit rights, etc)
	 * @param fieldPath full field path
	 * @param update true if this is an update request
	 * @return true if chain validation passed
	 */
	public abstract boolean isValid(ActivityImporter importer, JsonBean newFieldParent, JsonBean oldFieldParent, 
			JsonBean fieldDescription, String fieldPath);
	
	/**
	 * @return this validator specific Error message
	 */
	public abstract ApiErrorMessage getErrorMessage();
	
	
	/**
	 * @return the continueOnError
	 */
	public boolean isContinueOnError() {
		return continueOnError;
	}

	/**
	 * @param continueOnError the continueOnError to set
	 */
	public void setContinueOnError(boolean continueOnError) {
		this.continueOnError = continueOnError;
	}

	/**
	 * @return the continueOnSuccess
	 */
	public boolean isContinueOnSuccess() {
		return continueOnSuccess;
	}

	/**
	 * @param continueOnSuccess the continueOnSuccess to set
	 */
	public void setContinueOnSuccess(boolean continueOnSuccess) {
		this.continueOnSuccess = continueOnSuccess;
	}
}
