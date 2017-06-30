package org.digijava.kernel.ampapi.endpoints.gpi;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

public class GPIErrors { 
	public static final ApiErrorMessage AID_ON_BUDGET_DATE_DONOR_COMBINATION_EXISTS = new ApiErrorMessage(1, "Please select a unique date or donor agency.");
	public static final ApiErrorMessage UNAUTHORIZED_OPERATION = new ApiErrorMessage(2, "Unauthorized operation");
	public static final ApiErrorMessage DONOR_NOTES_DATE_DONOR_COMBINATION_EXISTS = new ApiErrorMessage(3, "Please select a unique date or donor agency.");
}
