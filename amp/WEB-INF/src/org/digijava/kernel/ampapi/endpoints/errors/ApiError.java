package org.digijava.kernel.ampapi.endpoints.errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.InterchangeEndpoints;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

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
	
	/**  Will store the mapping between the component and it's Id (C). */
	public static final Map<String, Integer> COMPONENT_ID_CLASS_MAP = new HashMap<String, Integer>() {{
		put(InterchangeEndpoints.class.getName(), 1);
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
	public static JsonBean toError(List<?> errorMessages) {
		Map<String, List<String>> errors = new HashMap<String, List<String>>();
		
		if (errorMessages != null && errorMessages.size() > 0) {
			if (errorMessages.get(0) instanceof String) {
				for(Object errorMessage : errorMessages) {
					String generalErrorCode = String.format(API_ERROR_PATTERN, GENERAL_ERROR_CODE, GENERIC_HANDLED_ERROR_CODE);
					if (errors.get(generalErrorCode) == null) {
						errors.put(generalErrorCode, new ArrayList<String>());
					}
					
					errors.get(generalErrorCode).add((String) errorMessage);
				}
			} else if (errorMessages.get(0) instanceof ApiErrorMessage) {
				int componentId = getErrorComponentId((ApiErrorMessage) errorMessages.get(0));
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
		errorBean.set(getErrorId(getErrorComponentId(apiErrorMessage), apiErrorMessage.id), new String[] {getErrorText(apiErrorMessage)});
		
		return getResultErrorBean(errorBean);
	};
	
	private static JsonBean getResultErrorBean(Object errorBean) {
		JsonBean resultErrorBean = new JsonBean();
		resultErrorBean.set(JSON_ERROR_CODE, errorBean);
		
		return resultErrorBean;
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
	
	private static Integer getErrorComponentId(ApiErrorMessage apiErrorMessage) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		
		for (StackTraceElement st : stackTrace) {
			for (String componentName : COMPONENT_ID_CLASS_MAP.keySet()) {
				if (st.toString().indexOf((componentName)) >= 0) {
					return COMPONENT_ID_CLASS_MAP.get(componentName);
				}
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
		if (apiErrorMessage.prefix != null && apiErrorMessage.value != null) {
			String errorText = "";
			try {
				errorText = String.format(apiErrorMessage.prefix, apiErrorMessage.value) + " (" + apiErrorMessage.description + ")";
			} catch(Exception e) {
				errorText = apiErrorMessage.description + " (Exception during formatting the error message - " + e.getMessage() + ")";
			}
			
			return errorText;
		} 
			
		return apiErrorMessage.description;
	}
}
