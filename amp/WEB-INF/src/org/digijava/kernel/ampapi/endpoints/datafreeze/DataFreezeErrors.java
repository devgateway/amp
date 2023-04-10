package org.digijava.kernel.ampapi.endpoints.datafreeze;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_DATAFREEZE_ID;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

public class DataFreezeErrors {
    
    public static final ApiErrorMessage FREEZING_DATE_EXISTS = new ApiErrorMessage(ERROR_CLASS_DATAFREEZE_ID, 0,
            "Freezing events cannot be repeated.");
    
    public static final ApiErrorMessage OPEN_PERIOD_OVERLAPS = new ApiErrorMessage(ERROR_CLASS_DATAFREEZE_ID, 1,
            "The open period range cannot overlap with other freezing events.");

    public static final ApiErrorMessage OPEN_PERIOD_START_AFTER_END = new ApiErrorMessage(ERROR_CLASS_DATAFREEZE_ID, 2,
            "The open period start date cannot be after the end date.");
    
}
