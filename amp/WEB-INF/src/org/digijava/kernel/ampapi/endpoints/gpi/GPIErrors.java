package org.digijava.kernel.ampapi.endpoints.gpi;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

public class GPIErrors { 
	public static final ApiErrorMessage DATE_DONOR_COMBINATION_EXISTS = new ApiErrorMessage(1, "There selected donor already has a record for the selected year.");	
}
