package org.digijava.kernel.ampapi.endpoints.activity.validators;

import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

import java.util.Map;

/**
 * Defines common base class for input validation
 * 
 * @author Nadejda Mandrescu
 */
public abstract class InputValidator {
    protected boolean continueOnError = false;
    // REFACTOR: seems to not be actually needed
    protected boolean continueOnSuccess = true;
    
    /**
     * Validates new field value configuration
     * @param importer or null if none existed
     * @param newFieldParent parent input JSON definition of the field
     * @param fieldDescription description of the current field (type, required, edit rights, etc)
     * @param fieldPath full field path
     * @return true if chain validation passed
     */
    public abstract boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
            APIField fieldDescription, String fieldPath);
    
    public boolean isValid(ObjectImporter importer, Object currentObject, Map<String, Object> newFieldParent,
                                    APIField fieldDescription, String fieldPath) {
        return isValid(importer, newFieldParent, fieldDescription, fieldPath);
    }
    
    /**
     * @return this validator specific Error message
     */
    public abstract ApiErrorMessage getErrorMessage();
    
    
    /**
     * @return the continueOnError
     */
    public boolean isContinueOnError() {
        return continueOnError;
    }

    /**
     * @param continueOnError the continueOnError to set
     */
    public void setContinueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
    }

    /**
     * @return the continueOnSuccess
     */
    public boolean isContinueOnSuccess() {
        return continueOnSuccess;
    }

    /**
     * @param continueOnSuccess the continueOnSuccess to set
     */
    public void setContinueOnSuccess(boolean continueOnSuccess) {
        this.continueOnSuccess = continueOnSuccess;
    }
    
    public Long getLong(Object o) {
        return o instanceof Number ? ((Number) o).longValue() : null;
    }
}
