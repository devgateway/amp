package org.digijava.kernel.ampapi.endpoints.reports.designer.validators;

import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * @author Viorel Chihai
 */
public interface ReportValidator {

    /**
     * Validate the value and return true if the object is valid. Otherwise return false.
     *
     * @param value value to be validated
     * @return true if the value is valid
     */
    boolean isValid(Object value);

    /**
     * Returns the error message
     *
     * @return error message
     */
    ApiErrorMessage getErrorMessage();

}