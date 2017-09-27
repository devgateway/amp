package org.digijava.kernel.ampapi.endpoints.datafreeze;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

public class DataFreezeErrors {
    public static final ApiErrorMessage FREEZING_DATE_EXISTS = new ApiErrorMessage(1, "Freezing events cannot be repeated.");
    public static final ApiErrorMessage OPEN_PERIOD_OVERLAPS = new ApiErrorMessage(2, "The open period range cannot overlap with other freezing events.");
}
