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
 * Defines common base class for input validation  
 * @author Nadejda Mandrescu
 */
public abstract class InputValidator {
	private boolean continueOnError = false;
	private boolean continueOnSuccess = false;
	
	/**
	 * Validates new field value configuration
	 * 
	 * @param newFieldValue input JSON definition of the field
	 * @param oldFieldValue existing JSON definition of the field (can be null if not present)
	 * @param fieldDescription description of the current field (type, required, edit rights, etc) 
	 * @param errors stores 
	 * @return true if chain validation passed
	 */
	public abstract boolean isValid(AmpActivityVersion oldActivity, JsonBean newFieldValue, JsonBean oldFieldValue, 
			JsonBean fieldDescription);
	
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
