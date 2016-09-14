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

    public static final ApiErrorMessage INVALID_FIELD = new ApiErrorMessage(10, "Invalid field");
    public static final ApiErrorMessage INVALID_SORT = new ApiErrorMessage(11, "Invalid sort");
    public static final ApiErrorMessage EXISTING_NAME = new ApiErrorMessage(12, "Existing name");
    public static final ApiErrorMessage INVALID_POPULATION_LAYERS = new ApiErrorMessage(13, "Invalid population layers");
    public static final ApiErrorMessage INVALID_INDICATOR_TYPE = new ApiErrorMessage(14, "Invalid Indicator Type");
    public static final ApiErrorMessage INVALID_IMPORT_NO_VALUE = new ApiErrorMessage(15, "Invalid Import: Please add at least one indicator value");
}