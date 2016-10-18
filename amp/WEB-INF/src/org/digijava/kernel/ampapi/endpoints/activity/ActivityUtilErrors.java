package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

public class ActivityUtilErrors {
	public static final ApiErrorMessage ERROR_NOT_ALLOWED = new ApiErrorMessage(1, "Not allowed");
	public static final ApiErrorMessage ERROR_UN_EXPECTED = new ApiErrorMessage(2, "Unexpected error");
}
