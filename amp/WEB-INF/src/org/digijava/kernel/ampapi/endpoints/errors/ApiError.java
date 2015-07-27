package org.digijava.kernel.ampapi.endpoints.errors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeEndpoints;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.security.Security;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;

import javax.servlet.http.HttpServletResponse;

/**
 * Defines API Error Utility class for manipulating ApiErrorMessage objects
 * @author Viorel Chihai
 */

public class ApiError {
	
	public final static int GENERAL_ERROR_CODE = 0;
	public final static int GENERIC_HANDLED_ERROR_CODE = 0;
	public final static int GENERIC_UNHANDLED_ERROR_CODE = 1;
	
	public final static String JSON_ERROR_CODE = "error";
	public final static String API_ERROR_PATTERN = "%02d%02d";
	
	public final static String UNKOWN_ERROR = "Unknown Error";
	
	/**  Will store the mapping between the component and it's Id (C). */
	public final static Map<String, Integer> COMPONENT_ID_CLASS_MAP = new HashMap<String, Integer>() {{
		put(InterchangeEndpoints.class.getName(), 1);
		put(Security.class.getName(), 2);
	}};
	
	/**
	 * Returns a JSON object with a single error message  => generic 0 error code with one error in the list.
	 * @param errorMessage
	 * @return the json of the error. E.g.: {0: [“Generic error 1”]}
	 */
	public static JsonBean toError(String errorMessage) {
		JsonBean errorBean = new JsonBean();
		errorBean.set(String.format(API_ERROR_PATTERN, GENERAL_ERROR_CODE, GENERIC_HANDLED_ERROR_CODE), new String[] {errorMessage});
		
		return getResultErrorBean(errorBean);
	};
	
	/**
	 * Returns a JSON object with list of error messages
	 * list of error messages => generic 0 error code with the given list of errors
	 * list of custom errors, where integer will be validated to be between 0 and 99
	 * @param ApiErrorMessage object 
	 * @return the JSON object of the error. E.g.: {0: [“Generic error 1”, “Generic error 2”], 135: [“Forbidden fields have been configured: sector_id”], 123: [“Generic error 1”]}
	 */
	public static JsonBean toError(Collection<?> errorMessages) {
		Map<String, Collection<String>> errors = new HashMap<String, Collection<String>>();
		
		if (errorMessages != null && errorMessages.size() > 0) {
			if (errorMessages.iterator().next() instanceof String) {
				String generalErrorCode = String.format(API_ERROR_PATTERN, GENERAL_ERROR_CODE, GENERIC_HANDLED_ERROR_CODE);
				errors.put(generalErrorCode, (Collection<String>) errorMessages);
			} else if (errorMessages.iterator().next() instanceof ApiErrorMessage) {
				int componentId = getErrorComponentId();
				for(Object errorMessage : errorMessages) {
					ApiErrorMessage apiError = (ApiErrorMessage) errorMessage;
					String errorId = getErrorId(componentId, apiError.id);
					
					if (errors.get(errorId) == null) {
						errors.put(errorId, new ArrayList<String>());
					}
					
					errors.get(errorId).add(getErrorText(apiError));
				}
			}
		}
		
		return getResultErrorBean(errors);
	};
	
	/**
	 * Returns a JSON object with a single error message. Generic 0 error code with one error in the list.
	 * @param ApiErrorMessage object
	 * @return the json of the error. E.g.: {123: [“Generic error 1”]}
	 */
	public static JsonBean toError(ApiErrorMessage apiErrorMessage) {
		JsonBean errorBean = new JsonBean();
		errorBean.set(getErrorId(getErrorComponentId(), apiErrorMessage.id), new String[] {getErrorText(apiErrorMessage)});
		
		return getResultErrorBean(errorBean);
	};

    /**
     * Sets "Internal Server Error" marker if the marker was absent or 200 OK
     * Does not override custom Response marker status if one has been set
     * See AMP-20522 for details
     */
    private static void processErrorResponseStatus() {
        Integer responseMarker = EndpointUtils.getResponseStatusMarker();
        if (responseMarker == null || responseMarker == HttpServletResponse.SC_OK) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
	
	private static JsonBean getResultErrorBean(Object errorBean) {
        processErrorResponseStatus();

		JsonBean resultErrorBean = new JsonBean();
		resultErrorBean.set(JSON_ERROR_CODE, errorBean);
		
		return resultErrorBean;
	}
	
	/**
	 * Builds full error code, that can be used for logging.
	 * Please refer to {@link #toError(ApiErrorMessage)} and its overloaded alternatives 
	 * to generate a correct Amp API Error response.
	 * @param error API Message error reference
	 * @return full error code
	 */
	public static String getErrorCode(ApiErrorMessage error) {
		return getErrorId(getErrorComponentId(), error.id);
	}
	
	/**
	 * Returns the id of the ApiErrorMessage object. A lookup for the class code will be made on the stacktrace . 
	 * @param ApiErrorMessage object
	 * @return the id of the error
	 */
	private static String getErrorId(int componentId, int errorId) {
		if (componentId != 0) {
			return String.format(API_ERROR_PATTERN, componentId, errorId);
		} else {
			return String.format(API_ERROR_PATTERN, GENERAL_ERROR_CODE, errorId);
		}
	}
	
	private static Integer getErrorComponentId() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		
		for (StackTraceElement st : stackTrace) {
			if (COMPONENT_ID_CLASS_MAP.containsKey(st.getClassName())) {
				return COMPONENT_ID_CLASS_MAP.get(st.getClassName());
			}
		}
		
		return GENERAL_ERROR_CODE;
	}
	
	/**
	 * Returns the message of the ApiErrorMessage object.
	 * @param apiErrorMessage object
	 * @see ApiErrorMessage
	 * @return the message of the error
	 */
	private static String getErrorText(ApiErrorMessage apiErrorMessage) {
		String errorText = "(" + TranslatorWorker.translateText(apiErrorMessage.description) + ")";
		if (!StringUtils.isBlank(apiErrorMessage.prefix)) {
			errorText += " " + TranslatorWorker.translateText(apiErrorMessage.prefix);
		}
		
		if (!StringUtils.isBlank(apiErrorMessage.value)) {
			// no translation of the value, it must come translated if translation is needed for it
			errorText += " " + apiErrorMessage.value;
		}
		
		return errorText;
	}
}
