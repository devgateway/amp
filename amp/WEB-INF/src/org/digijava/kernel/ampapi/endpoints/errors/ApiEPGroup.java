/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.errors;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Utility class to temporarily store and group same errors
 * 
 * @author Nadejda Mandrescu
 */
public class ApiEPGroup {
	
	private Map<Integer, ApiErrorMessage> errorGroups= new TreeMap<Integer, ApiErrorMessage>();
	
	public ApiEPGroup() {
	}
	
	public Collection<ApiErrorMessage> getAllErrors() {
		return errorGroups.values();
	}
	
	/**
	 * Append error message value to the existing one or create new.
	 * @param aem error message template
	 * @param value custom value
	 */
	public void addApiErrorMessage(ApiErrorMessage aemTemplate, String value) {
		if (errorGroups.containsKey(aemTemplate.id)) {
			aemTemplate = new ApiErrorMessage(errorGroups.get(aemTemplate.id), value);
		} else {
			aemTemplate = new ApiErrorMessage(aemTemplate, value);
		}
		errorGroups.put(aemTemplate.id, aemTemplate);
	}
	
	public int size() {
		return errorGroups.size();
	}

}
