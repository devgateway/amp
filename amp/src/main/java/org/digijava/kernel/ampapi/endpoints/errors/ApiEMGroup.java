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
public class ApiEMGroup {
    
    private Map<Integer, ApiErrorMessage> errorGroups= new TreeMap<Integer, ApiErrorMessage>();
    
    public ApiEMGroup() {
    }
    
    public void add(ApiEMGroup anotherErrors) {
        for (ApiErrorMessage em: anotherErrors.getAllErrors()) {
            if (errorGroups.containsKey(em.id)) {
                em = errorGroups.get(em.id).withDetails(em.values);
            }
            errorGroups.put(em.id, em);
        }
    }
    
    public Collection<ApiErrorMessage> getAllErrors() {
        return errorGroups.values();
    }
    
    /**
     * Append error message value to the existing one or create new.
     * @param aemTemplate error message template
     * @param value custom value
     */
    public void addApiErrorMessage(ApiErrorMessage aemTemplate, String value) {
        if (errorGroups.containsKey(aemTemplate.id)) {
            aemTemplate = errorGroups.get(aemTemplate.id).withDetails(value);
        } else {
            aemTemplate = aemTemplate.withDetails(value);
        }
        errorGroups.put(aemTemplate.id, aemTemplate);
    }
    
    public int size() {
        return errorGroups.size();
    }
    
    public boolean isEmpty() {
        return errorGroups.isEmpty();
    }

}
