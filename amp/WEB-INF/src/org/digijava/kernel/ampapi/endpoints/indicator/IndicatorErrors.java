package org.digijava.kernel.ampapi.endpoints.indicator;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

public class IndicatorErrors {
    
    // Validation errors
    public static final ApiErrorMessage INCORRECT_CONTENT = new ApiErrorMessage(9, 0, "Wrong content");
    public static final ApiErrorMessage INEXISTANT_ADM_LEVEL = new ApiErrorMessage(9, 1, "Inexistant AdmLevel");
    public static final ApiErrorMessage NUMBER_NOT_MATCH = new ApiErrorMessage(9, 2, "Number mismatch");
    public static final ApiErrorMessage NAME_NOT_MATCH = new ApiErrorMessage(9, 3, "Name mismatch");
    public static final ApiErrorMessage LOCATION_NOT_FOUND = new ApiErrorMessage(9, 4, "Location not found");
    
    public static final ApiErrorMessage INVALID_SORT = new ApiErrorMessage(9, 5, "Invalid sort");
    public static final ApiErrorMessage EXISTING_NAME = new ApiErrorMessage(9, 6, "Existing name");
    public static final ApiErrorMessage INVALID_POPULATION_LAYERS = new ApiErrorMessage(9, 7,
            "Invalid population layers");
    public static final ApiErrorMessage INVALID_INDICATOR_TYPE = new ApiErrorMessage(9, 8, "Invalid Indicator Type");
    public static final ApiErrorMessage INVALID_IMPORT_NO_VALUE = new ApiErrorMessage(9, 9,
            "Invalid Import: Please add at least one indicator value");
    
    public static final ApiErrorMessage INVALID_IMPORT_INVALID_ADMIN_LEVEL = new ApiErrorMessage(9, 10,
            "Invalid Import: Administrative divisions in the uploaded file do not match "
                    + "Admin level selected in wizard");
}
