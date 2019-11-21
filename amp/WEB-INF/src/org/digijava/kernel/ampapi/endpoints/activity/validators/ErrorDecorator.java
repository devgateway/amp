package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.common.values.BadInput;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

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
        Object newFieldValue = newParent.get(fieldName);
        BadInput newField;

        if (newFieldValue instanceof BadInput) {
            newField = (BadInput) newFieldValue;
        } else {
            newField = new BadInput(newFieldValue);
            newParent.put(fieldName, newField);
        }

        newField.addErrorCode(error.getErrorId());
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
