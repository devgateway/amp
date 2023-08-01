package org.digijava.kernel.ampapi.endpoints.currency;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.dbentity.AmpInflationRate;

import static org.digijava.kernel.ampapi.endpoints.errors.ApiError.ERROR_CLASS_CURRENCY_ID;

/**
 * Errors reported for Currency API
 *
 * @author Nadejda Mandrescu
 */
public class CurrencyErrors {
    
    public static final ApiErrorMessage INVALID_SOURCE_ID = new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 0,
            "Invalid source_id");
    
    public static final ApiErrorMessage INVALID_DATE_FORMAT = new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 1,
            "Invalid date format");
    
    public static final ApiErrorMessage INVALID_INFLATION_RATE_VALUE = new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 2,
            "Invalid inflation rate value");
    
    public static final ApiErrorMessage INVALID_CURRENCY_CODE = new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 3,
            "Invalid currency code");
    
    public static final ApiErrorMessage INVALID_INFLATION_RATE_SERIES = new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 4,
            "Invalid inflation rates series");
    
    public static final ApiErrorMessage INVALID_PERIOD = new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 5,
            String.format("Invalid period, must be between %d  and %d",
                    AmpInflationRate.MIN_DEFLATION_YEAR, AmpInflationRate.MAX_DEFLATION_YEAR));
    
    public static final ApiErrorMessage INVALID_CALENDAR_ID = new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 6,
            "Invalid calendar id");
    
    public static final ApiErrorMessage DUPLICATE_CALENDAR = new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 7,
            "Duplicate input for calendar id");
    
    public static final ApiErrorMessage DUPLICATE_CURRENCY = new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 8,
            "Duplicate input for currency");
    
    public static final ApiErrorMessage INVALID_CONSTANT_CURRENCIES_SERIES =
            new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 9, "Invalid constant currencies series");
    
    public static final ApiErrorMessage DUPLICATE_YEAR = new ApiErrorMessage(ERROR_CLASS_CURRENCY_ID, 10,
            "Duplicate year entry");
    
}
