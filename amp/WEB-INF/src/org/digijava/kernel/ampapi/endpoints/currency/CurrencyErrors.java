/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.currency;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Errors reported for Currency API
 * 
 * @author Nadejda Mandrescu
 */
public class CurrencyErrors {
	
	public static final ApiErrorMessage INVALID_SOURCE_ID = new ApiErrorMessage(1, "Invalid source_id");
	public static final ApiErrorMessage INVALID_DATE_FORMAT = new ApiErrorMessage(2, "Invalid date");
	public static final ApiErrorMessage INVALID_INFLATION_RATE_VALUE = new ApiErrorMessage(3, "Invalid inflation rate value");
	public static final ApiErrorMessage INVALID_CURRENCY_CODE = new ApiErrorMessage(4, "Invalid currency code");
	public static final ApiErrorMessage INVALID_INFLATION_RATE_SERIES = new ApiErrorMessage(5, "Invalid inflation rates series");
	public static final ApiErrorMessage INVALID_CURRENCY_YEARS = new ApiErrorMessage(6, "Invalid currency years");
	
}
