package org.digijava.kernel.ampapi.endpoints.indicator;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_INDICATOR_ID;

public class IndicatorErrors {
    
    // Validation errors
    public static final ApiErrorMessage INCORRECT_CONTENT = new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 0,
            "Wrong content");
    
    public static final ApiErrorMessage INEXISTANT_ADM_LEVEL = new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 1,
            "Inexistant AdmLevel");
    
    public static final ApiErrorMessage NUMBER_NOT_MATCH = new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 2,
            "Number mismatch");
    
    public static final ApiErrorMessage NAME_NOT_MATCH = new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 3,
            "Name mismatch");
    
    public static final ApiErrorMessage LOCATION_NOT_FOUND = new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 4,
            "Location not found");
    
    
    public static final ApiErrorMessage INVALID_SORT = new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 5, "Invalid sort");
    
    public static final ApiErrorMessage EXISTING_NAME = new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 6,
            "Existing name");
    
    public static final ApiErrorMessage INVALID_POPULATION_LAYERS = new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 7,
            "Invalid population layers");
    
    public static final ApiErrorMessage INVALID_INDICATOR_TYPE = new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 8,
            "Invalid Indicator Type");
    
    public static final ApiErrorMessage INVALID_IMPORT_NO_VALUE = new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 9,
            "Invalid Import: Please add at least one indicator value");
    
    public static final ApiErrorMessage INVALID_IMPORT_INVALID_ADMIN_LEVEL =
            new ApiErrorMessage(ERROR_CLASS_INDICATOR_ID, 10,
                    "Invalid Import: Administrative divisions in the uploaded file do not match "
                            + "Admin level selected in wizard");
}
