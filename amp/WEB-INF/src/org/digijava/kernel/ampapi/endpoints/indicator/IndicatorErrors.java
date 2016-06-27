package org.digijava.kernel.ampapi.endpoints.indicator;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

public class IndicatorErrors {

	// Validation errors
	public static final ApiErrorMessage UNKNOWN_ERROR = new ApiErrorMessage(0, "Unknown Error");

    public static final ApiErrorMessage UNAUTHORIZED = new ApiErrorMessage(1, "Unauthorized operation");

	public static final ApiErrorMessage FIELD_INVALID_TYPE = new ApiErrorMessage(2, "Invalid field type");
	public static final ApiErrorMessage FIELD_INVALID_VALUE = new ApiErrorMessage(3, "Invalid field value");

    public static final ApiErrorMessage INVALID_ID = new ApiErrorMessage(4, "Invalid id");

    public static final ApiErrorMessage INCORRECT_CONTENT = new ApiErrorMessage(5, "Wrong content");
    public static final ApiErrorMessage INEXISTANT_ADM_LEVEL = new ApiErrorMessage(6, "Inexistant AdmLevel");
    public static final ApiErrorMessage NUMBER_NOT_MATCH = new ApiErrorMessage(7, " Number misMatch");
    public static final ApiErrorMessage NAME_NOT_MATCH = new ApiErrorMessage(8, "Name misMatch");
    public static final ApiErrorMessage LOCATION_NOT_FOUND = new ApiErrorMessage(9, "Location not found");

}