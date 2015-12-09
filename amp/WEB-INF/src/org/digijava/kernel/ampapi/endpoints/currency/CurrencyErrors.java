/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.currency;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.module.aim.dbentity.AmpInflationRate;

/**
 * Errors reported for Currency API
 * 
 * @author Nadejda Mandrescu
 */
public class CurrencyErrors {
	
	public static final ApiErrorMessage INVALID_SOURCE_ID = new ApiErrorMessage(1, "Invalid source_id");
	public static final ApiErrorMessage INVALID_DATE_FORMAT = new ApiErrorMessage(2, "Invalid date format");
	public static final ApiErrorMessage INVALID_INFLATION_RATE_VALUE = new ApiErrorMessage(3, "Invalid inflation rate value");
	public static final ApiErrorMessage INVALID_CURRENCY_CODE = new ApiErrorMessage(4, "Invalid currency code");
	public static final ApiErrorMessage INVALID_INFLATION_RATE_SERIES = new ApiErrorMessage(5, "Invalid inflation rates series");
	public static final ApiErrorMessage INVALID_PERIOD = new ApiErrorMessage(6, 
			String.format("Invalid period, must be between %d  and %d", 
					AmpInflationRate.MIN_DEFLATION_YEAR, AmpInflationRate.MAX_DEFLATION_YEAR));
	public static final ApiErrorMessage INVALID_CALENDAR_ID = new ApiErrorMessage(7, "Invalid calendar id");
	public static final ApiErrorMessage DUPLICATE_CALENDAR = new ApiErrorMessage(8, "Duplicate input for calendar id");
	public static final ApiErrorMessage DUPLICATE_CURRENCY = new ApiErrorMessage(9, "Duplicate input for currency");
	public static final ApiErrorMessage INVALID_CONSTANT_CURRENCIES_SERIES = new ApiErrorMessage(10, "Invalid constant currencies series");
	
}
