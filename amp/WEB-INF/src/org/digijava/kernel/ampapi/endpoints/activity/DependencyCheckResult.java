package org.digijava.kernel.ampapi.endpoints.activity;
/**
 * Tri-state defining class for the dependency validator check (used in InterchangeDependencyResolver
 * and DependencyValidator). 
 * @author acartaleanu
 *
 */
public enum DependencyCheckResult {
    /**
     * The field has no value and is always required when some dependency is met, even on save as draft.
     */
    INVALID_ALWAYS_REQUIRED,
    /**
     * Defines the state when a dependency isn't valid because the field the import (and validation) is performed into 
     * isn't available. Example: one cannot import a location if no implementation level is selected, since in AF
     * this field would be unavailable. Same goes for ministry code in case the activity isn't on budget.
     */
    INVALID_NOT_CONFIGURABLE, 
    /**
     * Defines the state when a dependency is fulfilled. 
     */
    VALID;
    
    
    /**
     * 
     * @param other
     * @return
     */
    public static DependencyCheckResult convertToUnavailable(boolean booleanValue) {
        if (booleanValue)
            return VALID;
        else return INVALID_NOT_CONFIGURABLE;
    }
    
    public static DependencyCheckResult convertToAlwaysRequired(boolean booleanValue) {
        if (booleanValue) {
            return VALID;
        }
        return INVALID_ALWAYS_REQUIRED;
    }
}
