package org.digijava.kernel.ampapi.endpoints.errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeEndpoints;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.common.AmpConfigurationErrors;
import org.digijava.kernel.ampapi.endpoints.common.CommonErrors;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.contact.ContactErrors;
import org.digijava.kernel.ampapi.endpoints.currency.CurrencyErrors;
import org.digijava.kernel.ampapi.endpoints.dashboards.DashboardErrors;
import org.digijava.kernel.ampapi.endpoints.datafreeze.DataFreezeErrors;
import org.digijava.kernel.ampapi.endpoints.gpi.GPIErrors;
import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorErrors;
import org.digijava.kernel.ampapi.endpoints.performance.PerformanceRulesErrors;
import org.digijava.kernel.ampapi.endpoints.reports.designer.ReportDesignerErrors;
import org.digijava.kernel.ampapi.endpoints.reports.ReportErrors;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceErrors;
import org.digijava.kernel.ampapi.endpoints.security.SecurityErrors;
import org.digijava.kernel.ampapi.endpoints.sync.SynchronizerErrors;
import org.digijava.kernel.translator.TranslatorWorker;
import org.json.JSONObject;
import org.json.XML;

/**
 * Defines API Error Utility class for manipulating ApiErrorMessage objects
 * @author Viorel Chihai
 */

public class ApiError {
    
    public static final int ERROR_CLASS_GENERIC_ID = 0;
    public static final int ERROR_CLASS_VALIDATION_ID = 1;
    public static final int ERROR_CLASS_ACTIVITY_ID = 2;
    public static final int ERROR_CLASS_CONTACT_ID = 3;
    public static final int ERROR_CLASS_RESOURCE_ID = 4;
    public static final int ERROR_CLASS_SECURITY_ID = 5;
    public static final int ERROR_CLASS_REPORT_ID = 6;
    public static final int ERROR_CLASS_CURRENCY_ID = 7;
    public static final int ERROR_CLASS_DASHBOARD_ID = 8;
    public static final int ERROR_CLASS_INDICATOR_ID = 9;
    public static final int ERROR_CLASS_CONFIGURATION_ID = 10;
    public static final int ERROR_CLASS_GPI_ID = 11;
    public static final int ERROR_CLASS_SYNCHRONYZER_ID = 12;
    public static final int ERROR_CLASS_COMMON_ID = 13;
    public static final int ERROR_CLASS_DATAFREEZE_ID = 14;
    public static final int ERROR_CLASS_PERFORMANCERULE_ID = 15;
    public static final int ERROR_CLASS_MACHINE_TRANSLATION_ID = 16;
    public static final int ERROR_CLASS_REPORT_DESIGNER_ID = 18;

    public static final int ERROR_CLASS_TEST_ID = 99;

    public static final String ERROR_PATTERN = "%02d%02d";
    
    public static final Map<String, Class> ERROR_NAME_CLASSES = new LinkedHashMap<String, Class>() {{
        put("Generic Errors", GenericErrors.class);
        put("Validation Errors", ValidationErrors.class);
        put("Activity Errors", ActivityErrors.class);
        put("Contact Errors", ContactErrors.class);
        put("Resource Errors", ResourceErrors.class);
        put("Security Errors", SecurityErrors.class);
        put("Report Errors", ReportErrors.class);
        put("Report Designer Errors", ReportDesignerErrors.class);
        put("Currency Errors", CurrencyErrors.class);
        put("Dashboard Errors", DashboardErrors.class);
        put("Indicator Errors", IndicatorErrors.class);
        put("Configuration Errors", AmpConfigurationErrors.class);
        put("GPI Errors", GPIErrors.class);
        put("Synchronizer Errors", SynchronizerErrors.class);
        put("Common Errors", CommonErrors.class);
        put("Data Freeze Errors", DataFreezeErrors.class);
        put("Performance Rules Errors", PerformanceRulesErrors.class);
    }};

    public static void configureComponentClassToIdMap() {
        ApiErrorCollector errorCollector = new ApiErrorCollector();
        
        Set<String> usedIds = new TreeSet<>();
        ERROR_NAME_CLASSES.values().forEach(e -> {
            List<ApiErrorMessage> errorsIds = errorCollector.collect(e);
            for (ApiErrorMessage error : errorsIds) {
                if (usedIds.contains(error.getErrorId())) {
                    throw new RuntimeException("ApiErrorMessage '" + error.description + "' cannot be mapped to id="
                            + error.getErrorId() + ". Id already in use.");
                }
                usedIds.add(error.getErrorId());
            }
        });
    }

    private final static Set<String> COMPONENTS_WITH_NEW_ERROR_FORMAT = new HashSet<>(
            Collections.singletonList(InterchangeEndpoints.class.getName()));

    /**
     * Returns an ApiErrorResponse object with a single error message
     * generic 0 error code with one error in the list.
     * @param errorMessage
     * @return the json of the error. E.g.: {0: [“Generic error 1”]}
     */
    public static ApiErrorResponse toError(String errorMessage) {
        processErrorResponseStatus();
        return format(errorMessage);
    };

    /**
     * Builds a ApiErrorResponse with all errors stored in this group.
     * @param errorsGroup
     * @return
     * @see {@link #toError(Collection)}
     */
    public static ApiErrorResponse toError(ApiEMGroup errorsGroup) {
        return toError(errorsGroup.getAllErrors());
    }

    /**
     * Returns an ApiErrorResponse object with list of error messages and updates the response status
     * list of error messages => generic 0 error code with the given list of errors
     * list of custom errors, where integer will be validated to be between 0 and 99
     * @param errorMessages a collection of objects of type String or ApiErrorMessage
     * @return the JSON object of the error. E.g.: {0: [“Generic error 1”, “Generic error 2”], 135: [“Forbidden fields have been configured: sector_id”], 123: [“Generic error 1”]}
     */
    public static ApiErrorResponse toError(Collection<?> errorMessages) {
        processErrorResponseStatus();
        return format(errorMessages);
    }

    /**
     * Returns an ApiErrorResponse object with a single error message. Generic 0 error code with one error in the list.
     * Updates the response status.
     *
     * @param apiErrorMessage ApiErrorMessage object
     * @return the json of the error. E.g.: {"0102": ["Generic error 1"]}
     */
    public static ApiErrorResponse toError(ApiErrorMessage apiErrorMessage) {
        processErrorResponseStatus();
        return format(apiErrorMessage);
    }

    public static Map<String, Collection<Object>> formatNoWrap(Collection<?> messages) {
        return format(messages).getErrors();
    }

    public static ApiErrorResponse format(Collection<?> errorMessages) {
        Map<String, Collection<Object>> errors = new HashMap<>();

        if (errorMessages != null && errorMessages.size() > 0) {
            if (errorMessages.iterator().next() instanceof String) {
                String generalErrorCode = GenericErrors.INTERNAL_ERROR.getErrorId();
                errors.put(generalErrorCode, (Collection<Object>) errorMessages);
            } else if (errorMessages.iterator().next() instanceof ApiErrorMessage) {
                for(Object errorMessage : errorMessages) {
                    ApiErrorMessage apiError = (ApiErrorMessage) errorMessage;
                    String errorId = apiError.getErrorId();

                    if (errors.get(errorId) == null) {
                        errors.put(errorId, new ArrayList<>());
                    }

                    errors.get(errorId).add(getErrorText(apiError));
                }
            }
        }

        return new ApiErrorResponse(errors);
    };

    private static ApiErrorResponse format(ApiErrorMessage apiErrorMessage) {
        Map<String, Collection<Object>> errors = new HashMap<>();
        errors.put(apiErrorMessage.getErrorId(), Arrays.asList(getErrorText(apiErrorMessage)));

        return new ApiErrorResponse(errors);
    };

    private static ApiErrorResponse format(String errorMessage) {
        Map<String, Collection<Object>> errors = new HashMap<>();
        errors.put(GenericErrors.UNKNOWN_ERROR.getErrorId(), Arrays.asList(errorMessage));

        return new ApiErrorResponse(errors);
    }

    /**
     * Sets "Internal Server Error" marker if the marker was absent or 200 OK
     * Does not override custom Response marker status if one has been set
     * See AMP-20522 for details
     */
    private static void processErrorResponseStatus() {
        Integer responseMarker = EndpointUtils.getResponseStatusMarker();
        if (responseMarker == null || responseMarker == HttpServletResponse.SC_OK) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Determine if this call is being executed from a REST Endpoint / component that supports new error format.
     *
     * @return true if this component supports new error format
     */
    private static boolean isNewErrorFormat() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement st : stackTrace) {
            if (COMPONENTS_WITH_NEW_ERROR_FORMAT.contains(st.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert ApiErrorMessage to an error object. Some components use expanded errors while others use flattened.
     * See {@link #isNewErrorFormat()}.
     *
     * @param apiErrorMessage object
     * @see ApiErrorMessage
     * @return an object describing the error
     */
    private static Object getErrorText(ApiErrorMessage apiErrorMessage) {
        if (isNewErrorFormat()) {
            return getExpandedError(apiErrorMessage);
        } else {
            return getFlattenedError(apiErrorMessage);
        }
    }

    /**
     * Extract the expanded error message from ApiErrorMessage. This format is handy for post processing in UI layers.
     *
     * @param apiErrorMessage the error
     * @return expanded error message
     */
    private static Map<String, Set<String>> getExpandedError(ApiErrorMessage apiErrorMessage) {
        String errorText = TranslatorWorker.translateText(apiErrorMessage.description);
        if (!StringUtils.isBlank(apiErrorMessage.prefix)) {
            errorText += " " + TranslatorWorker.translateText(apiErrorMessage.prefix);
        }

        Map<String, Set<String>> error = new HashMap<>();
        error.put(errorText, apiErrorMessage.values);
        return error;
    }

    /**
     * Extract flattened error message from ApiErrorMessage.
     *
     * @param apiErrorMessage the error
     * @return the message represented by this error object
     */
    private static String getFlattenedError(ApiErrorMessage apiErrorMessage) {
        String errorText = "(" + TranslatorWorker.translateText(apiErrorMessage.description) + ")";
        if (!StringUtils.isBlank(apiErrorMessage.prefix)) {
            errorText += " " + TranslatorWorker.translateText(apiErrorMessage.prefix);
        }

        if (apiErrorMessage.values != null) {
            // no translation of the value, it must come translated if translation is needed for it
            errorText += " " + Joiner.on(", ").join(apiErrorMessage.values);
        }

        return errorText;
    }

    /**
     *
     * @param errorResponse
     * @return convert a error into a well-formed, element-normal XML string
     */
    public static String toXmlErrorString(ApiErrorResponse errorResponse) {
        Map<String, Collection<Object>> errorResponseMap = errorResponse.getErrors();
        List<Map<String, Object>> errors = new ArrayList<>();

        for (String key : errorResponseMap.keySet()) {
            Map<String, Object> err = new HashMap<>();
            Collection<Object> error = errorResponseMap.get(key);
            err.put("code", key);
            err.put("value", error);
            errors.add(err);
        }

        Map<String, List<Map<String, Object>>> errorsMap = new HashMap<>();
        errorsMap.put("error", errors);
        Map<String, Map<String, List<Map<String, Object>>>> responseErrorBean = new HashMap<>();
        responseErrorBean.put("errors", errorsMap);

        try {
            JSONObject o = new JSONObject(new ObjectMapper().writer().writeValueAsString(responseErrorBean));
            return XML.toString(o);
        } catch (Exception e) {
            throw AlgoUtils.translateException(e);
        }
    }
}
