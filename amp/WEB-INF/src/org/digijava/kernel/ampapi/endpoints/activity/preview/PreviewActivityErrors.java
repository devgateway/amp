package org.digijava.kernel.ampapi.endpoints.activity.preview;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Viorel Chihai
 */
public final class PreviewActivityErrors {

    private PreviewActivityErrors() {
    }

    public static final ApiErrorMessage ACTIVITY_NOT_FOUND = new ApiErrorMessage(1, "Activity not found");
    public static final ApiErrorMessage CURRENCY_NOT_FOUND = new ApiErrorMessage(2, 
            "Parameter " + ActivityEPConstants.PREVIEW_CURRENCY_ID + " is not specified or it is not a valid value");
    
}
