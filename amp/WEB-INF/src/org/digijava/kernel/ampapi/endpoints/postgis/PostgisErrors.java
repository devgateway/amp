/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.postgis;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

public class PostgisErrors {

	// Validation errors
	public static final ApiErrorMessage FIELD_REQUIRED = new ApiErrorMessage(1, "Required field");
	public static final ApiErrorMessage FIELD_INVALID_TYPE = new ApiErrorMessage(2, "Invalid field type");
	public static final ApiErrorMessage FIELD_INVALID_VALUE = new ApiErrorMessage(3, "Invalid field value");

    public static final ApiErrorMessage INVALID_ID = new ApiErrorMessage(4, "Invalid id");

}