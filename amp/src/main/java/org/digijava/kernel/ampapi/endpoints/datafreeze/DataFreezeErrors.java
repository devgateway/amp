package org.digijava.kernel.ampapi.endpoints.datafreeze;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_DATAFREEZE_ID;

public class DataFreezeErrors {
    
    public static final ApiErrorMessage FREEZING_DATE_EXISTS = new ApiErrorMessage(ERROR_CLASS_DATAFREEZE_ID, 0,
            "Freezing events cannot be repeated.");
    
    public static final ApiErrorMessage OPEN_PERIOD_OVERLAPS = new ApiErrorMessage(ERROR_CLASS_DATAFREEZE_ID, 1,
            "The open period range cannot overlap with other freezing events.");
    
}
