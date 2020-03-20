package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Octavian Ciubotaru
 */
public class JsonConstraintViolation {

    private String jsonPath;

    private ApiErrorMessage error;

    public JsonConstraintViolation(String jsonPath, ApiErrorMessage error) {
        this.jsonPath = jsonPath;
        this.error = error;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public ApiErrorMessage getError() {
        return error;
    }
}
