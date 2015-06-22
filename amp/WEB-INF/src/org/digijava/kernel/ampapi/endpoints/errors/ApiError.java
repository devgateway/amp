package org.digijava.kernel.ampapi.endpoints.errors;

import java.util.ArrayList;
import java.util.Formatter;
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
	
	public final static String GENERAL_ERROR_CODE = "0";
	public final static String UNHANDLED_ERROR_CODE = "1";
	
	/**  Will store the mapping between the component and it's Id (C). */
	public static final Map<String, Integer> COMPONENT_ID_CLASS_MAP = new HashMap<String, Integer>();
	
	static {
		COMPONENT_ID_CLASS_MAP.put(InterchangeEndpoints.class.getName(), 1);
	}
	
	/**
	 * Returns a JSON object with a single error message  => generic 0 error code with one error in the list.
	 * @param errorMessage
	 * @return the json of the error. E.g.: {0: [“Generic error 1”]}
	 */
	public static JsonBean toError(String errorMessage) {
		JsonBean errorBean = new JsonBean();
		errorBean.set(GENERAL_ERROR_CODE, new String[] {errorMessage});
		
		return errorBean;
	};
	
	/**
	 * Returns a JSON object with list of error messages
	 * list of error messages => generic 0 error code with the given list of errors
	 * list of custom errors, where integer will be validated to be between 0 and 99
	 * @param ApiErrorMessage object 
	 * @return the JSON object of the error. E.g.: {0: [“Generic error 1”, “Generic error 2”], 135: [“Forbidden fields have been configured: sector_id”], 123: [“Generic error 1”]}
	 */
	public static JsonBean toError(List<?> errorMessages) {
		JsonBean errorBean = new JsonBean();
		Map<String, List<String>> errors = new HashMap<String, List<String>>();
		
		for(Object errorMessage : errorMessages) {
			if (errorMessage instanceof String) {
				if (errors.get(GENERAL_ERROR_CODE) == null) {
					errors.put(GENERAL_ERROR_CODE, new ArrayList<String>());
				}
				
				errors.get(GENERAL_ERROR_CODE).add((String) errorMessage);
			} else if (errorMessage instanceof ApiErrorMessage) {
				ApiErrorMessage apiError = (ApiErrorMessage) errorMessage;
				String errorId = getErrorId(apiError);
				
				if (errors.get(errorId) == null) {
					errors.put(errorId, new ArrayList<String>());
				}
				
				errors.get(errorId).add(getErrorText(apiError));
			}
		}
		
		for (String errorId : errors.keySet()) {
			errorBean.set(errorId, errors.get(errorId).toArray());
		}
		
		return errorBean;
	};
	
	/**
	 * Returns a JSON object with a single error message. Generic 0 error code with one error in the list.
	 * @param ApiErrorMessage object
	 * @return the json of the error. E.g.: {123: [“Generic error 1”]}
	 */
	public static JsonBean toError(ApiErrorMessage apiErrorMessage) {
		JsonBean errorBean = new JsonBean();
		
		errorBean.set(getErrorId(apiErrorMessage), new String[] {getErrorText(apiErrorMessage)});
		
		return errorBean;
	};
	
	/**
	 * Returns the id of the ApiErrorMessage object. A lookup for the class code will be made on the stacktrace . 
	 * @param ApiErrorMessage object
	 * @return the id of the error
	 */
	private static String getErrorId(ApiErrorMessage apiErrorMessage) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StringBuilder sb = new StringBuilder(); 
		
		for (StackTraceElement st : stackTrace) {
			sb.append(st.toString());
		}
		
		for (String componentName : COMPONENT_ID_CLASS_MAP.keySet()) {
			if (sb.toString().indexOf((componentName)) > 0) {
				String errorId = "" +  COMPONENT_ID_CLASS_MAP.get(componentName) + apiErrorMessage.id;
				return errorId;
			}
		}
		
		return GENERAL_ERROR_CODE + apiErrorMessage.id;
	}
	
	/**
	 * Returns the message of the ApiErrorMessage object.
	 * @param apiErrorMessage object
	 * @see ApiErrorMessage
	 * @return the message of the error
	 */
	private static String getErrorText(ApiErrorMessage apiErrorMessage) {
		
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format(apiErrorMessage.prefix, apiErrorMessage.value);
		formatter.close();
		
		String errorText = sb + " (" + apiErrorMessage.description + ")";
		
		return errorText;
	}
}
