package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * @author Octavian Ciubotaru
 */
public final class ErrorDecorator {

    private ErrorDecorator() {
    }

    /**
     * Adds a specific error message to the set of errors for the given JSON section (newParent)
     * and fieldName that is part of it
     * @param newParent parent JSON structure to update
     * @param fieldName fieldName
     * @param fieldPath full field path
     * @param error the error to add
     * @param errors the errors stored so far
     */
    public static void addError(Map<String, Object> newParent, String fieldName, String fieldPath,
            ApiErrorMessage error, Map<Integer, ApiErrorMessage> errors) {

        addErrorInJson(newParent, fieldName, error);

        addError(fieldPath, error, errors);
    }

    private static void addErrorInJson(Map<String, Object> newParent, String fieldName, ApiErrorMessage error) {
        // REFACTOR: to split to reuse some functionality in order to be able to add a generic error not necessarily
        // related to a field
        String errorCode = ApiError.getErrorCode(error);
        JsonBean newField = new JsonBean();
        Object newFieldValue = newParent.get(fieldName);
        // if some errors where already reported, use new field storage
        if (newFieldValue instanceof JsonBean && ((JsonBean) newFieldValue).get(ActivityEPConstants.INVALID) != null) {
            newField = (JsonBean) newFieldValue;
        } else {
            // store original input
            newField.set(ActivityEPConstants.INPUT, newFieldValue);
            // and initialize fields errors list
            newField.set(ActivityEPConstants.INVALID, new HashSet<String>());
            // record new wrapped field value
            newParent.put(fieldName, newField);
        }

        // register field level invalid errors
        ((Set<String>) newField.get(ActivityEPConstants.INVALID)).add(errorCode);
    }

    private static void addError(String fieldPath, ApiErrorMessage error, Map<Integer, ApiErrorMessage> errors) {
        // record general errors for the request
        ApiErrorMessage generalError = errors.get(error.id);
        if (generalError == null) {
            generalError = error;
        }
        generalError = generalError.withDetails(fieldPath);
        errors.put(error.id, generalError);
    }
}
